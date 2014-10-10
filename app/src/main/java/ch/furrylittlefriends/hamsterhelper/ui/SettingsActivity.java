package ch.furrylittlefriends.hamsterhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;

import com.tundem.aboutlibraries.Libs;
import com.tundem.aboutlibraries.ui.LibsActivity;

import ch.furrylittlefriends.hamsterhelper.HamsterHelperApplication;
import ch.furrylittlefriends.hamsterhelper.R;

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
        //getFragmentManager().beginTransaction()
        //        .replace(android.R.id.content, new SettingsFragment())
        //       .commit();

        addPreferencesFromResource(R.xml.pref_general);
        setContentView(R.layout.activity_settings);

        Button button = (Button) findViewById(R.id.libraries);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an intent with context and the Activity class
                Intent i = new Intent(getApplicationContext(), LibsActivity.class);
                i.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));
                i.putExtra(Libs.BUNDLE_LIBS, new String[]{});

                i.putExtra(Libs.BUNDLE_VERSION, true);
                i.putExtra(Libs.BUNDLE_LICENSE, true);

                i.putExtra(Libs.BUNDLE_TITLE, "Open Source");

                i.putExtra(Libs.BUNDLE_THEME, R.style.AppTheme);
                startActivity(i);

            }
        });
    }
}
