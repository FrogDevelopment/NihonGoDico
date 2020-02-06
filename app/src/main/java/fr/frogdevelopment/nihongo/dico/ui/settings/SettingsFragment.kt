package fr.frogdevelopment.nihongo.dico.ui.settings

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.frogdevelopment.nihongo.dico.BuildConfig
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.contentprovider.MySuggestionProvider
import fr.frogdevelopment.nihongo.dico.data.entities.Entry
import fr.frogdevelopment.nihongo.dico.data.rest.RestServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : PreferenceFragmentCompat() {

    private val entriesClient = RestServiceFactory.entriesClient
    private val sentencesClient = RestServiceFactory.sentencesClient

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("settings_version")!!.summary = BuildConfig.VERSION_NAME

        val clearHistory = findPreference<Preference>(KEY_CLEAR_HISTORY)
        clearHistory!!.setOnPreferenceClickListener {
            clearHistory()
            true
        }

        val offline = findPreference<SwitchPreference>(KEY_OFFLINE)
        offline!!.setOnPreferenceChangeListener { _: Preference?, newValue: Any ->
            handleDownloadsVisibility(java.lang.Boolean.TRUE == newValue)
            true
        }
        handleDownloadsVisibility(offline.isChecked)

        val downloadEntries = findPreference<Preference>(KEY_DOWNLOAD_ENTRIES)
        downloadEntries!!.setOnPreferenceClickListener {
            downloadEntries()
            true
        }
    }

    private fun clearHistory() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_clear_history_dialog_title)
                .setPositiveButton(R.string.settings_clear_history_dialog_positive_button) { _, _ ->
                    val searchRecentSuggestions = SearchRecentSuggestions(requireContext(), MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    searchRecentSuggestions.clearHistory()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun handleDownloadsVisibility(visible: Boolean) {
        findPreference<Preference>("settings_download_category")!!.isVisible = visible
    }

    private fun downloadEntries() {
        val languages = findPreference<ListPreference>(KEY_LANGUAGE)
        val lang = languages!!.value
        entriesClient
                .import(lang)
                .enqueue(object : Callback<List<Entry>> {
                    override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                        if (response.isSuccessful) {
                            Log.d("NIHONGO_DICO", "Success")
                        } else {
                            Log.e("NIHONGO_DICO", "Fetching details response code : " + response.code())
                        }
                    }

                    override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                        Log.e("NIHONGO_DICO", "Fetching details error", t)
                    }
                })
    }

    companion object {
        const val KEY_LANGUAGE = "languageTag"
        const val LANGUAGE_DEFAULT = "eng"
        const val KEY_OFFLINE = "settings_offline"
        const val OFFLINE_DEFAULT = false
        const val KEY_CLEAR_HISTORY = "settings_clear_history"
        const val KEY_DOWNLOAD_ENTRIES = "settings_download_entries"
        const val KEY_DOWNLOAD_SENTENCES = "settings_download_sentences"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}