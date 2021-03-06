package ch.furrylittlefriends.hamsterhelper.ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.bugsnag.android.Bugsnag;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.ui.HamsterActivity;


/**
 * Created with love by fork on 30.05.14.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements HamsterActivity {

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bugsnag.register(this, BuildConfig.BUGSNAG_API_KEY);
        Bugsnag.onActivityCreate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugsnag.onActivityPause(this);
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugsnag.onActivityResume(this);
        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bugsnag.onActivityDestroy(this);
    }


    @Override
    public HamsterHelperApplication getMyApplication() {
        return ((HamsterHelperApplication) getApplication());
    }


}
