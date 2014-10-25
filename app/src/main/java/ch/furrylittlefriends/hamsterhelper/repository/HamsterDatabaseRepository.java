package ch.furrylittlefriends.hamsterhelper.repository;

import android.util.Log;

import com.activeandroid.query.Select;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.TreeSet;

import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 05.10.14.
 */
public class HamsterDatabaseRepository implements HamsterRepository {

    private static final String TAG = HamsterDatabaseRepository.class.getSimpleName();

    private final Bus bus;

    public HamsterDatabaseRepository(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void getAllHamters() {
        Log.i(TAG, "loading hamsters from database");
        List<Hamster> hamsters = new Select().from(Hamster.class).execute();

        Log.i(TAG, "loaded " + hamsters.size() + " hamsters from database");
        bus.post(new OnHamstersLoadedEvent(new TreeSet<Hamster>(hamsters)));
    }

    @Override
    public Hamster storeHamster(Hamster hamster) {
        return null;
    }

    @Override
    public void deleteHamster(Hamster hamster) {

    }

    @Override
    public String saveHamsterImage(Hamster hamster, String path) {
        return null;
    }
}
