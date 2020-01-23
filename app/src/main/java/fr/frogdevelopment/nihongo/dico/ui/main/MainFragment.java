package fr.frogdevelopment.nihongo.dico.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.rest.EntriesClient;
import fr.frogdevelopment.nihongo.dico.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.search.EntryDetails;
import fr.frogdevelopment.nihongo.dico.ui.details.DetailsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.net.HttpURLConnection.HTTP_OK;

public class MainFragment extends Fragment implements EntriesAdapter.OnEntryClickListener {

    private MainViewModel mViewModel;
    private EntriesClient mEntriesClient;

    private EntriesAdapter mAdapter;
    private ContentLoadingProgressBar mSearchingProgress;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mEntriesClient = RestServiceFactory.getEntriesClient();
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
            mSearchingProgress.show();
        } else {
            mSearchingProgress.hide();
        }
    }

    private void onSearchFinished(List<Entry> entries) {
        mSearchingProgress.hide();
        mAdapter.setEntries(entries);
    }

    private void onSearchError(String error) {
        mSearchingProgress.hide();
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.entries_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new EntriesAdapter(requireContext(), this);
        recyclerView.setAdapter(mAdapter);

        mSearchingProgress = root.findViewById(R.id.searching_progress);

        return root;
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
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new DetailsFragment())
                            .commitNow();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntryDetails> call, @NonNull Throwable t) {
                Log.e("NIHONGO_DICO", "Error while fetching details", t);
                Toast.makeText(requireContext(), "Call failure", Toast.LENGTH_LONG).show();
            }
        });
    }
}