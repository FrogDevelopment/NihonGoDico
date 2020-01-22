package fr.frogdevelopment.nihongo.dico.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.rest.EntriesClient;
import fr.frogdevelopment.nihongo.dico.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.search.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static java.net.HttpURLConnection.HTTP_OK;

public class MainFragment extends Fragment {

    private EntriesClient entriesClient;
    private MainViewModel mViewModel;

    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private EntriesAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        entriesClient = RestServiceFactory.getEntriesClient();

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSearch(view);

        mProgressBar = view.findViewById(R.id.main_progress);
        mListView = view.findViewById(R.id.main_entries);
    }

    private void initSearch(@NonNull View view) {
        mSearchView = view.findViewById(R.id.main_search_field);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mProgressBar.setVisibility(VISIBLE);
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ImageView closeButton = mSearchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            // Clear the text from EditText view
            SearchView.SearchAutoComplete searchAutoComplete = view.findViewById(R.id.search_src_text);
            searchAutoComplete.setText(null);

            mSearchView.setQuery("", false);

            if (mAdapter != null) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void search(String query) {
        entriesClient.search("eng", query).enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(@NonNull Call<List<Entry>> call, @NonNull Response<List<Entry>> response) {

                if (response.code() != HTTP_OK) {
                    Toast.makeText(requireContext(), "Response code : " + response.code(), LENGTH_SHORT).show();
                } else {
                    List<Entry> entries = response.body();

                    mAdapter = new EntriesAdapter(requireActivity(), entries);

                    mListView.setAdapter(mAdapter);

//                    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(requireContext(), AUTHORITY, MODE);
//                    suggestions.saveRecentQuery(query, entries.size() + " results");

                    mProgressBar.setVisibility(INVISIBLE);

                    mSearchView.clearFocus();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Entry>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Call failure", LENGTH_SHORT).show();
            }
        });
    }
}