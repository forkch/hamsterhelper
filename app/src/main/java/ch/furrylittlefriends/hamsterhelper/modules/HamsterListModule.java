package ch.furrylittlefriends.hamsterhelper.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperModule;
import ch.furrylittlefriends.hamsterhelper.activities.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterListInteractor;
import ch.furrylittlefriends.hamsterhelper.presenters.HamsterListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 01.09.14.
 */
@Module(injects = HamsterListActivity.class,
        addsTo = HamsterHelperModule.class,
        library = true)
public class HamsterListModule {

    private HamsterListActivity view;

    public HamsterListModule(HamsterListActivity view) {
        this.view = view;
    }

    @Provides
    @Singleton
    public HamsterListActivity provideView() {
        return view;
    }

    @Provides
    @Singleton
    public HamsterListPresenter providePresenter(HamsterListActivity view, HamsterListInteractor hamsterListInteractor, Bus bus) {
        return new HamsterListPresenter(view, hamsterListInteractor, bus);
    }
}
