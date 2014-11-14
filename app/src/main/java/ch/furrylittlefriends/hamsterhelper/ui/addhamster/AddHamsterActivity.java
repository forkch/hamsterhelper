package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.content.Intent;
import android.net.Uri;
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

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.modules.AddHamsterModule;
import ch.furrylittlefriends.hamsterhelper.ui.base.BaseFragmentActivity;
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

    @InjectView(R.id.motherSpinner)
    Spinner motherSpinner;

    @InjectView(R.id.fatherSpinner)
    Spinner fatherSpinner;

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

        setHamsterImage(null);

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


    @OnClick(R.id.addPicture)
    public void onAddPictureClick() {
        presenter.selectPicture();

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

    public void addMothers(SortedSet<Hamster> hamsters) {
        motherSpinner.setAdapter(new HamsterSpinnerAdapter(this, getHamstersByGender(new ArrayList<Hamster>(hamsters), false)));
    }


    public void addFathers(List<Hamster> hamsters) {
        fatherSpinner.setAdapter(new HamsterSpinnerAdapter(this, getHamstersByGender(hamsters, true)));
    }

    private List<Hamster> getHamstersByGender(List<Hamster> hamsters, boolean males) {
        List<Hamster> filteredHamsters = new ArrayList<Hamster>();
        for (Hamster hamster : hamsters) {
            if (hamster.isMale() == males) {
                filteredHamsters.add(hamster);
            }
        }
        return filteredHamsters;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.processResult(requestCode, resultCode, data);
    }

    public void setHamsterImage(Uri mImageCaptureUri) {
        if (mImageCaptureUri == null) {
            Picasso.with(this).load(R.drawable.hamster_image).fit().centerCrop().into(hamsterImage);
        } else {
            Picasso.with(this).load(mImageCaptureUri).fit().centerCrop().into(hamsterImage);
        }
    }

}
