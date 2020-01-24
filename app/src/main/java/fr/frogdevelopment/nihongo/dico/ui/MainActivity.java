package fr.frogdevelopment.nihongo.dico.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.MainActivityBinding;
import fr.frogdevelopment.nihongo.dico.ui.bottom.BottomNavigationDrawerFragment;
import fr.frogdevelopment.nihongo.dico.ui.bottom.BottomSheetSearchFragment;
import fr.frogdevelopment.nihongo.dico.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private boolean isSearching;
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainActivityBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commit();
            getSupportFragmentManager()
                    .addOnBackStackChangedListener(this::switchFaB);
        }

        setSupportActionBar(mBinding.bottomAppBar);

        isSearching = true;
        mBinding.fab.setOnClickListener(v -> {
            BottomSheetSearchFragment bottomSheetSearchFragment = new BottomSheetSearchFragment();
            bottomSheetSearchFragment.show(getSupportFragmentManager(), bottomSheetSearchFragment.getTag());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isSearching) {
                BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
            } else {
                switchFaB();
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

    private void switchFaB() {
        mBinding.fab.hide(onVisibilityChangedListener);
    }

    private void switchToDetails() {
        // Hide navigation drawer icon
        mBinding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        // Move FAB from the center of BottomAppBar to the end of it
        mBinding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

        // Change FAB icon
        mBinding.fab.setImageResource(R.drawable.ic_speak);

        isSearching = false;
    }

    private void switchToSearch() {
        // Show navigation drawer icon
        mBinding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        // Move FAB from the center of BottomAppBar to the end of it
        mBinding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

        // Change FAB icon
        mBinding.fab.setImageResource(R.drawable.ic_baseline_search_24);

        isSearching = true;

        getSupportFragmentManager().popBackStackImmediate();
    }
}