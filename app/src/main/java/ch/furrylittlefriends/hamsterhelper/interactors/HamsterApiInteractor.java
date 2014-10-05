package ch.furrylittlefriends.hamsterhelper.interactors;

import android.util.Log;

import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.furrylittlefriends.hamsterhelper.events.OnHamstersSyncedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterApiInteractor {

    private static final String TAG = HamsterApiInteractor.class.getSimpleName();

    private final HamsterService hamsterService;
    private final Bus bus;

    public HamsterApiInteractor(Bus bus, RestAdapter restAdapter) {
        this.bus = bus;

        hamsterService = restAdapter.create(HamsterService.class);
    }


    public Hamster addHamsterSync(Hamster hamster) {
        return hamsterService.addHamster(hamster);
    }

    public void deleteHamster(final Hamster hamster) {
        hamsterService.deleteHamster(hamster.getServerId(), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Log.i(TAG, "deleted hamster");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "failed to delete hamster");
            }
        });
    }

    public void sync() {
        hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {

                Map<String, Hamster> idMap = new HashMap<String, Hamster>();
                for (Hamster h : hamsters) {
                    idMap.put(h.getServerId(), h);
                }
                for (Hamster h : hamsters) {
                    h.setMother(idMap.get(h.getMotherServerId()));
                    h.setFather(idMap.get(h.getFatherServerId()));
                    h.save();
                }
                bus.post(new OnHamstersSyncedEvent());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
