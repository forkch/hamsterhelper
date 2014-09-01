package ch.furrylittlefriends.hamsterhelper;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.otto.Bus;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.activities.SettingsActivity;
import ch.furrylittlefriends.hamsterhelper.modules.InteractorsModule;
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

    public <T> T inject(T obj) {
        return objectGraph.inject(obj);
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    public <T> void createScopedGraphAndInject(T obj, Object... modules) {
        createScopedGraph(modules).inject(obj);
    }

    protected void setupDagger() {
        Object[] modules = getModules().toArray();
        objectGraph = ObjectGraph.create(modules);
        objectGraph.inject(this);
    }


    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new HamsterHelperModule(this), new InteractorsModule(this)
        );
    }


    public String getEndpoint() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String endpoint = preferences.getString(SettingsActivity.KEY_ENDPOINT, "http://10.0.2.2:9000");
        return endpoint;
    }
}
