package ch.furrylittlefriends.hamsterhelper.events;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 30.09.14.
 */
public class HamsterDeletedEvent {
    private final Hamster hamster;

    public HamsterDeletedEvent(Hamster hamster) {
        this.hamster = hamster;
    }

    public Hamster getHamster() {
        return hamster;
    }
}
