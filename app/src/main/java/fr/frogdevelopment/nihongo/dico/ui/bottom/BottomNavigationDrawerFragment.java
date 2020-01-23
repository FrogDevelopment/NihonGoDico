package fr.frogdevelopment.nihongo.dico.ui.bottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.ui.details.DetailsFragment;
import fr.frogdevelopment.nihongo.dico.ui.main.MainFragment;
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_favorites:
                hideFaB();
                fragment = new DetailsFragment(); // fixme just for test
                break;
            case R.id.nav_settings:
                hideFaB();
                fragment = new SettingsFragment();
                break;

            default:
                showFaB();
                fragment = MainFragment.newInstance();
                break;
        }

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();

        dismiss();
        return true;
    }

    private void hideFaB() {
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab_search);
        fab.hide();
    }

    private void showFaB() {
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab_search);
        fab.show();
    }
}
