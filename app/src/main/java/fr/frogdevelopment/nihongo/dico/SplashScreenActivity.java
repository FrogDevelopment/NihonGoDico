/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import fr.frogdevelopment.nihongo.dico.downloads.DownloadsActivity;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static final long SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // This method will be executed once the timer is over
        new Handler().postDelayed(this::launchActivity, SPLASH_TIME_OUT);
    }

    private void launchActivity() {
        Intent intent;
        boolean data_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("data_saved", false);
        if (data_saved) {
            intent = new Intent(this, MainActivity.class);
        }else {
            intent = new Intent(this, DownloadsActivity.class);
        }
        startActivity(intent);

        // close this activity
        finish();
    }

}
