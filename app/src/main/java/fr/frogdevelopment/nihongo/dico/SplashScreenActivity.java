/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

	// Splash screen timer
	private static final long SPLASH_TIME_OUT = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		// This method will be executed once the timer is over
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SplashScreenActivity.this.launchActivity();
			}
		}, SPLASH_TIME_OUT);
	}

	private void launchActivity() {
		startActivity(new Intent(this, MainActivity.class));

		// close this activity
		finish();
	}

}
