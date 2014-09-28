package ch.furrylittlefriends.hamsterhelper.events;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 27.09.14.
 */
public class HamsterAddedEvent {
    private Hamster hamster;

    public HamsterAddedEvent(Hamster hamster) {
        this.hamster = hamster;
    }

    public Hamster getHamster() {
        return hamster;
    }
}
