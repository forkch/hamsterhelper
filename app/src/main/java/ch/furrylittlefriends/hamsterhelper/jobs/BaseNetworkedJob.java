package ch.furrylittlefriends.hamsterhelper.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * Created with love by fork on 04.10.14.
 */
public abstract class BaseNetworkedJob extends Job {
    public BaseNetworkedJob() {
        super(new Params(1).requireNetwork().persist());
    }
}
