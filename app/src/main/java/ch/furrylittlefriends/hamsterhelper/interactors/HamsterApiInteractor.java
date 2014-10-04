package ch.furrylittlefriends.hamsterhelper.interactors;

import android.util.Log;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.HamsterDeletedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

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

    public void getAllHamsters() {

        hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {

                Map<String, Hamster> idMap = new HashMap<String, Hamster>();
                for (Hamster h : hamsters) {
                    idMap.put(h.getServerId(), h);
                }
                for (Hamster h : hamsters) {
                    h.setMother(idMap.get(h.getMotherId()));
                    h.setFather(idMap.get(h.getFatherId()));
                    h.save();
                }
                bus.post(new
                        OnHamstersLoadedEvent(hamsters));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void addHamster(Hamster hamster) {

        hamsterService.addHamster(hamster, new Callback<Hamster>() {
            @Override
            public void success(Hamster hamster, Response response) {
                bus.post(new HamsterAddedEvent(hamster));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });

    }

    public void addHamsterSync(Hamster hamster) {

        hamsterService.addHamster(hamster);

    }

    public void deleteHamster(final Hamster hamster) {
        hamsterService.deleteHamster(hamster.getServerId(), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Log.i(TAG, "deleted hamster");
                bus.post(new HamsterDeletedEvent(hamster));
            }

            @Override
            public void failure(RetrofitError error) {

                Log.i(TAG, "failed to delete hamster");
            }
        });
    }
}
