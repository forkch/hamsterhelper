package ch.furrylittlefriends.hamsterhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Named;
import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.activities.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.activities.SettingsActivity;
import ch.furrylittlefriends.hamsterhelper.modules.InteractorsModule;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 30.05.14.
 */

@Module(
        injects = {HamsterHelperApplication.class,
                SettingsActivity.class},
        includes = InteractorsModule.class

)
public class HamsterHelperModule {

    private final Context context;
    private HamsterHelperApplication hamsterHelperApplication;

    HamsterHelperModule(HamsterHelperApplication hamsterHelperApplication) {
        this.hamsterHelperApplication = hamsterHelperApplication;
        this.context = hamsterHelperApplication.getApplicationContext();
    }

    @Provides
    public HamsterHelperApplication provideHamsterHelperApplication() {
        return this.hamsterHelperApplication;
    }


    @Provides
    @Singleton
    public Bus providesEventBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

}
