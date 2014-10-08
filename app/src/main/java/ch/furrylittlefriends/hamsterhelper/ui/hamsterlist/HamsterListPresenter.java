package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.view.View;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.HamsterDeletedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersSyncedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterOfflineIteractor;
import ch.furrylittlefriends.hamsterhelper.jobs.DeleteHamsterJob;
import ch.furrylittlefriends.hamsterhelper.jobs.HamsterSyncJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.util.NetworkHelper;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter implements HamsterListAdapter.OnDeleteButtonListener {
    private static String TAG = HamsterListPresenter.class.getSimpleName();
    private HamsterListActivity view;
    private final HamsterOfflineIteractor hamsterOfflineIteractor;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;
    private final HamsterListAdapter hamsterListAdapter;

    public HamsterListPresenter(final HamsterListActivity view, HamsterOfflineIteractor hamsterOfflineIteractor, HamsterApiInteractor hamsterListInteractor, Bus bus, JobManager jobManager) {
        this.view = view;
        this.hamsterOfflineIteractor = hamsterOfflineIteractor;
        this.hamsterApiInteractor = hamsterListInteractor;
        this.bus = bus;
        this.jobManager = jobManager;
        hamsterListAdapter =
                new HamsterListAdapter(view, new ArrayList<Hamster>(), this);
        view.setListAdapter(hamsterListAdapter);
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void syncHamsters() {
        if (NetworkHelper.isConnected(view)) {
            jobManager.addJobInBackground(new HamsterSyncJob());
        } else {
            Toast.makeText(view, "not connected", Toast.LENGTH_SHORT).show();
            view.onHamstersLoaded();
        }
    }

    public void loadHamsters() {
        hamsterOfflineIteractor.getAllHamsters();
    }

    @Subscribe
    public void onHamstersSynced(final OnHamstersSyncedEvent e) {
        loadHamsters();
    }

    @Subscribe
    public void onHasmstersLoaded(final OnHamstersLoadedEvent e) {
        hamsterListAdapter.clear();
        hamsterListAdapter.addAll(e.getHamsters());
        view.onHamstersLoaded();
    }

    @Subscribe
    public void onHamsterAdded(HamsterAddedEvent e) {
        if (e.isSyncedWithServer()) {
            loadHamsters();
        }
    }

    @Subscribe
    public void onHamsterDeleted(final HamsterDeletedEvent e) {
        hamsterListAdapter.remove(e.getHamster());
    }

    public void deleteHamster(Hamster hamster) {
        if (!hamster.hasChildren()) {
            jobManager.addJobInBackground(new DeleteHamsterJob(hamster));
        } else {
            view.cannotDeleteHamsterHasChildren(hamster);
        }
    }

    @Override
    public void onDelete(View v) {
        int positionForView = view.getPositionForView(v);
        Hamster hamster = hamsterListAdapter.getItem(positionForView);
        if (StringUtils.isBlank(hamster.getServerId())) {
            Toast.makeText(view, "Hamster not yet synced with server", Toast.LENGTH_SHORT).show();
            return;
        }
        deleteHamster(hamster);
    }
}
