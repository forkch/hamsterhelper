package ch.furrylittlefriends.hamsterhelper.events;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 27.09.14.
 */
public class OnHamstersLoadedEvent {
    private final List<Hamster> hamsters;

    public OnHamstersLoadedEvent(List<Hamster> hamsters) {
        this.hamsters = hamsters;
    }

    public List<Hamster> getHamsters() {
        return hamsters;
    }
}
