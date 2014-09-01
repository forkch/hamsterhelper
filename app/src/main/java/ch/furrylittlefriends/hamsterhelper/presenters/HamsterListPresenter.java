package ch.furrylittlefriends.hamsterhelper.presenters;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.activities.HamsterListActivity;
import ch.furrylittlefriends.hamsterhelper.adapters.HamsterListAdapter;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterListInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 01.09.14.
 */
public class HamsterListPresenter {
    private HamsterListActivity view;
    private HamsterListInteractor hamsterListInteractor;
    private Bus bus;

    public HamsterListPresenter(HamsterListActivity view, HamsterListInteractor hamsterListInteractor, Bus bus) {
        this.view = view;
        this.hamsterListInteractor = hamsterListInteractor;
        this.bus = bus;
        this.bus.register(this);
    }


    public void loadHamsters() {
        hamsterListInteractor.getAllHamsters(new OnHamstersLoadedListener() {

            @Override
            public void onHamstersLoaded(List<Hamster> hamsters) {
                view.setListAdapter(new HamsterListAdapter(view, hamsters));
            }
        });

    }

}
