package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import ch.furrylittlefriends.hamsterhelper.events.HamsterDeletedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter implements HamsterListAdapter.OnDelteButtonListener {
    private static String TAG = HamsterListPresenter.class.getSimpleName();
    private HamsterListActivity view;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final HamsterListAdapter hamsterListAdapter;

    public HamsterListPresenter(HamsterListActivity view, HamsterApiInteractor hamsterListInteractor, Bus bus) {
        this.view = view;
        this.hamsterApiInteractor = hamsterListInteractor;
        this.bus = bus;
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

    public void deleteHamster(Hamster item) {
        hamsterApiInteractor.deleteHamster(item);
    }

    @Override
    public void onDelete(View v) {

        int positionForView = view.getPositionForView(v);
        Hamster hamster = hamsterListAdapter.getItem(positionForView);
        deleteHamster(hamster);
    }
}
