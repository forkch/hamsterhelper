package ch.furrylittlefriends.hamsterhelper.jobs;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterDeletedEvent;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class DeleteHamsterJob extends BaseNetworkedJob {

    private final Hamster hamsterToBeDeleted;
    @Inject
    HamsterCloudRepository hamsterApiInteractor;
    @Inject
    Bus bus;

    public DeleteHamsterJob(Hamster hamsterToBeDeleted) {
        this.hamsterToBeDeleted = hamsterToBeDeleted;
    }

    @Override
    public void onAdded() {

        hamsterToBeDeleted.delete();
        bus.post(new HamsterDeletedEvent(hamsterToBeDeleted));

    }

    @Override
    public void onRun() throws Throwable {
        hamsterApiInteractor.deleteHamster(hamsterToBeDeleted);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }
}
