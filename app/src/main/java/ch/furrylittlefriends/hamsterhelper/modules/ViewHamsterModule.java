package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
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
    public ViewHamsterPresenter providesPresenter(ViewHamsterActivity view, HamsterDatabaseRepository hamsterOfflineIteractor, HamsterCloudRepository hamsterListInteractor, Bus bus, JobManager jobManager) {
        return new ViewHamsterPresenter(view, hamsterOfflineIteractor, hamsterListInteractor, bus, jobManager);
    }

}
