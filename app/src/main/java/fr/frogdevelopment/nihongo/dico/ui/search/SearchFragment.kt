package fr.frogdevelopment.nihongo.dico.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel
import fr.frogdevelopment.nihongo.dico.data.rest.Entry
import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.search.SearchViewModel
import fr.frogdevelopment.nihongo.dico.databinding.SearchFragmentBinding
import fr.frogdevelopment.nihongo.dico.ui.details.DetailsFragment
import fr.frogdevelopment.nihongo.dico.ui.search.EntriesAdapter.OnEntryClickListener

class SearchFragment : Fragment(), OnEntryClickListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var detailsViewModel: DetailsViewModel

    private var _binding: SearchFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        detailsViewModel = ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchFragmentBinding.inflate(layoutInflater)
        binding.entriesRecyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        searchViewModel.searching().observe(requireActivity(), Observer { isSearching: Boolean -> onSearch(isSearching) })
        searchViewModel.entries().observe(requireActivity(), Observer { entries: List<Entry> -> onSearchFinished(entries) })
        searchViewModel.error().observe(requireActivity(), Observer { error: String -> onSearchError(error) })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchViewModel.searching().removeObservers(requireActivity())
        searchViewModel.entries().removeObservers(requireActivity())
        searchViewModel.error().removeObservers(requireActivity())
        _binding = null
    }

    private fun onSearch(isSearching: Boolean) {
        if (isSearching) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }

    private fun onSearchFinished(entries: List<Entry>) {
        hideProgressBar()
        binding.entriesRecyclerview.adapter = EntriesAdapter(requireContext(), this, entries)
    }

    private fun onSearchError(error: String) {
        hideProgressBar()
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    private fun showProgressBar() {
        binding.searchingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.searchingProgress.visibility = View.INVISIBLE
    }

    override fun onEntryClick(senseSeq: String) {
        showProgressBar()
        detailsViewModel.searchEntryDetails(senseSeq).observe(viewLifecycleOwner, Observer { entryDetails -> showDetails(entryDetails) })
    }

    private fun showDetails(details: EntryDetails?) {
        hideProgressBar()
        if (details == null) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        } else {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, DetailsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
        }
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }

    }
}