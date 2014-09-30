package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 25.09.14.
 */
public class AddHamsterPresenter {
    private AddHamsterActivity view;
    private HamsterApiInteractor hamsterListInteractor;
    private Bus bus;

    public AddHamsterPresenter(AddHamsterActivity view, HamsterApiInteractor hamsterListInteractor, Bus bus) {
        this.view = view;
        this.hamsterListInteractor = hamsterListInteractor;
        this.bus = bus;
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    @Subscribe
    public void onHamsterAdded(HamsterAddedEvent e) {

        view.onHamsterAdded();
    }

    public void addHamster(String hamsterName, boolean isMale, String gencode, double weight, DateTime selectedBirthday) {
        if (StringUtils.isBlank(hamsterName)) {
            view.validateNameFailed();
            return;
        }
        Hamster hamster = new Hamster();
        hamster.setName(hamsterName);
        hamster.setMale(isMale);
        hamster.setWeight(weight);
        hamster.setGencode(gencode);
        hamster.setBirthday(selectedBirthday);
        hamsterListInteractor.addHamster(hamster);
    }
}
