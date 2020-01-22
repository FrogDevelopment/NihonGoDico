package fr.frogdevelopment.nihongo.dico.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

public class MainFragment extends ListFragment {

    private EntriesAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new EntriesAdapter(requireContext());
        setListAdapter(mAdapter);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.searching().observe(requireActivity(), isSearching -> setListShown(!isSearching));
        viewModel.entries().observe(requireActivity(), entries -> {
            mAdapter.clear();
            mAdapter.addAll(entries);
            setListShown(true);
        });
    }

}