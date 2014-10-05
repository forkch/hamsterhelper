package ch.furrylittlefriends.hamsterhelper.jobs;

import android.util.Log;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class AddHamsterJob extends BaseNetworkedJob {

    private Hamster hamsterToBeAdded;

    @Inject
    transient HamsterApiInteractor hamsterApiInteractor;
    @Inject
    transient Bus bus;


    public AddHamsterJob(Hamster hamsterToBeAdded) {
        this.hamsterToBeAdded = hamsterToBeAdded;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        Hamster hamsterFromServer = hamsterApiInteractor.addHamsterSync(hamsterToBeAdded);

        new Update(Hamster.class).set("serverId", hamsterToBeAdded.getServerId()).where("Id = ?", hamsterToBeAdded.getId());

        bus.post(new HamsterAddedEvent(hamsterFromServer, true));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }

}
