package ch.furrylittlefriends.hamsterhelper.jobs;

import android.net.Uri;

import com.activeandroid.query.Update;
import com.squareup.otto.Bus;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.util.FileUtils;

/**
 * Created by fork on 04.10.14.
 */
public class AddHamsterJob extends BaseNetworkedJob {

    private Hamster hamsterToBeAdded;
    private final String imageFilePath;

    @Inject
    transient HamsterApiInteractor hamsterApiInteractor;
    @Inject
    transient Bus bus;


    public AddHamsterJob(Hamster hamsterToBeAdded, String imageFilePath) {
        this.hamsterToBeAdded = hamsterToBeAdded;
        this.imageFilePath = imageFilePath;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        Hamster hamsterFromServer = hamsterApiInteractor.addHamsterSync(hamsterToBeAdded);

        new Update(Hamster.class).set("serverId=", hamsterFromServer.getServerId()).where("Id = ?", hamsterToBeAdded.getId());
        if (StringUtils.isNotBlank(imageFilePath)) {
            String imageName = hamsterApiInteractor.uploadImage(hamsterFromServer, new File(imageFilePath));
            new Update(Hamster.class).set("image=", imageName).where("Id = ?", hamsterToBeAdded.getId());
        }

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
