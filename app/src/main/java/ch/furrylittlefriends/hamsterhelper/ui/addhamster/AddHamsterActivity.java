package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.ui.BaseActivity;
import ch.furrylittlefriends.hamsterhelper.modules.AddHamsterModule;

public class AddHamsterActivity extends BaseActivity {

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

    @InjectView(R.id.weightSeekbar)
    SeekBar weightSeekbar;

    @InjectView(R.id.weightTextView)
    TextView weightTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hamster);
        getMyApplication().createScopedGraphAndInject(this, new AddHamsterModule(this));
        ButterKnife.inject(this);

        ArrayAdapter<CharSequence> gencodesAdapter = ArrayAdapter.createFromResource(this,
                R.array.gencodeEntries, android.R.layout.simple_spinner_item);
        gencodeSpinner.setAdapter(gencodesAdapter);

        setupWeightBinding();
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
    private void setupWeightBinding() {
       weightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
    }

    @OnClick(R.id.save)
    public void saveHamster() {
        String hamsterNameText = hamsterNameTextField.getText().toString();
        boolean isMale = isMaleCheckbox.isSelected();
        int weight = weightSeekbar.getProgress();
        String gencode = (String)gencodeSpinner.getSelectedItem();

        presenter.addHamster(hamsterNameText, isMale, gencode, weight);
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
}
