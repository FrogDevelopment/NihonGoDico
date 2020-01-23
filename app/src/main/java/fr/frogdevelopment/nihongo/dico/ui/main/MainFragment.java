package fr.frogdevelopment.nihongo.dico.ui.main;

import android.os.Bundle;
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
import fr.frogdevelopment.nihongo.dico.search.Entry;

public class MainFragment extends Fragment {

    private EntriesAdapter mAdapter;
    private ContentLoadingProgressBar mSearchingProgress;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.searching().observe(requireActivity(), this::onSearchStart);
        viewModel.entries().observe(requireActivity(), this::onSearchFinished);
        viewModel.error().observe(requireActivity(), this::onSearchError);
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
        mAdapter = new EntriesAdapter(requireContext());
        recyclerView.setAdapter(mAdapter);

        mSearchingProgress = root.findViewById(R.id.searching_progress);

        return root;
    }

}