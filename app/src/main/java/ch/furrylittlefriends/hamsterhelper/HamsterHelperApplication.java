package ch.furrylittlefriends.hamsterhelper;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.activeandroid.ActiveAndroid;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.modules.HamsterHelperModule;
import ch.furrylittlefriends.hamsterhelper.ui.SettingsActivity;
import dagger.ObjectGraph;

/**
 * Created with love by fork on 24.08.14.
 */
public class HamsterHelperApplication extends Application {

    private ObjectGraph objectGraph;
    @Inject
    Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        setupStrictModePolicies();
        setupDagger();

        //ConfigureLog4J.configure();
        bus.register(this);

    }

    private void setupStrictModePolicies() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork().detectCustomSlowCalls().detectDiskReads().detectDiskWrites().penaltyLog().penaltyDeath().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectActivityLeaks()
                .penaltyLog().build());
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
                new HamsterHelperModule(this)
        );
    }


    protected List<Object> getModules(Object... addidtionalModules) {
        List<Object> modules = new ArrayList<Object>();

        modules.addAll(Arrays.<Object>asList(
                new HamsterHelperModule(this)));
        modules.addAll(Arrays.<Object>asList(addidtionalModules));
        return modules;
    }


    public String getEndpoint() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(SettingsActivity.KEY_ENDPOINT, "http://10.0.2.2:9000");
    }
}
