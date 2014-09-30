package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.modules.AddHamsterModule;
import ch.furrylittlefriends.hamsterhelper.ui.BaseFragmentActivity;
import icepick.Icepick;

public class AddHamsterActivity extends BaseFragmentActivity {

    private static final String TAG = AddHamsterActivity.class.getSimpleName();
    public static final String VAL_HAMSTER_NAME = "VAL_HAMSTER_NAME";
    private static final String VAL_IS_MALE = "VAL_IS_MALE";
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

    private DateTimeFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_hamster);
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.color.add_hamster_picture_button_color)
                .headerLayout(R.layout.add_hamster_header)
                .contentLayout(R.layout.activity_add_hamster)
                .headerOverlayLayout(R.layout.add_picture_overlay);
        setContentView(helper.createView(this));
        helper.initActionBar(this);


        getMyApplication().createScopedGraphAndInject(this, new AddHamsterModule(this));
        ButterKnife.inject(this);

        ArrayAdapter<CharSequence> gencodesAdapter = ArrayAdapter.createFromResource(this,
                R.array.gencodeEntries, android.R.layout.simple_spinner_item);
        gencodeSpinner.setAdapter(gencodesAdapter);

        formatter = DateTimeFormat.forPattern(getString(R.string.birthday_date_format));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_hamster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.birthdaySpinner)
    public void onBirthdayTouch() {
        presenter.showBirtdayPicker();
    }

    @OnClick(R.id.weightTextView)
    public void onWeightClick() {
        presenter.showWeightPicker();
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        presenter.addHamster();
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        finish();
    }


    public void onHamsterAdded() {
        Toast.makeText(this, getString(R.string.message_hamster_added), Toast.LENGTH_SHORT).show();
        finish();
    }

    void setBirthdayText(DateTime date) {
        if (date == null) {
            birthdaySpinner.setText("");
        } else {
            birthdaySpinner.setText(date.toString(formatter));
        }
    }

    void setWeightText(double weight) {
        weightTextview.setText(" " + weight + getString(R.string.weight_unit));
    }

    public void validateNameFailed() {
        hamsterNameTextField.setError(getString(R.string.error_hamstername));
    }

    public String getGencode() {
        return gencodeSpinner.getSelectedItem().toString();
    }

    public boolean isMale() {
        return isMaleCheckbox.isActivated();
    }

    public String getHamsterName() {
        return hamsterNameTextField.getText().toString();
    }

    public void validateBirthdayFailed() {
        birthdaySpinner.setError(getString(R.string.error_birthday));
    }
}
