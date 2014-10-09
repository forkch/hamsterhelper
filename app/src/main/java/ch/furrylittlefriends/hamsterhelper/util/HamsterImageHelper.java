package ch.furrylittlefriends.hamsterhelper.util;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.ui.ActivityWithHamsterImage;

/**
 * Created by fork on 09.10.14.
 */
public class HamsterImageHelper {

    private static final String TAG = HamsterImageHelper.class.getSimpleName();
    public static void setHamsterImage(Context context, ActivityWithHamsterImage activity, Hamster hamster) {
        setHamsterImage(context, activity.getImageView(), hamster);
    }

    public static void setHamsterImage(Context context, ImageView imageView, Hamster hamster) {
        String tempUri = hamster.getTempImageUri();
        if (StringUtils.isNotBlank(tempUri)) {
            Log.i(TAG, "setting temporary image " + tempUri);
            Picasso.with(context).load(tempUri).fit().centerCrop().into(imageView);
        } else if (StringUtils.isNotBlank(hamster.getImage())) {
            String imageUrl = "";
            Log.i(TAG, "" + BuildConfig.IS_S3);
            if (!BuildConfig.IS_S3) {
                imageUrl = BuildConfig.ENDPOINT + "api/hamsters/" + hamster.getServerId() + "/image/" + hamster.getImage();
            } else {
                imageUrl = BuildConfig.HAMSTER_IMAGE_ENDPOINT_S3 + hamster.getImage();
            }
            Log.i(TAG, "loading hamster image from " + imageUrl);
            Picasso.with(context).load(imageUrl).fit().centerCrop().into(imageView);
        } else {
            Log.i(TAG, "setting default image");
            Picasso.with(context).load(R.drawable.hamster_image).fit().centerCrop().into(imageView);
        }
    }
}
