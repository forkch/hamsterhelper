package ch.furrylittlefriends.hamsterhelper.modules;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by fork on 27.09.14.
 */
@Module(complete=false,library = true)
public class InteractorsModule {

    @Provides
    @Singleton
    public HamsterApiInteractor provideHamsterListInteractor(Bus bus, RestAdapter restAdapter) {
        return new HamsterApiInteractor(bus, restAdapter);
    }
}
