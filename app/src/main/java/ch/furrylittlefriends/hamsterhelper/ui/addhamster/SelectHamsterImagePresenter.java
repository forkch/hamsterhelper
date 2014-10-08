package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
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

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Set;

import ch.furrylittlefriends.hamsterhelper.views.SimpleDialog;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by fork on 08.10.14.
 */
public class SelectHamsterImagePresenter {
    private static final String TAG = SelectHamsterImagePresenter.class.getSimpleName();


    private static final Set<String> VALID_IMAGE_MIME_TYPES = Sets.newHashSet("image/jpeg", "image/jpg", "image/png", "image/bmp");

    private final AddHamsterActivity view;
    private File mImageCaptureFile;
    @Icicle
    Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    public SelectHamsterImagePresenter(AddHamsterActivity view) {

        this.view = view;
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

    private void cleanupFiles() {
        if (mImageCaptureFile != null && mImageCaptureFile.exists()) {
            mImageCaptureFile.delete();
        }
        mImageCaptureUri = null;
    }

    public Uri getImageUri() {
        return mImageCaptureUri;
    }

    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    private boolean checkMimeType(String type) {
        for (String validImageMimeType : VALID_IMAGE_MIME_TYPES) {
            if (StringUtils.contains(type, validImageMimeType))
                return true;
        }
        return false;
    }

}
