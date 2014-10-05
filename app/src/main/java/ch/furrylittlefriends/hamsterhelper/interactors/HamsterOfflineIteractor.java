package ch.furrylittlefriends.hamsterhelper.interactors;

import android.util.Log;

import com.activeandroid.query.Select;
import com.squareup.otto.Bus;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.RestAdapter;

/**
 * Created by fork on 05.10.14.
 */
public class HamsterOfflineIteractor {

    private static final String TAG = HamsterOfflineIteractor.class.getSimpleName();

    private final Bus bus;

    public HamsterOfflineIteractor(Bus bus) {
        this.bus = bus;
    }

    public void getAllHamsters() {
        Log.i(TAG, "loading hamsters from database");
        List<Hamster> hamsters = new Select().from(Hamster.class).execute();

        Log.i(TAG, "loaded " + hamsters.size() + " hamsters from database");
        bus.post(new OnHamstersLoadedEvent(hamsters));
    }
}
