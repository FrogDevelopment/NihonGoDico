package fr.frogdevelopment.nihongo.dico.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding;
import fr.frogdevelopment.nihongo.dico.ui.main.MainViewModel;

public class DetailsFragment extends Fragment {

    private MainViewModel mViewModel;
    private DetailsFragmentBinding mBinding;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    private DetailsFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(getLayoutInflater());

        mBinding.kanji.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_mincho));
        mBinding.kana.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_gothic));

        mViewModel.entryDetails().observe(requireActivity(), entryDetails -> mBinding.setDetails(entryDetails));

        return mBinding.getRoot();
    }
}
