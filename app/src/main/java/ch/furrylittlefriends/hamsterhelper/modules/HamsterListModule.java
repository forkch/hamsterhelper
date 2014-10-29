package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
import ch.furrylittlefriends.hamsterhelper.ui.hamsterlist.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.ui.hamsterlist.HamsterListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created with love by fork on 01.09.14.
 */
@Module(injects = HamsterListActivity.class,
        addsTo = HamsterHelperModule.class,
        includes = InteractorsModule.class)
public class HamsterListModule {

    private final HamsterListActivity view;

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
    public HamsterListPresenter providePresenter(HamsterListActivity view, HamsterDatabaseRepository hamsterOfflineIteractor, HamsterCloudRepository hamsterListInteractor, Bus bus, JobManager jobManager) {
        return new HamsterListPresenter(view, hamsterOfflineIteractor, hamsterListInteractor, bus, jobManager);
    }
}
