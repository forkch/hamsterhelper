package ch.furrylittlefriends.hamsterhelper.queue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;

/**
 * Created by fork on 04.10.14.
 */
public class HamsterApiTaskService extends Service implements HamsterApiTask.Callback {
    private static final String TAG = "Tape:HamsterApiTaskService";

    @Inject
    HamsterApiTaskQueue queue;
    @Inject
    Bus bus;

    private boolean running;

    @Override
    public void onCreate() {
        super.onCreate();
        ((HamsterHelperApplication) getApplication()).inject(this);
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeNext();
        return START_STICKY;
    }

    private void executeNext() {
        if (running) return; // Only one task at a time.

        HamsterApiTask task = queue.peek();
        if (task != null) {
            running = true;
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Override
    public void onSuccess(final String url) {
        running = false;
        queue.remove();
        //bus.post(new HamsterApiTask(url));
        executeNext();
    }

    @Override
    public void onFailure() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
