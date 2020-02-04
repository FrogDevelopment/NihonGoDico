package fr.frogdevelopment.nihongo.dico.ui.settings

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.frogdevelopment.nihongo.dico.BuildConfig
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.contentprovider.MySuggestionProvider

class SettingsFragment : PreferenceFragmentCompat() {

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

    companion object {
        const val KEY_LANGUAGE = "languageTag"
        const val LANGUAGE_DEFAULT = "eng"
        const val KEY_OFFLINE = "settings_offline"
        const val OFFLINE_DEFAULT = false
        const val KEY_CLEAR_HISTORY = "settings_clear_history"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}