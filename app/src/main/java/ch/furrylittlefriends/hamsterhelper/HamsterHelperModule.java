package ch.furrylittlefriends.hamsterhelper;

import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.activities.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.activities.SettingsActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 30.05.14.
 */

@Module(
        injects = {HamsterHelperApplication.class,
                HamsterListActivity.class,
                SettingsActivity.class}
)
public class HamsterHelperModule {

    private final Context context;
    private HamsterHelperApplication hamsterHelperApplication;

    HamsterHelperModule(HamsterHelperApplication hamsterHelperApplication) {
        this.hamsterHelperApplication = hamsterHelperApplication;
        this.context = hamsterHelperApplication.getApplicationContext();
    }

    @Provides
    public HamsterHelperApplication providesAdHocRailwayApplication() {
        return this.hamsterHelperApplication;
    }


    @Provides
    @Singleton
    public Bus providesEventBus() {
        return new Bus(ThreadEnforcer.ANY);
    }
}
