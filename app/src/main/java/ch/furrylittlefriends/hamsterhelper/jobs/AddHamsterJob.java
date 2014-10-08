package ch.furrylittlefriends.hamsterhelper.jobs;

import com.activeandroid.query.Update;
import com.squareup.otto.Bus;

import java.io.FileDescriptor;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class AddHamsterJob extends BaseNetworkedJob {

    private Hamster hamsterToBeAdded;
    private final byte[] bitmap;
    private final String fileDescriptor;

    @Inject
    transient HamsterApiInteractor hamsterApiInteractor;
    @Inject
    transient Bus bus;


    public AddHamsterJob(Hamster hamsterToBeAdded, byte[] bitmap, String path) {
        this.hamsterToBeAdded = hamsterToBeAdded;
        this.bitmap = bitmap;
        this.fileDescriptor = path;
    }

    @Override
    public void onAdded() {
        bus.post(new HamsterAddedEvent(hamsterToBeAdded, false));
    }

    @Override
    public void onRun() throws Throwable {
        Hamster hamsterFromServer = hamsterApiInteractor.addHamsterSync(hamsterToBeAdded);

        new Update(Hamster.class).set("serverId=", hamsterFromServer.getServerId()).where("Id = ?", hamsterToBeAdded.getId());
        /*if (bitmap != null) {
            String imageName = hamsterApiInteractor.uploadImage(hamsterFromServer, bitmap);
            new Update(Hamster.class).set("image=", imageName).where("Id = ?", hamsterToBeAdded.getId());
        }*/
        if (fileDescriptor != null) {
            String imageName = hamsterApiInteractor.uploadImage(hamsterFromServer, fileDescriptor);
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
