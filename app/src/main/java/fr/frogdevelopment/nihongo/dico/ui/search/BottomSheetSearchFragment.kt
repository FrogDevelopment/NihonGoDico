package fr.frogdevelopment.nihongo.dico.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.search.SearchViewModel
import fr.frogdevelopment.nihongo.dico.databinding.SearchsheetFragmentBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchsheetFragmentBinding.inflate(layoutInflater)
        binding.bottomSearchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                dismiss()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        binding.bottomSearchField.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = binding.bottomSearchField.suggestionsAdapter.cursor
                cursor.moveToPosition(position)
                val suggestion = cursor.getString(2)
                viewModel.search(suggestion)
                dismiss()
                return true
            }

        })

        // Get the SearchView and set the searchable configuration
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (binding.bottomSearchField).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        }

        if (viewModel.isOffline()) {
            binding.onlineWarning.visibility = View.GONE
        } else {
            binding.onlineWarning.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}