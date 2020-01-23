package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import fr.frogdevelopment.nihongo.dico.to_delete.MainActivity;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static final long SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        // This method will be executed once the timer is over
        new Handler().postDelayed(this::launchActivity, SPLASH_TIME_OUT);
    }

    private void launchActivity() {
        startActivity(new Intent(this, MainActivity.class));

        // close this activity
        finish();
    }

}
