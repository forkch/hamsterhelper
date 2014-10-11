package ch.furrylittlefriends.hamsterhelper.ui.viewhamster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.modules.ViewHamsterModule;
import ch.furrylittlefriends.hamsterhelper.ui.ActivityWithHamsterImage;
import ch.furrylittlefriends.hamsterhelper.ui.base.BaseActivity;
import ch.furrylittlefriends.hamsterhelper.ui.base.BaseFragmentActivity;
import ch.furrylittlefriends.hamsterhelper.ui.ExtraDataKeys;

public class ViewHamsterActivity extends BaseFragmentActivity implements ActivityWithHamsterImage {

    @Inject
    ViewHamsterPresenter presenter;

    @InjectView(R.id.hamsterImage)
    ImageView hamsterImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.color.add_hamster_picture_button_color)
                .headerLayout(R.layout.header_hamster_image)
                .contentLayout(R.layout.activity_view_hamster);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        getMyApplication().createScopedGraphAndInject(this, new ViewHamsterModule(this));
        ButterKnife.inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        Intent intent = getIntent();
        Hamster hamster = (Hamster) intent.getExtras().getSerializable(ExtraDataKeys.HAMSTER);
        presenter.loadHamster(hamster);
    }

    @Override
    protected void onStop() {
        presenter.onPause();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_hamster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setHamsterImage(Uri mImageCaptureUri) {
        if (mImageCaptureUri == null) {
            Picasso.with(this).load(R.drawable.hamster_image).fit().centerCrop().into(hamsterImage);
        } else {
            Picasso.with(this).load(mImageCaptureUri).fit().centerCrop().into(hamsterImage);
        }
    }

    @Override
    public ImageView getImageView() {
        return hamsterImage;
    }
}
