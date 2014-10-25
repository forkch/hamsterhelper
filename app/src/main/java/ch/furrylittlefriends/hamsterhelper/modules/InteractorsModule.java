package ch.furrylittlefriends.hamsterhelper.modules;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
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
    public HamsterCloudRepository provideHamsterListInteractor(Bus bus, RestAdapter restAdapter) {
        return new HamsterCloudRepository(bus, restAdapter);
    }

    @Provides
    @Singleton
    public HamsterDatabaseRepository provideHamsterListInteractor(Bus bus) {
        return new HamsterDatabaseRepository(bus);
    }
}
