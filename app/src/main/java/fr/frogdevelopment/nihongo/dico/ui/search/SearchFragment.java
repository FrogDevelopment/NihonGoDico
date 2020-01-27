package fr.frogdevelopment.nihongo.dico.ui.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel;
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.data.rest.search.Entry;
import fr.frogdevelopment.nihongo.dico.data.rest.search.EntryDetails;
import fr.frogdevelopment.nihongo.dico.data.search.SearchViewModel;
import fr.frogdevelopment.nihongo.dico.databinding.SearchFragmentBinding;
import fr.frogdevelopment.nihongo.dico.ui.details.DetailsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.KEY_LANGUAGE;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.KEY_OFFLINE;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.LANGUAGE_DEFAULT;
import static fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.OFFLINE_DEFAULT;
import static java.net.HttpURLConnection.HTTP_OK;

public class SearchFragment extends Fragment implements EntriesAdapter.OnEntryClickListener {

    private SearchViewModel mSearchViewModel;
    private DetailsViewModel mDetailsViewModel;

    private EntriesAdapter mAdapter;
    private SearchFragmentBinding mBinding;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mDetailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        mAdapter = new EntriesAdapter(requireContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = SearchFragmentBinding.inflate(getLayoutInflater());

        mBinding.entriesRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.entriesRecyclerview.setAdapter(mAdapter);
        mBinding.entriesRecyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), VERTICAL));

        mSearchViewModel.searching().observe(requireActivity(), this::onSearchStart);
        mSearchViewModel.entries().observe(requireActivity(), this::onSearchFinished);
        mSearchViewModel.error().observe(requireActivity(), this::onSearchError);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSearchViewModel.searching().removeObservers(requireActivity());
        mSearchViewModel.entries().removeObservers(requireActivity());
        mSearchViewModel.error().removeObservers(requireActivity());
        mBinding = null;
    }

    private void onSearchStart(Boolean isSearching) {
        if (isSearching) {
            mBinding.searchingProgress.show();
        } else {
            mBinding.searchingProgress.hide();
        }
    }

    private void onSearchFinished(List<Entry> entries) {
        mBinding.searchingProgress.hide();
        mAdapter.setEntries(entries);
    }

    private void onSearchError(String error) {
        mBinding.searchingProgress.hide();
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEntryClick(String senseSeq) {
        mBinding.searchingProgress.show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String language = preferences.getString(KEY_LANGUAGE, LANGUAGE_DEFAULT);
        boolean offline = preferences.getBoolean(KEY_OFFLINE, OFFLINE_DEFAULT);
        if (offline) {
            searchOffline(senseSeq, language);
        } else {
            searchOnline(senseSeq, language);
        }
    }

    private void searchOffline(String senseSeq, String language) {
        mBinding.searchingProgress.hide();
        Toast.makeText(requireContext(), "Offline search not ready yet", Toast.LENGTH_LONG).show();
    }

    private void searchOnline(String senseSeq, String language) {
        RestServiceFactory.getEntriesClient().getDetails(language, senseSeq).enqueue(new Callback<EntryDetails>() {
            @Override
            public void onResponse(@NonNull Call<EntryDetails> call, @NonNull Response<EntryDetails> response) {
                mBinding.searchingProgress.hide();
                if (response.code() != HTTP_OK) {
                    Log.e("NIHONGO_DICO", "Response code : " + response.code());
                    Toast.makeText(requireContext(), "Response code : " + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    onDetails(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntryDetails> call, @NonNull Throwable t) {
                mBinding.searchingProgress.hide();
                Log.e("NIHONGO_DICO", "Error while fetching details", t);
                Toast.makeText(requireContext(), "Call failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onDetails(EntryDetails details) {
        mDetailsViewModel.setDetails(details);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}