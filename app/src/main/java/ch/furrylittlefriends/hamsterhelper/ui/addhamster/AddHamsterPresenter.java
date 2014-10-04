package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.os.Bundle;
import android.view.View;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.path.android.jobqueue.JobManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.events.HamsterAddedEvent;
import ch.furrylittlefriends.hamsterhelper.events.OnHamstersLoadedEvent;
import ch.furrylittlefriends.hamsterhelper.interactors.HamsterApiInteractor;
import ch.furrylittlefriends.hamsterhelper.jobs.AddHamsterJob;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by fork on 25.09.14.
 */
public class AddHamsterPresenter implements DatePickerDialog.OnDateSetListener, NumberPickerDialogFragment.NumberPickerDialogHandler {
    private static final String TAG = AddHamsterPresenter.class.getSimpleName();
    public static final String VAL_BIRTHDAY = "VAL_BIRTHDAY";
    private AddHamsterActivity view;
    private HamsterApiInteractor hamsterApiInteractor;
    private Bus bus;
    private final JobManager jobManager;

    @Icicle
    DateTime selectedBirthday;
    @Icicle
    double weight = 0;

    private DateTimeFormatter formatter;

    public AddHamsterPresenter(AddHamsterActivity view, HamsterApiInteractor hamsterApiInteractor1, Bus bus, JobManager jobManager) {
        this.view = view;
        this.hamsterApiInteractor = hamsterApiInteractor1;
        this.bus = bus;
        this.jobManager = jobManager;
        formatter = DateTimeFormat.forPattern(view.getString(R.string.birthday_date_format));
    }

    public void onResume() {
        bus.register(this);
        view.setBirthdayText(selectedBirthday);
        view.setWeightText(weight);
        hamsterApiInteractor.getAllHamsters();

    }

    public void onPause() {
        bus.unregister(this);
    }

    @Subscribe
    public void onHamsterAdded(HamsterAddedEvent e) {

        view.onHamsterAdded();
    }

    @Subscribe
    public void onHamstersLoaded(OnHamstersLoadedEvent e) {
view.addMothers(e.getHamsters());
    }

    public void addHamster() {
        String hamsterName = view.getHamsterName();
        boolean isMale = view.isMale();
        String gencode = view.getGencode();

        if (StringUtils.isBlank(hamsterName)) {
            view.validateNameFailed();
            return;
        }
        if (selectedBirthday == null) {
            view.validateBirthdayFailed();
            return;
        }
        Hamster hamster = new Hamster();
        hamster.setName(hamsterName);
        hamster.setMale(isMale);
        hamster.setWeight(weight);
        hamster.setGencode(gencode);
        hamster.setBirthday(selectedBirthday);
        //hamsterApiInteractor.addHamster(hamster);
        jobManager.addJobInBackground(new AddHamsterJob(hamster));
    }

    public void showWeightPicker() {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(view.getSupportFragmentManager())
                .addNumberPickerDialogHandler(this)
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setLabelText(view.getString(R.string.weight_unit));
        npb.show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        selectedBirthday = new DateTime(year, month, day, 0, 0);
        view.setBirthdayText(selectedBirthday);
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        this.weight = fullNumber;
        view.setWeightText(weight);
    }

    public void showBirtdayPicker() {
        if (selectedBirthday == null) {
            selectedBirthday = DateTime.now();
        }
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, selectedBirthday.getYear(), selectedBirthday.getMonthOfYear(), selectedBirthday.getDayOfMonth(), true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(view.getSupportFragmentManager(), TAG);
    }

    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
    }
}
