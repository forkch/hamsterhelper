package ch.furrylittlefriends.hamsterhelper.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterActivity;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 25.09.14.
 */
@Module(injects = AddHamsterActivity.class,
        addsTo = HamsterHelperModule.class)
public class AddHamsterModule {
    private AddHamsterActivity view;

    public AddHamsterModule(AddHamsterActivity view) {
        this.view = view;
    }

    @Provides
    @Singleton
    public AddHamsterActivity providesView() {
        return view;
    }

    @Provides
    @Singleton
    public AddHamsterPresenter providesPresenter(AddHamsterActivity view, HamsterApiInteractor hamsterListInteractor, Bus bus) {
        return new AddHamsterPresenter(view, hamsterListInteractor, bus);
    }

}
