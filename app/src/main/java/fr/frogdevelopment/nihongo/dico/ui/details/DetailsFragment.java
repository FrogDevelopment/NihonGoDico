package fr.frogdevelopment.nihongo.dico.ui.details;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel;
import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails;
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence;
import fr.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.KEY_LANGUAGE;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.KEY_OFFLINE;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.LANGUAGE_DEFAULT;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.OFFLINE_DEFAULT;
import static java.net.HttpURLConnection.HTTP_OK;

public class DetailsFragment extends Fragment {

    private DetailsFragmentBinding mBinding;
    private DetailsViewModel mViewModel;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(getLayoutInflater());

        mViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        mBinding.setViewModel(mViewModel);

        mBinding.kanji.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_mincho));
        mBinding.kana.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.sawarabi_gothic));

        if (mViewModel.entryDetails().pos.isEmpty()) {
            mBinding.posTitle.setVisibility(GONE);
            mBinding.posValue.setVisibility(GONE);
        } else {
            mBinding.posTitle.setVisibility(VISIBLE);
            mBinding.posValue.setVisibility(VISIBLE);
            mBinding.posValue.setText(toString("pos_", mViewModel.entryDetails().pos));
        }

        if (mViewModel.entryDetails().field.isEmpty()) {
            mBinding.fieldTitle.setVisibility(GONE);
            mBinding.fieldValue.setVisibility(GONE);
        } else {
            mBinding.fieldTitle.setVisibility(VISIBLE);
            mBinding.fieldValue.setVisibility(VISIBLE);
            mBinding.fieldValue.setText(toString("field_", mViewModel.entryDetails().field));
        }

        if (mViewModel.entryDetails().misc.isEmpty()) {
            mBinding.miscTitle.setVisibility(GONE);
            mBinding.miscValue.setVisibility(GONE);
        } else {
            mBinding.miscTitle.setVisibility(VISIBLE);
            mBinding.miscValue.setVisibility(VISIBLE);
            mBinding.miscValue.setText(toString("misc_", mViewModel.entryDetails().misc));
        }

        if (mViewModel.entryDetails().dial.isEmpty()) {
            mBinding.dialTitle.setVisibility(GONE);
            mBinding.dialValue.setVisibility(GONE);
        } else {
            mBinding.dialTitle.setVisibility(VISIBLE);
            mBinding.dialValue.setVisibility(VISIBLE);
            mBinding.dialValue.setText(toString("dial_", mViewModel.entryDetails().dial));
        }

        if (mViewModel.entryDetails().info == null) {
            mBinding.infoTitle.setVisibility(GONE);
            mBinding.infoValue.setVisibility(GONE);
        } else {
            mBinding.infoTitle.setVisibility(VISIBLE);
            mBinding.infoValue.setVisibility(VISIBLE);
            mBinding.infoValue.setText(getStringResourceByName("info_", mViewModel.entryDetails().info));
        }

        mBinding.sentences.addItemDecoration(new DividerItemDecoration(requireContext(), VERTICAL));

        fetchSentences();

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

    private void showProgressBar() {
        mBinding.searchingProgress.setVisibility(VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.searchingProgress.setVisibility(INVISIBLE);
    }

    private void fetchSentences() {
        showProgressBar();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String language = preferences.getString(KEY_LANGUAGE, LANGUAGE_DEFAULT);
        boolean offline = preferences.getBoolean(KEY_OFFLINE, OFFLINE_DEFAULT);
        if (offline) {
            fetchOffline(language);
        } else {
            fetchOnline(language, mViewModel.entryDetails());
        }
    }

    private void fetchOffline(String language) {
        hideProgressBar();
        Toast.makeText(requireContext(), "Offline search not ready yet", Toast.LENGTH_LONG).show();
    }

    private void fetchOnline(String language, EntryDetails entryDetails) {
        RestServiceFactory.getSentencesClient()
                .search(language, mViewModel.entryDetails().kanji, mViewModel.entryDetails().kana, entryDetails.gloss)
                .enqueue(new Callback<List<Sentence>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Sentence>> call, @NonNull Response<List<Sentence>> response) {
                        hideProgressBar();
                        if (response.code() != HTTP_OK) {
                            Log.e("NIHONGO_DICO", "Response code : " + response.code());
                            Toast.makeText(requireContext(), "Response code : " + response.code(), Toast.LENGTH_LONG).show();
                        } else {
                            List<Sentence> sentences = response.body();
                            SentencesAdapter sentencesAdapter = new SentencesAdapter(requireActivity(), sentences);
                            mBinding.sentences.setAdapter(sentencesAdapter);
                            if (sentences == null || sentences.isEmpty()) {
                                mBinding.sentencesTitle.setVisibility(INVISIBLE);
                            } else {
                                mBinding.sentencesTitle.setVisibility(VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Sentence>> call, @NonNull Throwable t) {
                        hideProgressBar();
                        Log.e("NIHONGO_DICO", "Error while fetching sentences", t);
                        Toast.makeText(requireContext(), "Call failure", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
