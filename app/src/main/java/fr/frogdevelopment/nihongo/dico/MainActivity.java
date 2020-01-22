package fr.frogdevelopment.nihongo.dico;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.frogdevelopment.nihongo.dico.ui.bottom.BottomNavigationDrawerFragment;
import fr.frogdevelopment.nihongo.dico.ui.bottom.BottomSheetSearchFragment;
import fr.frogdevelopment.nihongo.dico.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        BottomAppBar bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(v -> {
            BottomSheetSearchFragment bottomSheetSearchFragment = new BottomSheetSearchFragment();
            bottomSheetSearchFragment.show(getSupportFragmentManager(), bottomSheetSearchFragment.getTag());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
            bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}