package fr.frogdevelopment.nihongo.dico.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel
import fr.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.entities.EntrySearch
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.entriesRecyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        searchViewModel.isSearching.observe(viewLifecycleOwner, { isSearching -> onSearch(isSearching) })
        searchViewModel.entries.observe(viewLifecycleOwner, { entries -> onSearchFinished(entries) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSearch(isSearching: Boolean) {
        if (isSearching) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }

    private fun onSearchFinished(entries: List<EntrySearch>?) {
        if (entries == null) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        } else {
            binding.entriesRecyclerview.adapter = EntriesAdapter(requireContext(), this, entries)
        }
    }

    private fun showProgressBar() {
        binding.searchingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.searchingProgress.visibility = View.INVISIBLE
    }

    override fun onEntryClick(senseSeq: String) {
        showProgressBar()
        detailsViewModel.searchEntryDetails(senseSeq).observe(viewLifecycleOwner, { entryDetails -> showDetails(entryDetails) })
    }

    private fun showDetails(details: EntryDetails?) {
        hideProgressBar()
        if (details == null) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        } else {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.main_container, DetailsFragment::class.java, null)
                setReorderingAllowed(true)
                addToBackStack("search")
            }
        }
    }

}