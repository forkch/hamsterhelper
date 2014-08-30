package ch.furrylittlefriends.hamsterhelper;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import com.squareup.otto.Bus;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by fork on 24.08.14.
 */
public class HamsterHelperApplication extends Application {


    private ObjectGraph objectGraph;
    @Inject
    Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        //setupStrictModePolicies();

        setupDagger();

        //ConfigureLog4J.configure();
        bus.register(this);

    }
    protected void setupDagger() {
        Object[] modules = getModules().toArray();
        objectGraph = ObjectGraph.create(modules);
        objectGraph.inject(this);
    }


    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new HamsterHelperModule(this)
        );
    }

    public <T> T inject(T obj) {
        return objectGraph.inject(obj);
    }

}
