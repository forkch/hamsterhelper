package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.ui.hamsterlist.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.ui.hamsterlist.HamsterListPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fork on 01.09.14.
 */
@Module(injects = HamsterListActivity.class,
        addsTo = HamsterHelperModule.class,
        includes = InteractorsModule.class)
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
    public HamsterListPresenter providePresenter(HamsterListActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterListInteractor, Bus bus, JobManager jobManager) {
        return new HamsterListPresenter(view, hamsterOfflineIteractor, hamsterListInteractor, bus, jobManager);
    }
}
