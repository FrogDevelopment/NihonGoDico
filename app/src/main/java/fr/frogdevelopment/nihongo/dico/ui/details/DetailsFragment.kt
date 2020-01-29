package fr.frogdevelopment.nihongo.dico.ui.details

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.details.DetailsViewModel
import fr.frogdevelopment.nihongo.dico.data.rest.EntryDetails
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence
import fr.frogdevelopment.nihongo.dico.databinding.DetailsFragmentBinding
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.Companion.KEY_LANGUAGE
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.Companion.KEY_OFFLINE
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.Companion.LANGUAGE_DEFAULT
import fr.frogdevelopment.nihongo.dico.ui.settings.SettingsFragment.Companion.OFFLINE_DEFAULT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel
    
    private var _binding: DetailsFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java)
        
        _binding = DetailsFragmentBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        binding.kanji.typeface = ResourcesCompat.getFont(requireContext(), R.font.sawarabi_mincho)
        binding.kana.typeface = ResourcesCompat.getFont(requireContext(), R.font.sawarabi_gothic)
        
        if (viewModel.entryDetails().pos!!.isEmpty()) {
            binding.posTitle.visibility = View.GONE
            binding.posValue.visibility = View.GONE
        } else {
            binding.posTitle.visibility = View.VISIBLE
            binding.posValue.visibility = View.VISIBLE
            binding.posValue.text = toString("pos_", viewModel.entryDetails().pos!!)
        }
        
        if (viewModel.entryDetails().field!!.isEmpty()) {
            binding.fieldTitle.visibility = View.GONE
            binding.fieldValue.visibility = View.GONE
        } else {
            binding.fieldTitle.visibility = View.VISIBLE
            binding.fieldValue.visibility = View.VISIBLE
            binding.fieldValue.text = toString("field_", viewModel.entryDetails().field!!)
        }
        
        if (viewModel.entryDetails().misc!!.isEmpty()) {
            binding.miscTitle.visibility = View.GONE
            binding.miscValue.visibility = View.GONE
        } else {
            binding.miscTitle.visibility = View.VISIBLE
            binding.miscValue.visibility = View.VISIBLE
            binding.miscValue.text = toString("misc_", viewModel.entryDetails().misc!!)
        }
        
        if (viewModel.entryDetails().dial!!.isEmpty()) {
            binding.dialTitle.visibility = View.GONE
            binding.dialValue.visibility = View.GONE
        } else {
            binding.dialTitle.visibility = View.VISIBLE
            binding.dialValue.visibility = View.VISIBLE
            binding.dialValue.text = toString("dial_", viewModel.entryDetails().dial!!)
        }
        
        if (viewModel.entryDetails().info == null) {
            binding.infoTitle.visibility = View.GONE
            binding.infoValue.visibility = View.GONE
        } else {
            binding.infoTitle.visibility = View.VISIBLE
            binding.infoValue.visibility = View.VISIBLE
            binding.infoValue.text = getStringResourceByName("info_", viewModel.entryDetails().info!!)
        }
        
        binding.sentences.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        fetchSentences()
        
        return binding.root
    }

    private fun toString(prefix: String, values: Set<String>): String {
        val resources: MutableList<String?> = ArrayList(values.size)
        for (value in values) {
            resources.add(getStringResourceByName(prefix, value))
        }
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

    private fun showProgressBar() {
        binding.searchingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.searchingProgress.visibility = View.INVISIBLE
    }

    private fun fetchSentences() {
        showProgressBar()
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = preferences.getString(KEY_LANGUAGE, LANGUAGE_DEFAULT)
        val offline = preferences.getBoolean(KEY_OFFLINE, OFFLINE_DEFAULT)
        if (offline) {
            fetchOffline(language)
        } else {
            fetchOnline(language!!, viewModel.entryDetails())
        }
    }

    private fun fetchOffline(language: String?) {
        hideProgressBar()
        Toast.makeText(requireContext(), "Offline search not ready yet", Toast.LENGTH_LONG).show()
    }

    private fun fetchOnline(language: String, entryDetails: EntryDetails) {
        RestServiceFactory.sentencesClient
                .search(language, viewModel.entryDetails().kanji, viewModel.entryDetails().kana!!, entryDetails.gloss!!)
                .enqueue(object : Callback<List<Sentence>?> {
                    override fun onResponse(call: Call<List<Sentence>?>, response: Response<List<Sentence>?>) {
                        hideProgressBar()
                        if (response.code() != HttpURLConnection.HTTP_OK) {
                            Log.e("NIHONGO_DICO", "Response code : " + response.code())
                            Toast.makeText(requireContext(), "Response code : " + response.code(), Toast.LENGTH_LONG).show()
                        } else {
                            val sentences = response.body()
                            if (sentences == null || sentences.isEmpty()) {
                                binding.sentencesTitle.visibility = View.INVISIBLE
                            } else {
                                val sentencesAdapter = SentencesAdapter(requireActivity(), sentences)
                                binding.sentences.adapter = sentencesAdapter
                                binding.sentencesTitle.visibility = View.INVISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Sentence>?>, t: Throwable) {
                        hideProgressBar()
                        Log.e("NIHONGO_DICO", "Error while fetching sentences", t)
                        Toast.makeText(requireContext(), "Call failure", Toast.LENGTH_LONG).show()
                    }
                })
    }

    companion object {
        fun newInstance(): DetailsFragment {
            return DetailsFragment()
        }
    }
}