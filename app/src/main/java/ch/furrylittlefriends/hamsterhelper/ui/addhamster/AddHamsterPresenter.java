package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.jobs.AddHamsterJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by fork on 25.09.14.
 */
public class AddHamsterPresenter implements DatePickerDialog.OnDateSetListener, NumberPickerDialogFragment.NumberPickerDialogHandler {
    private static final String TAG = AddHamsterPresenter.class.getSimpleName();
    private AddHamsterActivity view;
    private final HamsterOfflineIteractor hamsterOfflineIteractor;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;


    @Icicle
    DateTime selectedBirthday;
    @Icicle
    double weight = 0;
    private final SelectHamsterImagePresenter selectHamsterImagePresenter;


    public AddHamsterPresenter(AddHamsterActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterApiInteractor, Bus bus, JobManager jobManager) {
        this.view = view;
        this.hamsterOfflineIteractor = hamsterOfflineIteractor;
        this.hamsterApiInteractor = hamsterApiInteractor;
        this.bus = bus;
        this.jobManager = jobManager;
        selectHamsterImagePresenter = new SelectHamsterImagePresenter(view);
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

    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
        selectHamsterImagePresenter.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        selectHamsterImagePresenter.onRestoreInstanceState(savedInstanceState);
        view.setHamsterImage(selectHamsterImagePresenter.getImageUri());
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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        selectedBirthday = new DateTime(year, month + 1, day, 0, 0);
        view.setBirthdayText(selectedBirthday);
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
        Uri imageUri = selectHamsterImagePresenter.getImageUri();
        if (imageUri != null) {
            hamster.setTempImageUri(imageUri.toString());
        }
        hamster.save();

        try {
            String path = null;
            if (imageUri != null) {
                FileDescriptor mInputPFD = view.getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor();
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


    @Subscribe
    public void onHamsterAdded(HamsterAddedEvent e) {
        view.onHamsterAdded();
    }

    @Subscribe
    public void onHamstersLoaded(OnHamstersLoadedEvent e) {
        view.addMothers(e.getHamsters());
    }


    public void selectPicture() {
        selectHamsterImagePresenter.selectPicture();
    }

    public void processResult(int requestCode, int resultCode, Intent data) {
        selectHamsterImagePresenter.processResult(requestCode,resultCode,data);
    }
}
