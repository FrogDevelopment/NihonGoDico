package fr.frogdevelopment.nihongo.dico.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.rest.Entry
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory
import fr.frogdevelopment.nihongo.dico.data.search.SearchViewModel
import fr.frogdevelopment.nihongo.dico.databinding.SearchsheetFragmentBinding
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment
import org.apache.commons.lang3.exception.ExceptionUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class BottomSheetSearchFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: SearchViewModel

    private var _binding: SearchsheetFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SearchBottomSheetDialog)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchsheetFragmentBinding.inflate(layoutInflater)
        binding.bottomSearchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun search(query: String) {
        viewModel.isSearching(true)
        val language = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(SettingsFragment.KEY_LANGUAGE, "eng")
        RestServiceFactory.entriesClient.search(language!!, query).enqueue(object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    viewModel.setError("Response code : " + response.code())
                } else {
                    viewModel.setEntries(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                Log.e("NIHONGO_DICO", "Error while searching", t)
                viewModel.setError("Failure: " + ExceptionUtils.getMessage(t))
            }
        })
        dismiss()
    }
}