package ch.furrylittlefriends.hamsterhelper.jobs;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class AddHamsterJob extends BaseNetworkedJob {

    private final Hamster hamsterToBeAdded;
    @Inject
    HamsterApiInteractor hamsterApiInteractor;
    @Inject
    Bus bus;


    public AddHamsterJob(Hamster hamsterToBeAdded) {
        this.hamsterToBeAdded = hamsterToBeAdded;
    }

    @Override
    public void onAdded() {
        hamsterToBeAdded.save();
        bus.post(new HamsterAddedEvent(hamsterToBeAdded));

    }

    @Override
    public void onRun() throws Throwable {
        hamsterApiInteractor.addHamsterSync(hamsterToBeAdded);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }
}