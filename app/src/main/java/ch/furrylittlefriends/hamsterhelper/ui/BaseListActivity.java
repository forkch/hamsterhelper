package ch.furrylittlefriends.hamsterhelper.ui;

import android.app.ListActivity;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;


/**
 * Created by fork on 30.05.14.
 */
public abstract class BaseListActivity extends ListActivity implements HamsterActivity {

    @Override
    public HamsterHelperApplication getMyApplication() {
       return ((HamsterHelperApplication)getApplication());
    }
}
