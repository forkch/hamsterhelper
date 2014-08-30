package ch.furrylittlefriends.hamsterhelper.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.adapters.HamsterListAdapter;
import ch.furrylittlefriends.hamsterhelper.fragments.SettingsFragment;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.services.HamsterService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HamsterListActivity extends BaseListActivity {

    public static final String TAG = HamsterListActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamster_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String endpoint = preferences.getString(SettingsActivity.KEY_ENDPOINT, "http://10.0.2.2:9000");

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();

        HamsterService hamsterService = restAdapter.create(HamsterService.class);

        hamsterService.getAllHamsters(new Callback<List<Hamster>>() {
            @Override
            public void success(List<Hamster> hamsters, Response response) {
                        setListAdapter(new HamsterListAdapter(HamsterListActivity.this, hamsters));
            }

            @Override
            public void failure(RetrofitError error) {
                        Log.e(TAG, error.getMessage());
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamster_list, menu);
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


}
