package ch.furrylittlefriends.hamsterhelper.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.fragments.SettingsFragment;

/**
 * Created by fork on 4/24/14.
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_ENDPOINT = "endpoint";
    public static final String KEY_USE_MOCK_ENDPOINT = "useMockeEndpoint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HamsterHelperApplication hamsterHelperApplication = (HamsterHelperApplication) getApplication();
        hamsterHelperApplication.inject(this);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
