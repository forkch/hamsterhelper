package ch.furrylittlefriends.hamsterhelper.queue;

import com.squareup.tape.Task;

/**
 * Created by fork on 04.10.14.
 */
public class HamsterApiTask  implements Task<HamsterApiTask.Callback> {


    @Override
    public void execute(Callback callback) {

    }

    public interface Callback {
        void onSuccess(String url);
        void onFailure();
    }

}
