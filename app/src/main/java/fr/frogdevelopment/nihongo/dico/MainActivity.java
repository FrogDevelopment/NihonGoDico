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

    private boolean isSearching;
    private BottomAppBar mBaB;
    private FloatingActionButton mFaB;

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

        mBaB = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(mBaB);

        isSearching = true;
        mFaB = findViewById(R.id.fab_search);
        mFaB.setOnClickListener(v -> {
            BottomSheetSearchFragment bottomSheetSearchFragment = new BottomSheetSearchFragment();
            bottomSheetSearchFragment.show(getSupportFragmentManager(), bottomSheetSearchFragment.getTag());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isSearching) {
                BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
            } else {
                mFaB.hide(onVisibilityChangedListener);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private FloatingActionButton.OnVisibilityChangedListener onVisibilityChangedListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            if (isSearching) {
                switchToDetails();
            } else {
                switchToSearch();
            }
            fab.show();
        }
    };

    private void switchToDetails() {
        // Hide navigation drawer icon
        mBaB.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        // Move FAB from the center of BottomAppBar to the end of it
        mBaB.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

        // Change FAB icon
        mFaB.setImageResource(R.drawable.ic_baseline_favorite_24);

        isSearching = false;
    }

    private void switchToSearch() {
        // Show navigation drawer icon
        mBaB.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        // Move FAB from the center of BottomAppBar to the end of it
        mBaB.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

        // Change FAB icon
        mFaB.setImageResource(R.drawable.ic_baseline_search_24);

        isSearching = true;
    }
}