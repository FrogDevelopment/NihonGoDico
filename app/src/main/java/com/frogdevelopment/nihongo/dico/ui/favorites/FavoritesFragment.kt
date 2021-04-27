package com.frogdevelopment.nihongo.dico.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.details.DetailsViewModel
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.data.favorites.FavoritesViewModel
import com.frogdevelopment.nihongo.dico.databinding.SearchFragmentBinding
import com.frogdevelopment.nihongo.dico.ui.details.DetailsFragment
import com.frogdevelopment.nihongo.dico.ui.favorites.FavoritesAdapter.OnEntryClickListener

class FavoritesFragment : Fragment(), OnEntryClickListener {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var detailsViewModel: DetailsViewModel

    private var _binding: SearchFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesViewModel = ViewModelProvider(requireActivity()).get(FavoritesViewModel::class.java)
        detailsViewModel = ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.entriesRecyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        favoritesViewModel.getFavorites().observe(viewLifecycleOwner, { favorites -> onSearchFinished(favorites) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSearchFinished(entries: List<EntryDetails>) {
        if (entries.isEmpty()) {
            binding.entriesRecyclerview.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.entriesRecyclerview.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            binding.entriesRecyclerview.adapter = FavoritesAdapter(requireContext(), this, entries)
        }
    }

    override fun onEntryClick(entryDetails: EntryDetails) {
        detailsViewModel.setDetails(entryDetails)
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, DetailsFragment::class.java, null)
            setReorderingAllowed(true)
            addToBackStack("search")
        }
    }
}