package fr.frogdevelopment.nihongo.dico.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsActivityBinding binding = SettingsActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.settings_container, SettingsFragment.newInstance())
                    .commitNow();
        }

        setSupportActionBar(binding.bottomAppBar);
    }
}