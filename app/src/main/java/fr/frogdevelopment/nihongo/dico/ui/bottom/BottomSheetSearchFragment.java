package fr.frogdevelopment.nihongo.dico.ui.bottom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.rest.EntriesClient;
import fr.frogdevelopment.nihongo.dico.rest.RestServiceFactory;
import fr.frogdevelopment.nihongo.dico.search.Entry;
import fr.frogdevelopment.nihongo.dico.ui.main.MainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static java.net.HttpURLConnection.HTTP_OK;

public class BottomSheetSearchFragment extends BottomSheetDialogFragment {

    private EntriesClient entriesClient;
    private MainViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SearchBottomSheetDialog);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        entriesClient = RestServiceFactory.getEntriesClient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchsheet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView mSearchView = view.findViewById(R.id.bottom_search_field);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        });
    }

    private void search(String query) {
        dismiss();
        mViewModel.isSearching(true);
        entriesClient.search("eng", query).enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(@NonNull Call<List<Entry>> call, @NonNull Response<List<Entry>> response) {

                if (response.code() != HTTP_OK) {
                    Toast.makeText(requireContext(), "Response code : " + response.code(), LENGTH_SHORT).show();
                    mViewModel.isSearching(false);
                } else {
                    mViewModel.setEntries(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Entry>> call, @NonNull Throwable t) {
                Log.e("NIHONGO_DICO", "Error while searching", t);
                Toast.makeText(requireContext(), "Call failure", LENGTH_SHORT).show();
                mViewModel.isSearching(false);
            }
        });
    }
}
