package ch.furrylittlefriends.hamsterhelper.ui;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;


/**
 * Created by fork on 30.05.14.
 */
public abstract class BaseActivity extends Activity implements HamsterActivity {

    @Inject
    Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public HamsterHelperApplication getMyApplication() {
        return ((HamsterHelperApplication)getApplication());
    }
}