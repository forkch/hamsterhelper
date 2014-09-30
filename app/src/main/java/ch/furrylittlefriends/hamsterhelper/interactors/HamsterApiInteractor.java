package ch.furrylittlefriends.hamsterhelper.interactors;

import android.util.Log;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterApiInteractor {

    private static final String TAG = HamsterApiInteractor.class.getSimpleName();


    private final HamsterService hamsterService;
    private final Bus bus;

    public HamsterApiInteractor(Bus bus) {
        this.bus = bus;

        Gson gson = Converters.registerAll(new GsonBuilder()).create();

        String s = gson.toJson(new DateTime());
        GsonConverter converter = new GsonConverter(gson);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(converter)
                .setEndpoint(BuildConfig.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(TAG))
                .build();

        hamsterService = restAdapter.create(HamsterService.class);
    }

    public void getAllHamsters() {

        /*hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {
                bus.post(new
                        OnHamstersLoadedEvent(hamsters));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/

        Observable<List<Hamster>> allHamstersObs = hamsterService.getAllHamstersObs();
        allHamstersObs.subscribe(new Action1<List<Hamster>>() {
            @Override
            public void call(List<Hamster> hamsters) {
                bus.post(new
                        OnHamstersLoadedEvent(hamsters));
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
}
