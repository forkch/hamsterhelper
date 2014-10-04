package ch.furrylittlefriends.hamsterhelper.modules;

import android.content.Context;
import android.util.Log;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.path.android.jobqueue.BaseJob;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.log.CustomLogger;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

import javax.inject.Singleton;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.bus.MainThreadBus;
import ch.furrylittlefriends.hamsterhelper.jobs.AddHamsterJob;
import ch.furrylittlefriends.hamsterhelper.ui.SettingsActivity;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

/**
 * Created by fork on 30.05.14.
 */

@Module(
        injects = {HamsterHelperApplication.class,
                SettingsActivity.class,
                AddHamsterJob.class},
        library = true,
        includes = InteractorsModule.class

)
public class HamsterHelperModule {

    private final Context context;
    private HamsterHelperApplication hamsterHelperApplication;

    public HamsterHelperModule(HamsterHelperApplication hamsterHelperApplication) {
        this.hamsterHelperApplication = hamsterHelperApplication;
        this.context = hamsterHelperApplication.getApplicationContext();
    }

    @Provides
    public HamsterHelperApplication provideHamsterHelperApplication() {
        return this.hamsterHelperApplication;
    }

    @Provides
    @Singleton
    public Bus providesEventBus() {
        return MainThreadBus.getMainThreadBus();
    }

    @Provides
    @Singleton
    public JobManager providesJobManager() {
        JobManager jobManager = new JobManager(hamsterHelperApplication, configureJobManager());
        return jobManager;
    }

    private Configuration configureJobManager() {
        Configuration configuration = new Configuration.Builder(hamsterHelperApplication)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute

                .injector(new DependencyInjector() {
                    @Override
                    public void inject(BaseJob baseJob) {
                        hamsterHelperApplication.inject(baseJob);
                    }
                })
                .build();
        return configuration;

    }


    @Provides
    @Singleton
    public RestAdapter providesRestAdapter() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = Converters.registerAll(builder).create();
        String s = gson.toJson(new DateTime());
        GsonConverter converter = new GsonConverter(gson);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(converter)
                .setEndpoint(BuildConfig.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("REST: "))
                .build();
        return restAdapter;
    }

}
