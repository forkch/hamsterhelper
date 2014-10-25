package ch.furrylittlefriends.hamsterhelper.jobs;

import com.activeandroid.query.Update;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class AddHamsterJob extends BaseNetworkedJob {

    private Hamster hamsterToBeAdded;
    private final String fileDescriptor;

    @Inject
    transient HamsterCloudRepository hamsterApiInteractor;
    @Inject
    transient Bus bus;


    public AddHamsterJob(Hamster hamsterToBeAdded, String path) {
        this.hamsterToBeAdded = hamsterToBeAdded;
        this.fileDescriptor = path;
    }
    
    @Override
    public void onAdded() {
        bus.post(new HamsterAddedEvent(hamsterToBeAdded, false));
    }

    @Override
    public void onRun() throws Throwable {
        Hamster hamsterFromServer = hamsterApiInteractor.storeHamster(hamsterToBeAdded);

        new Update(Hamster.class).set("serverId=", hamsterFromServer.getServerId()).where("Id = ?", hamsterToBeAdded.getId());
        if (fileDescriptor != null) {
            String imageName = hamsterApiInteractor.saveHamsterImage(hamsterFromServer, fileDescriptor);
            new Update(Hamster.class).set("image=", imageName).where("Id = ?", hamsterToBeAdded.getId());
        }
        bus.post(new HamsterAddedEvent(hamsterFromServer, true));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}
