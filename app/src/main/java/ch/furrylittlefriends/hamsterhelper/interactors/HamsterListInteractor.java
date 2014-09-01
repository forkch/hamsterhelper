package ch.furrylittlefriends.hamsterhelper.interactors;

import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.presenters.OnHamstersLoadedListener;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListInteractor {

    private static final String TAG = HamsterListInteractor.class.getSimpleName();
    private String endpoint;

    @Inject
    Bus bus;

    public HamsterListInteractor(String endpoint) {

        this.endpoint = endpoint;
    }
    public void getAllHamsters(final OnHamstersLoadedListener onHamstersLoadedListener) {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();

        HamsterService hamsterService = restAdapter.create(HamsterService.class);

        hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {
                onHamstersLoadedListener.onHamstersLoaded(hamsters);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
