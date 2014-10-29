package ch.furrylittlefriends.hamsterhelper.events;

import java.util.SortedSet;
import java.util.TreeSet;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created with love by fork on 27.09.14.
 */
public class OnHamstersLoadedEvent {
    private final SortedSet<Hamster> hamsters;

    public OnHamstersLoadedEvent(TreeSet<Hamster> hamsters) {
        this.hamsters = hamsters;
    }

    public SortedSet<Hamster> getHamsters() {
        return hamsters;
    }
}
