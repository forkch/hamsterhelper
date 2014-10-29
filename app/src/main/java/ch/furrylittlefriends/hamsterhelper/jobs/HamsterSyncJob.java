package ch.furrylittlefriends.hamsterhelper.jobs;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.repository.HamsterCloudRepository;

/**
 * Created with love by fork on 04.10.14.
 */
public class HamsterSyncJob extends BaseNetworkedJob {

    @Inject
    HamsterCloudRepository hamsterApiInteractor;

    public HamsterSyncJob() {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
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
