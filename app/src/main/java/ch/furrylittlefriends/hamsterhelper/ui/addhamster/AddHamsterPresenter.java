package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.common.collect.Sets;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.jobs.AddHamsterJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.views.SimpleDialog;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by fork on 25.09.14.
 */
public class AddHamsterPresenter implements DatePickerDialog.OnDateSetListener, NumberPickerDialogFragment.NumberPickerDialogHandler {
    private static final String TAG = AddHamsterPresenter.class.getSimpleName();
    public static final String VAL_BIRTHDAY = "VAL_BIRTHDAY";

    private static final Set<String> VALID_IMAGE_MIME_TYPES = Sets.newHashSet("image/jpeg", "image/jpg", "image/png", "image/bmp");

    private AddHamsterActivity view;
    private final HamsterOfflineIteractor hamsterOfflineIteractor;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;


    @Icicle
    DateTime selectedBirthday;
    @Icicle
    double weight = 0;

    private DateTimeFormatter formatter;
    File mImageCaptureFile;
    @Icicle
    Uri mImageCaptureUri;

    private static final String STATE_CAPTURE_URI = "STATE_CAPTURE_URI";
    private static final String STATE_CROPPED_URI = "STATE_CROPPED_URI";
    private static final String STATE_USERNAME = "STATE_USERNAME";
    private static final String STATE_EMAIL = "STATE_EMAIL";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private static final int IMAGE_CROPPED = 3;

    public AddHamsterPresenter(AddHamsterActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterApiInteractor, Bus bus, JobManager jobManager) {
        this.view = view;
        this.hamsterOfflineIteractor = hamsterOfflineIteractor;
        this.hamsterApiInteractor = hamsterApiInteractor;
        this.bus = bus;
        this.jobManager = jobManager;
        formatter = DateTimeFormat.forPattern(view.getString(R.string.birthday_date_format));
    }

    public void onResume() {
        bus.register(this);
        view.setBirthdayText(selectedBirthday);
        view.setWeightText(weight);
        hamsterOfflineIteractor.getAllHamsters();

    }

    public void onPause() {
        bus.unregister(this);
    }

    @Subscribe
    public void onHamsterAdded(HamsterAddedEvent e) {
        view.onHamsterAdded();
    }

    @Subscribe
    public void onHamstersLoaded(OnHamstersLoadedEvent e) {
        view.addMothers(e.getHamsters());
    }

    public void addHamster() {
        String hamsterName = view.getHamsterName();
        boolean isMale = view.isMale();
        String gencode = view.getGencode();

        if (StringUtils.isBlank(hamsterName)) {
            view.validateNameFailed();
            return;
        }
        if (selectedBirthday == null) {
            view.validateBirthdayFailed();
            return;
        }
        Hamster hamster = new Hamster();
        hamster.setName(hamsterName);
        hamster.setMale(isMale);
        hamster.setWeight(weight);
        hamster.setGencode(gencode);
        hamster.setBirthday(selectedBirthday);
        if (mImageCaptureUri != null) {
            hamster.setTempImageUri(mImageCaptureUri.toString());
        }
        hamster.save();

        try {
            String path = null;
            if (mImageCaptureUri != null) {
                FileDescriptor mInputPFD = view.getContentResolver().openFileDescriptor(mImageCaptureUri, "r").getFileDescriptor();
                FileInputStream fileInputStream = new FileInputStream(mInputPFD);
                File tempFile = File.createTempFile("hhh", "jpg");
                FileUtils.copyInputStreamToFile(fileInputStream, tempFile);
                path = tempFile.getAbsolutePath();
            }
            jobManager.addJobInBackground(new AddHamsterJob(hamster, path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showWeightPicker() {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(view.getSupportFragmentManager())
                .addNumberPickerDialogHandler(this)
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setLabelText(view.getString(R.string.weight_unit));
        npb.show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        selectedBirthday = new DateTime(year, month + 1, day, 0, 0);
        view.setBirthdayText(selectedBirthday);
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        this.weight = fullNumber;
        view.setWeightText(weight);
    }

    public void showBirtdayPicker() {
        if (selectedBirthday == null) {
            selectedBirthday = DateTime.now();
        }
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, selectedBirthday.getYear(), selectedBirthday.getMonthOfYear() - 1, selectedBirthday.getDayOfMonth(), true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(view.getSupportFragmentManager(), TAG);
    }

    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        view.setHamsterImage(mImageCaptureUri);
    }

    public void selectPicture() {

        SimpleDialog.DialogBuilder dialogBuilder = new SimpleDialog.DialogBuilder(view, view.getFragmentManager());
        dialogBuilder.setTitle("Choose picture")
                .setPrimaryButton("Make a picture", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            File hamsterhelperDirectory = new File(Environment.getExternalStorageDirectory(), "hamsterhelper");
                            hamsterhelperDirectory.mkdirs();
                            mImageCaptureFile = new File(hamsterhelperDirectory,
                                    "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                            mImageCaptureUri = Uri.fromFile(mImageCaptureFile);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                            try {
                                intent.putExtra("return-data", false);
                                view.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                view.startActivityForResult(intent, PICK_FROM_CAMERA);

                            } catch (ActivityNotFoundException e) {
                                Log.e(TAG, "no activity found for intent " + MediaStore.ACTION_IMAGE_CAPTURE);
                                Toast.makeText(view, "no camera app installed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(view, "no external storage", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setSecondaryButton("Select from gallery", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.putExtra("return-data", false);
                intent.setAction(Intent.ACTION_GET_CONTENT);

                view.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
            }
        })
                .setSecondaryButton("Select from Filemanager", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();

                        intent.setType("file/*");
                        intent.putExtra("return-data", false);
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        view.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                    }
                }).show();
    }

    public void processResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            cleanupFiles();
            return;
        }

        if (requestCode == PICK_FROM_FILE) {
            if (data == null) {
                Log.i(TAG, "user cancelled taking picture from gallery");
                return;
            }
            mImageCaptureUri = data.getData();
        }

        Log.i(TAG, "URI of image: " + mImageCaptureUri.toString());
        String scheme = mImageCaptureUri.getScheme();
        String type = "";
        if(StringUtils.contains(scheme, "content")) {
            type = view.getContentResolver().getType(mImageCaptureUri);
        }else {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            type = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mImageCaptureUri.toString().toLowerCase()));
        }
        if (checkMimeType(type)) {
            view.setHamsterImage(mImageCaptureUri);
        }
    }

    private boolean checkMimeType(String type) {
        for (String validImageMimeType : VALID_IMAGE_MIME_TYPES) {
            if (StringUtils.contains(type, validImageMimeType))
                return true;
        }
        return false;
    }

    private void cleanupFiles() {
        if (mImageCaptureFile != null && mImageCaptureFile.exists()) {
            mImageCaptureFile.delete();
        }
        mImageCaptureUri = null;
    }

}
