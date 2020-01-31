package fr.frogdevelopment.nihongo.dico.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import fr.frogdevelopment.nihongo.dico.BuildConfig
import fr.frogdevelopment.nihongo.dico.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("settings_version")!!.summary = BuildConfig.VERSION_NAME

        val offline = findPreference<SwitchPreference>(KEY_OFFLINE)
        offline!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any ->
            handleDownloadsVisibility(java.lang.Boolean.TRUE == newValue)
            true
        }
        handleDownloadsVisibility(offline.isChecked)
    }

    private fun handleDownloadsVisibility(visible: Boolean) {
        findPreference<Preference>("settings_download_category")!!.isVisible = visible
    }

    companion object {
        const val KEY_LANGUAGE = "languageTag"
        const val LANGUAGE_DEFAULT = "eng"
        const val KEY_OFFLINE = "settings_offline"
        const val OFFLINE_DEFAULT = false

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}