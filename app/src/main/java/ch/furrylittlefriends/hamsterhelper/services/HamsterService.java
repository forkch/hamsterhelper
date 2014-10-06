package ch.furrylittlefriends.hamsterhelper.services;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import rx.Observable;

/**
 * Created by fork on 30.08.14.
 */
public interface HamsterService {

    @GET("/api/hamsters")
    public void getAllHamsters(Callback<List<Hamster>> cb );

    @GET("/api/hamsters")
    public Observable<List<Hamster>> getAllHamstersObs();


    @POST("/api/hamsters")
    public void addHamster(@Body Hamster hamster, Callback<Hamster> cb);

    @POST("/api/hamsters")
    public Hamster addHamster(@Body Hamster hamster);

    @DELETE("/api/hamsters/{id}" )
    public void deleteHamster(@Path("id") String id, Callback<Void> cb);

    @Multipart
    @POST("/api/hamsters/{id}/image")
    public String uploadImage(@Path("id") String serverId, @Part("image")TypedFile image);
}
