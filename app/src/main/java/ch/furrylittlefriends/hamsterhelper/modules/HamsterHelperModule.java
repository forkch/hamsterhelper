package ch.furrylittlefriends.hamsterhelper.modules;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.ui.SettingsActivity;
import ch.furrylittlefriends.hamsterhelper.bus.MainThreadBus;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 30.05.14.
 */

@Module(
        injects = {HamsterHelperApplication.class,
                SettingsActivity.class},
        library = true,
        includes = InteractorsModule.class

)
public class HamsterHelperModule {

    private final Context context;
    private HamsterHelperApplication hamsterHelperApplication;

    public HamsterHelperModule(HamsterHelperApplication hamsterHelperApplication) {
        this.hamsterHelperApplication = hamsterHelperApplication;
        this.context = hamsterHelperApplication.getApplicationContext();
    }

    @Provides
    public HamsterHelperApplication provideHamsterHelperApplication() {
        return this.hamsterHelperApplication;
    }

    @Provides
    @Singleton
    public Bus providesEventBusread() {
        return new MainThreadBus();
    }

}
