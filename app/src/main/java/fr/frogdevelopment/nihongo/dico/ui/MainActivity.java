package fr.frogdevelopment.nihongo.dico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.MainActivityBinding;
import fr.frogdevelopment.nihongo.dico.ui.search.BottomSheetSearchFragment;
import fr.frogdevelopment.nihongo.dico.ui.search.SearchFragment;
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isSearching;
    private MainActivityBinding mBinding;
    private BottomNavigationDrawerFragment bottomNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainActivityBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, SearchFragment.newInstance())
                    .commit();
            getSupportFragmentManager()
                    .addOnBackStackChangedListener(this::switchFaB);
        }

        setSupportActionBar(mBinding.bottomAppBar);

        isSearching = true;
        mBinding.fab.setOnClickListener(v -> {
            if (isSearching) {
                BottomSheetSearchFragment bottomSheetSearchFragment = new BottomSheetSearchFragment();
                bottomSheetSearchFragment.show(getSupportFragmentManager(), bottomSheetSearchFragment.getTag());
            }
        });

        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment(this::navigateTo);
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
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.getTag());
            } else {
                switchFaB();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateTo(int itemId) {
        switch (itemId) {
            case R.id.nav_settings:
                mBinding.fab.hide();
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.nav_favorites:
                mBinding.fab.hide();
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;

            default:
                mBinding.fab.show();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, SearchFragment.newInstance())
                        .commitNow();
                break;
        }
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
        mBinding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mBinding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        mBinding.fab.setImageResource(R.drawable.ic_speak);

        isSearching = false;
    }

    private void switchToSearch() {
        mBinding.bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        mBinding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        mBinding.fab.setImageResource(R.drawable.ic_baseline_search_24);

        isSearching = true;
        getSupportFragmentManager().popBackStackImmediate();
    }
}