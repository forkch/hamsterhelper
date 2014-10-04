package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.view.View;

import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ch.furrylittlefriends.hamsterhelper.events.HamsterDeletedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.jobs.DeleteHamsterJob;
import ch.furrylittlefriends.hamsterhelper.jobs.HamsterSyncJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter implements HamsterListAdapter.OnDelteButtonListener {
    private static String TAG = HamsterListPresenter.class.getSimpleName();
    private HamsterListActivity view;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;
    private final HamsterListAdapter hamsterListAdapter;

    public HamsterListPresenter(HamsterListActivity view, HamsterApiInteractor hamsterListInteractor, Bus bus, JobManager jobManager) {
        this.view = view;
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
        jobManager.addJobInBackground(new HamsterSyncJob());
    }

    public void loadHamsters() {
        hamsterApiInteractor.getAllHamsters();
    }

    @Subscribe
    public void onHamstersLoaded(final OnHamstersLoadedEvent e) {
        hamsterListAdapter.clear();
        hamsterListAdapter.addAll(e.getHamsters());
        view.onHamstersLoaded();
        view.ensureAddButtonVisibility();
    }

    @Subscribe
    public void onHamsterDeleted(final HamsterDeletedEvent e) {
        loadHamsters();
        view.ensureAddButtonVisibility();
    }

    public void deleteHamster(Hamster hamster) {
        if(!hamster.hasChildren()) {
            jobManager.addJobInBackground(new DeleteHamsterJob(hamster));
        } else {
            view.cannotDeleteHamsterHasChildren(hamster);
        }
    }

    @Override
    public void onDelete(View v) {

        int positionForView = view.getPositionForView(v);
        Hamster hamster = hamsterListAdapter.getItem(positionForView);
        deleteHamster(hamster);
    }
}
