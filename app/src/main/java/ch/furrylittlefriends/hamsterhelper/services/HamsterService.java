package ch.furrylittlefriends.hamsterhelper.services;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by fork on 30.08.14.
 */
public interface HamsterService {

    @GET("/api/hamsters")
    public void getAllHamsters(Callback<List<Hamster>> cb );
}
