package ch.furrylittlefriends.hamsterhelper.repository;

import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 12.10.14.
 */
public interface HamsterRepository {

    void getAllHamters();

    Hamster storeHamster(Hamster hamster);

    void deleteHamster(Hamster hamster);

    String saveHamsterImage(Hamster hamster, String path);
}
