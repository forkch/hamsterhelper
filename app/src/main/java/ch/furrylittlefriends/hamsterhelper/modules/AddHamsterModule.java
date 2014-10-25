package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
import ch.furrylittlefriends.hamsterhelper.ui.addhamster.AddHamsterActivity;
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
    public AddHamsterPresenter providesPresenter(AddHamsterActivity view, HamsterDatabaseRepository hamsterOfflineIteractor, HamsterCloudRepository hamsterListInteractor, Bus bus, JobManager jobManager) {
        return new AddHamsterPresenter(view, hamsterOfflineIteractor, hamsterListInteractor, bus, jobManager);
    }

}
