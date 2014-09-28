package ch.furrylittlefriends.hamsterhelper.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 27.09.14.
 */
@Module(complete=false,library = true)
public class InteractorsModule {

    @Provides
    @Singleton
    public HamsterApiInteractor provideHamsterListInteractor(Bus bus) {
        return new HamsterApiInteractor(bus);
    }
}
