package ch.furrylittlefriends.hamsterhelper.ui.viewhamster;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
import ch.furrylittlefriends.hamsterhelper.util.HamsterImageHelper;

/**
 * Created with love by fork on 09.10.14.
 */
public class ViewHamsterPresenter {
    private final ViewHamsterActivity view;
    private final HamsterDatabaseRepository hamsterOfflineIteractor;
    private final HamsterCloudRepository hamsterListInteractor;
    private final Bus bus;
    private final JobManager jobManager;

    public ViewHamsterPresenter(ViewHamsterActivity view, HamsterDatabaseRepository hamsterOfflineIteractor, HamsterCloudRepository hamsterListInteractor, Bus bus, JobManager jobManager) {

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
