package ch.furrylittlefriends.hamsterhelper.interactors;

import android.util.Log;

import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
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
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BuildConfig.ENDPOINT).build();

        hamsterService = restAdapter.create(HamsterService.class);
    }

    public void getAllHamsters() {

        /*hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {

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
