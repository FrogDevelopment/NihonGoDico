package fr.frogdevelopment.nihongo.dico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.ui.search.SearchFragment;
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsActivity;

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
        switch (item.getItemId()) {
            case R.id.nav_settings:
                hideFaB();
                startActivity(new Intent(requireContext(), SettingsActivity.class));
                break;

            case R.id.nav_favorites:
                hideFaB();
                Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                break;

            default:
                showFaB();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, SearchFragment.newInstance())
                        .commitNow();
                break;
        }

        dismiss();
        return true;
    }

    private void hideFaB() {
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.hide();
    }

    private void showFaB() {
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.show();
    }
}
