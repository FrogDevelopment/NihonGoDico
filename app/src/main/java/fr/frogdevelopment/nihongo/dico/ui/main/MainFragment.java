package fr.frogdevelopment.nihongo.dico.ui.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.databinding.MainFragmentBinding;
import fr.frogdevelopment.nihongo.dico.rest.EntriesClient;
import fr.frogdevelopment.nihongo.dico.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.search.EntryDetails;
import fr.frogdevelopment.nihongo.dico.to_delete.MainActivity;
import fr.frogdevelopment.nihongo.dico.ui.details.DetailsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.net.HttpURLConnection.HTTP_OK;

public class MainFragment extends Fragment implements EntriesAdapter.OnEntryClickListener {

    private MainViewModel mViewModel;
    private EntriesClient mEntriesClient;

    private EntriesAdapter mAdapter;
    private MainFragmentBinding mBinding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mEntriesClient = RestServiceFactory.getEntriesClient();
        mAdapter = new EntriesAdapter(requireContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = MainFragmentBinding.inflate(getLayoutInflater());

        mBinding.entriesRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.entriesRecyclerview.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.searching().observe(requireActivity(), this::onSearchStart);
        mViewModel.entries().observe(requireActivity(), this::onSearchFinished);
        mViewModel.error().observe(requireActivity(), this::onSearchError);
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
        mEntriesClient.getDetails("eng", senseSeq).enqueue(new Callback<EntryDetails>() {
            @Override
            public void onResponse(@NonNull Call<EntryDetails> call, @NonNull Response<EntryDetails> response) {
                if (response.code() != HTTP_OK) {
                    Log.e("NIHONGO_DICO", "Response code : " + response.code());
                    Toast.makeText(requireContext(), "Response code : " + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    mViewModel.setDetails(response.body());
                    ((MainActivity) requireActivity()).switchToDetails();
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, DetailsFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntryDetails> call, @NonNull Throwable t) {
                Log.e("NIHONGO_DICO", "Error while fetching details", t);
                Toast.makeText(requireContext(), "Call failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}