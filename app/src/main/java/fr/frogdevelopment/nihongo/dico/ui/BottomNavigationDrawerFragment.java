package fr.frogdevelopment.nihongo.dico.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.BottomsheetFragmentBinding;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    private final OnNavigateToListener onNavigateToListener;
    private BottomsheetFragmentBinding mBinding;

    public BottomNavigationDrawerFragment(OnNavigateToListener onNavigateToListener) {
        super();
        this.onNavigateToListener = onNavigateToListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NavigationBottomSheet);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomsheetFragmentBinding.inflate(getLayoutInflater());
        mBinding.navigationView.setNavigationItemSelectedListener(this);

        return mBinding.getRoot();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (onNavigateToListener != null) {
            onNavigateToListener.navigateTo(item.getItemId());
        }

        dismiss();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    interface OnNavigateToListener {

        void navigateTo(int itemId);
    }

}
