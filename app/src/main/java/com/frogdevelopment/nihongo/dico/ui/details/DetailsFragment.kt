package com.frogdevelopment.nihongo.dico.ui.details

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.details.DetailsViewModel
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel

    private var _binding: DetailsFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java)

        _binding = DetailsFragmentBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        viewModel.entryDetails().observe(viewLifecycleOwner, { entryDetails -> populateView(entryDetails!!) })

        return binding.root
    }

    private fun populateView(entryDetails: EntryDetails) {
        handleFavoriteDrawable(entryDetails)
        binding.favorite.setOnClickListener {
            viewModel.toggleFavorite(entryDetails)
            handleFavoriteDrawable(entryDetails)
        }

        if (entryDetails.pos!!.isEmpty()) {
            binding.posTitle.visibility = View.GONE
            binding.posValue.visibility = View.GONE
        } else {
            binding.posTitle.visibility = View.VISIBLE
            binding.posValue.visibility = View.VISIBLE
            binding.posValue.text = toString("pos_", entryDetails.pos)
        }

        if (entryDetails.field!!.isEmpty()) {
            binding.fieldTitle.visibility = View.GONE
            binding.fieldValue.visibility = View.GONE
        } else {
            binding.fieldTitle.visibility = View.VISIBLE
            binding.fieldValue.visibility = View.VISIBLE
            binding.fieldValue.text = toString("field_", entryDetails.field)
        }

        if (entryDetails.misc!!.isEmpty()) {
            binding.miscTitle.visibility = View.GONE
            binding.miscValue.visibility = View.GONE
        } else {
            binding.miscTitle.visibility = View.VISIBLE
            binding.miscValue.visibility = View.VISIBLE
            binding.miscValue.text = toString("misc_", entryDetails.misc)
        }

        if (entryDetails.dial!!.isEmpty()) {
            binding.dialTitle.visibility = View.GONE
            binding.dialValue.visibility = View.GONE
        } else {
            binding.dialTitle.visibility = View.VISIBLE
            binding.dialValue.visibility = View.VISIBLE
            binding.dialValue.text = toString("dial_", entryDetails.dial)
        }

        if (entryDetails.info == null) {
            binding.infoTitle.visibility = View.GONE
            binding.infoValue.visibility = View.GONE
        } else {
            binding.infoTitle.visibility = View.VISIBLE
            binding.infoValue.visibility = View.VISIBLE
            binding.infoValue.text = getStringResourceByName("info_", entryDetails.info)
        }

        binding.sentences.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.sentences.setHasFixedSize(true)
        fetchSentences(entryDetails)
    }

    private fun handleFavoriteDrawable(entryDetails: EntryDetails) {
        binding.favorite.setImageResource(when (entryDetails.favorite) {
            true -> R.drawable.ic_baseline_favorite_24
            else -> R.drawable.ic_baseline_favorite_border_24
        })
    }

    private fun toString(prefix: String, value: String): String {
        val values = value.split(",")
        val resources: MutableList<String?> = ArrayList(values.size)
        values.mapTo(resources) { getStringResourceByName(prefix, it) }
        return TextUtils.join(", ", resources)
    }

    private fun getStringResourceByName(prefix: String, value: String): String {
        val packageName = requireActivity().packageName
        val resourceName = prefix + value.replace("-".toRegex(), "_")
        val resId = resources.getIdentifier(resourceName, "string", packageName)
        return try {
            getString(resId)
        } catch (e: NotFoundException) {
            value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchSentences(entryDetails: EntryDetails) {
        binding.searchingProgress.visibility = View.VISIBLE
        viewModel.sentences(entryDetails).observe(viewLifecycleOwner, { sentences ->
            binding.searchingProgress.visibility = View.INVISIBLE
            when {
                sentences == null -> {
                    binding.sentencesTitle.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                }
                sentences.isEmpty() -> {
                    binding.sentencesTitle.visibility = View.INVISIBLE
                }
                else -> {
                    val sentencesAdapter = SentencesAdapter(requireActivity(), sentences)
                    binding.sentences.adapter = sentencesAdapter
                    binding.sentencesTitle.visibility = View.VISIBLE
                }
            }
        })
    }

}