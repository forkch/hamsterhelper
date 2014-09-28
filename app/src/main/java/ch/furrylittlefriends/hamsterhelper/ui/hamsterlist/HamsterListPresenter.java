package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import ch.furrylittlefriends.hamsterhelper.adapters.HamsterListAdapter;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter {
    private static String TAG = HamsterListPresenter.class.getSimpleName();
    private HamsterListActivity view;
    private HamsterApiInteractor hamsterListInteractor;
    private Bus bus;
    private final HamsterListAdapter hamsterListAdapter;

    public HamsterListPresenter(HamsterListActivity view, HamsterApiInteractor hamsterListInteractor, Bus bus) {
        this.view = view;
        this.hamsterListInteractor = hamsterListInteractor;
        this.bus = bus;
        hamsterListAdapter =
                new HamsterListAdapter(view, new ArrayList<Hamster>());
        view.setListAdapter(hamsterListAdapter);
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }


    public void loadHamsters() {
        hamsterListInteractor.getAllHamsters();
    }

    @Subscribe
    public void onHamstersLoaded(final OnHamstersLoadedEvent e) {
        hamsterListAdapter.clear();
        hamsterListAdapter.addAll(e.getHamsters());
        view.onHamstersLoaded();
    }

}
