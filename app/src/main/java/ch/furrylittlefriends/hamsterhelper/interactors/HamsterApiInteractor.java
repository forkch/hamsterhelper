package ch.furrylittlefriends.hamsterhelper.interactors;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.squareup.otto.Bus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.furrylittlefriends.hamsterhelper.events.OnHamstersSyncedEvent;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.functions.Action1;
import rx.functions.Func1;

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

        hamsterService.getAllHamstersObs().map(new Func1<List<Hamster>, List<Hamster>>() {
            @Override
            public List<Hamster> call(List<Hamster> hamsters) {

                Map<String, Hamster> idMap = new HashMap<String, Hamster>();
                for (Hamster h : hamsters) {
                    idMap.put(h.getServerId(), h);
                    h.save();
                }

                for (Hamster h : hamsters) {
                    h.setMother(idMap.get(h.getMotherServerId()));
                    h.setFather(idMap.get(h.getFatherServerId()));
                    h.save();
                }

                return hamsters;
            }
        }).map(new Func1<List<Hamster>, SortedSet<Hamster>>() {
            @Override
            public SortedSet<Hamster> call(List<Hamster> hamsters) {
                return new TreeSet<Hamster>(hamsters);
            }
        }).subscribe(new Action1<SortedSet<Hamster>>() {
            @Override
            public void call(SortedSet<Hamster> hamsters) {
                bus.post(new OnHamstersSyncedEvent());
            }
        });

        /*hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
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
        });*/
    }

    public String uploadImage(Hamster hamster, File imageFile) {
       return hamsterService.uploadImage(hamster.getServerId(), new TypedFile("image/jpeg", imageFile));
    }

}
