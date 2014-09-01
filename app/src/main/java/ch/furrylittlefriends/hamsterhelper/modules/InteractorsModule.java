package ch.furrylittlefriends.hamsterhelper.modules;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterListInteractor;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 01.09.14.
 */
@Module(injects = {},
        library = true)
public class InteractorsModule {

    private HamsterHelperApplication hamsterHelperApplication;

    public InteractorsModule(HamsterHelperApplication hamsterHelperApplication) {

        this.hamsterHelperApplication = hamsterHelperApplication;
    }


    @Provides
    public HamsterListInteractor provideHamsterListInteractor() {
        return new HamsterListInteractor(hamsterHelperApplication.getEndpoint());
    }

}
