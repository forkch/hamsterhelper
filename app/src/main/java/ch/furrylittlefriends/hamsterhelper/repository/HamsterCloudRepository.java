package ch.furrylittlefriends.hamsterhelper.repository;

import android.util.Log;

import com.activeandroid.query.Delete;
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
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterCloudRepository implements HamsterRepository {

    private static final String TAG = HamsterCloudRepository.class.getSimpleName();

    private final HamsterService hamsterService;
    private final Bus bus;

    public HamsterCloudRepository(Bus bus, RestAdapter restAdapter) {
        this.bus = bus;
        hamsterService = restAdapter.create(HamsterService.class);
    }

    @Override
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
        getAllHamters();
    }


    @Override
    public void getAllHamters() {

        hamsterService.getAllHamstersObs().map(new Func1<List<Hamster>, List<Hamster>>() {
            @Override
            public List<Hamster> call(List<Hamster> hamsters) {

                new Delete().from(Hamster.class).execute();
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
    }

    @Override
    public Hamster storeHamster(Hamster hamster) {
        return hamsterService.addHamster(hamster);
    }

    @Override
    public String saveHamsterImage(Hamster hamster, String path) {
        File f = new File(path);
        String serverImageId = hamsterService.uploadImage(hamster.getServerId(), new TypedFile("image/jpeg", f));
        f.delete();
        return serverImageId;
    }
}
