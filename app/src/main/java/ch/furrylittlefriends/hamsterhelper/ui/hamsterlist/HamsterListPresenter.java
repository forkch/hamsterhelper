package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.content.Intent;
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
import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterDatabaseRepository;
import ch.furrylittlefriends.hamsterhelper.jobs.DeleteHamsterJob;
import ch.furrylittlefriends.hamsterhelper.jobs.HamsterSyncJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.ui.ExtraDataKeys;
import ch.furrylittlefriends.hamsterhelper.ui.viewhamster.ViewHamsterActivity;
import ch.furrylittlefriends.hamsterhelper.util.NetworkHelper;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter implements HamsterListAdapter.HamsterListRowListener {
    private static String TAG = HamsterListPresenter.class.getSimpleName();
    private HamsterListActivity view;
    private final HamsterDatabaseRepository hamsterOfflineIteractor;
    private HamsterCloudRepository hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;
    private final HamsterListAdapter hamsterListAdapter;

    public HamsterListPresenter(final HamsterListActivity view, HamsterDatabaseRepository hamsterOfflineIteractor, HamsterCloudRepository hamsterListInteractor, Bus bus, JobManager jobManager) {
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
        hamsterOfflineIteractor.getAllHamters();
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

    @Override
    public void onHamsterClick(int position) {
        Hamster hamster = hamsterListAdapter.getItem(position);

        Intent viewHamsterIntent = new Intent(view, ViewHamsterActivity.class);
        viewHamsterIntent.putExtra(ExtraDataKeys.HAMSTER, hamster);
        view.startActivity(viewHamsterIntent);
    }
}
