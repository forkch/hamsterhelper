package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterActivity;
import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterPresenter;
import ch.furrylittlefriends.hamsterhelper.ui.viewhamster.ViewHamsterActivity;
import ch.furrylittlefriends.hamsterhelper.ui.viewhamster.ViewHamsterPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 25.09.14.
 */
@Module(injects = ViewHamsterActivity.class,
        addsTo = HamsterHelperModule.class)
public class ViewHamsterModule {
    private ViewHamsterActivity view;

    public ViewHamsterModule(ViewHamsterActivity view) {
        this.view = view;
    }

    @Provides
    @Singleton
    public ViewHamsterActivity providesView() {
        return view;
    }

    @Provides
    @Singleton
    public ViewHamsterPresenter providesPresenter(ViewHamsterActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterListInteractor, Bus bus, JobManager jobManager) {
        return new ViewHamsterPresenter(view, hamsterOfflineIteractor, hamsterListInteractor, bus, jobManager);
    }

}
