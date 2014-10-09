package ch.furrylittlefriends.hamsterhelper.ui.viewhamster;

import android.util.Log;
import android.widget.ImageView;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import ch.furrylittlefriends.hamsterhelper.BuildConfig;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.util.HamsterImageHelper;

/**
 * Created by fork on 09.10.14.
 */
public class ViewHamsterPresenter {
    private final ViewHamsterActivity view;
    private final HamsterOfflineIteractor hamsterOfflineIteractor;
    private final HamsterApiInteractor hamsterListInteractor;
    private final Bus bus;
    private final JobManager jobManager;

    public ViewHamsterPresenter(ViewHamsterActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterListInteractor, Bus bus, JobManager jobManager) {

        this.view = view;
        this.hamsterOfflineIteractor = hamsterOfflineIteractor;
        this.hamsterListInteractor = hamsterListInteractor;
        this.bus = bus;
        this.jobManager = jobManager;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void loadHamster(Hamster hamster) {
        HamsterImageHelper.setHamsterImage(view, view, hamster);
    }
}
