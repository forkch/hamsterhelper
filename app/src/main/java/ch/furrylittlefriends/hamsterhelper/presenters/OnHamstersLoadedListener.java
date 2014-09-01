package ch.furrylittlefriends.hamsterhelper.presenters;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 01.09.14.
 */
public interface OnHamstersLoadedListener {

    public void onHamstersLoaded(List<Hamster> hamsters);
}
