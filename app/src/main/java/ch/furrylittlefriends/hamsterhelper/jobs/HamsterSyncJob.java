package ch.furrylittlefriends.hamsterhelper.jobs;

import com.activeandroid.query.Delete;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 04.10.14.
 */
public class HamsterSyncJob extends BaseNetworkedJob {

    @Inject
    HamsterApiInteractor hamsterApiInteractor;

    public HamsterSyncJob() {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        new Delete().from(Hamster.class).execute();
        hamsterApiInteractor.sync();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
