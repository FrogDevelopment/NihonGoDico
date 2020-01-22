package fr.frogdevelopment.nihongo.dico.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.frogdevelopment.nihongo.dico.R;

public class MainFragment extends Fragment {

    private EntriesAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.searching().observe(requireActivity(), isSearching -> {
//            setListShown(!isSearching);
        });
        viewModel.entries().observe(requireActivity(), entries -> mAdapter.setEntries(entries));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.entries_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new EntriesAdapter(requireContext());
        recyclerView.setAdapter(mAdapter);
        return root;
    }

}