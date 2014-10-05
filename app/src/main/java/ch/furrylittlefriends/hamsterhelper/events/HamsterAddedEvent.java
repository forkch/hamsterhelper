package ch.furrylittlefriends.hamsterhelper.events;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 27.09.14.
 */
public class HamsterAddedEvent {
    private Hamster hamster;
    private final boolean syncedWithServer;

    public HamsterAddedEvent(Hamster hamster, boolean syncedWithServer) {
        this.hamster = hamster;
        this.syncedWithServer = syncedWithServer;
    }

    public boolean isSyncedWithServer() {
        return syncedWithServer;
    }

    public Hamster getHamster() {
        return hamster;
    }
}
