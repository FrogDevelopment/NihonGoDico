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
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel;
import fr.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding;

public class DetailsFragment extends Fragment {

    private DetailsFragmentBinding mBinding;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(getLayoutInflater());

        mBinding.kanji.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_mincho));
        mBinding.kana.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_gothic));

        DetailsViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        mBinding.setViewModel(viewModel);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
