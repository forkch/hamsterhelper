package ch.furrylittlefriends.hamsterhelper.queue;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.FileObjectQueue.Converter;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

import java.io.File;
import java.io.IOException;


/**
 * Created by fork on 04.10.14.
 */
public class HamsterApiTaskQueue extends TaskQueue<HamsterApiTask> {
    private static final String FILENAME = "image_upload_task_queue";

    private final Context context;
    private final Bus bus;

    private HamsterApiTaskQueue(ObjectQueue<HamsterApiTask> delegate, Context context, Bus bus) {
        super(delegate);
        this.context = context;
        this.bus = bus;
        bus.register(this);

        if (size() > 0) {
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, HamsterApiTaskService.class));
    }

    @Override
    public void add(HamsterApiTask entry) {
        super.add(entry);
        startService();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public static HamsterApiTaskQueue create(Context context, Gson gson, Bus bus) {
        Converter<HamsterApiTask> converter = new GsonConverter<HamsterApiTask>(gson, HamsterApiTask.class);
        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue<HamsterApiTask> delegate;
        try {
            delegate = new FileObjectQueue<HamsterApiTask>(queueFile, converter);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
        return new HamsterApiTaskQueue(delegate, context, bus);
    }
}

