package fr.frogdevelopment.nihongo.dico.ui.details;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel;
import fr.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailsFragment extends Fragment {

    private DetailsFragmentBinding mBinding;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(getLayoutInflater());

        DetailsViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        mBinding.setViewModel(viewModel);

        mBinding.kanji.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_mincho));
        mBinding.kana.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_gothic));

        if (viewModel.entryDetails().pos.isEmpty()) {
            mBinding.posTitle.setVisibility(GONE);
            mBinding.posValue.setVisibility(GONE);
        } else {
            mBinding.posTitle.setVisibility(VISIBLE);
            mBinding.posValue.setVisibility(VISIBLE);
            mBinding.posValue.setText(toString("pos_", viewModel.entryDetails().pos));
        }

        if (viewModel.entryDetails().field.isEmpty()) {
            mBinding.fieldTitle.setVisibility(GONE);
            mBinding.fieldValue.setVisibility(GONE);
        } else {
            mBinding.fieldTitle.setVisibility(VISIBLE);
            mBinding.fieldValue.setVisibility(VISIBLE);
            mBinding.fieldValue.setText(toString("field_", viewModel.entryDetails().field));
        }

        if (viewModel.entryDetails().misc.isEmpty()) {
            mBinding.miscTitle.setVisibility(GONE);
            mBinding.miscValue.setVisibility(GONE);
        } else {
            mBinding.miscTitle.setVisibility(VISIBLE);
            mBinding.miscValue.setVisibility(VISIBLE);
            mBinding.miscValue.setText(toString("misc_", viewModel.entryDetails().misc));
        }

        if (viewModel.entryDetails().dial.isEmpty()) {
            mBinding.dialTitle.setVisibility(GONE);
            mBinding.dialValue.setVisibility(GONE);
        } else {
            mBinding.dialTitle.setVisibility(VISIBLE);
            mBinding.dialValue.setVisibility(VISIBLE);
            mBinding.dialValue.setText(toString("dial_", viewModel.entryDetails().dial));
        }

        if (viewModel.entryDetails().info == null) {
            mBinding.infoTitle.setVisibility(GONE);
            mBinding.infoValue.setVisibility(GONE);
        } else {
            mBinding.infoTitle.setVisibility(VISIBLE);
            mBinding.infoValue.setVisibility(VISIBLE);
            mBinding.infoValue.setText(getStringResourceByName("info_", viewModel.entryDetails().info));
        }

        return mBinding.getRoot();
    }

    private String toString(String prefix, Set<String> values) {
        List<String> resources = new ArrayList<>(values.size());
        for (String value : values) {
            resources.add(getStringResourceByName(prefix, value));
        }

        return TextUtils.join(", ", resources);
    }

    private String getStringResourceByName(@NonNull String prefix, @NonNull String value) {
        String packageName = requireActivity().getPackageName();
        String resourceName = prefix + value.replaceAll("-", "_");
        int resId = getResources().getIdentifier(resourceName, "string", packageName);

        try {
            return getString(resId);
        } catch (Resources.NotFoundException e) {
            return value;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
