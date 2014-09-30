package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.modules.AddHamsterModule;
import ch.furrylittlefriends.hamsterhelper.ui.BaseFragmentActivity;

public class AddHamsterActivity extends BaseFragmentActivity implements DatePickerDialog.OnDateSetListener, NumberPickerDialogFragment.NumberPickerDialogHandler{

    private static final String TAG = AddHamsterActivity.class.getSimpleName();
    @Inject
    AddHamsterPresenter presenter;

    @InjectView(R.id.hamsterImage)
    ImageView hamsterImage;

    @InjectView(R.id.hamsterName)
    EditText hamsterNameTextField;

    @InjectView(R.id.isMale)
    CheckBox isMaleCheckbox;

    @InjectView(R.id.gencodeSpinner)
    Spinner gencodeSpinner;

    @InjectView(R.id.birthdaySpinner)
    TextView birthdaySpinner;

    @InjectView(R.id.weightTextView)
    TextView weightTextview;
    private DatePickerDialog datePickerDialog;
    private DateTime selectedBirthday;
    private double weight = 0;
    private DateTimeFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hamster);
        getMyApplication().createScopedGraphAndInject(this, new AddHamsterModule(this));
        ButterKnife.inject(this);

        ArrayAdapter<CharSequence> gencodesAdapter = ArrayAdapter.createFromResource(this,
                R.array.gencodeEntries, android.R.layout.simple_spinner_item);
        gencodeSpinner.setAdapter(gencodesAdapter);
        formatter = DateTimeFormat.forPattern(getString(R.string.birthday_date_format));

        selectedBirthday = DateTime.now();
        setBirthdayText(selectedBirthday);
    }

    @OnClick(R.id.birthdaySpinner)
    public void onBirthdayTouch() {
            datePickerDialog = DatePickerDialog.newInstance(this, selectedBirthday.getYear(), selectedBirthday.getMonthOfYear(), selectedBirthday.getDayOfMonth(), true);
            datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.setCloseOnSingleTapDay(true);
            datePickerDialog.show(getSupportFragmentManager(), TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStop() {
        presenter.onPause();
        super.onStop();
    }


    @OnClick(R.id.weightTextView)
    public void onWeightClick() {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setLabelText(getString(R.string.weight_unit));
        npb.show();
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        String hamsterNameText = hamsterNameTextField.getText().toString();
        boolean isMale = isMaleCheckbox.isSelected();
        String gencode = (String) gencodeSpinner.getSelectedItem();

        presenter.addHamster(hamsterNameText, isMale, gencode, weight, selectedBirthday);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick(){
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_hamster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onHamsterAdded() {
        Toast.makeText(this, "Hamster added", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        selectedBirthday = new DateTime(year, month, day, 0,0);
        setBirthdayText(selectedBirthday);
    }

    private void setBirthdayText(DateTime date) {
        birthdaySpinner.setText(getString(R.string.birthday_label) + " " + date.toString(formatter));
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        this.weight = fullNumber;
        setWeightText(weight);
    }

    private void setWeightText(double weight) {
        weightTextview.setText(getString(R.string.weight_label) + " " + weight + getString(R.string.weight_unit));
    }

    public void validateNameFailed() {
        hamsterNameTextField.setError(getString(R.string.error_hamstername));
    }
}
