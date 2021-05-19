package com.frogdevelopment.nihongo.dico.ui.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.frogdevelopment.nihongo.dico.BuildConfig
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.AUTHORITY
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.MODE
import com.frogdevelopment.nihongo.dico.service.DictionaryDownloadJobIntentService
import com.frogdevelopment.nihongo.dico.service.SentencesDownloadJobIntentService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Instant
import java.time.ZoneId

class SettingsFragment : PreferenceFragmentCompat() {

    private val onComplete = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            if (intent.action == ACTION) {
                updateSummaryDate(intent.getCharSequenceExtra("preferenceName")!!)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.registerReceiver(onComplete, IntentFilter(ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(onComplete)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>(KEY_VERSION)!!.summary = BuildConfig.VERSION_NAME

        val clearHistory = findPreference<Preference>(KEY_CLEAR_HISTORY)
        clearHistory!!.setOnPreferenceClickListener {
            clearHistory()
            true
        }

        val downloadEntries = findPreference<Preference>(KEY_DOWNLOAD_ENTRIES)
        downloadEntries!!.setOnPreferenceClickListener {
            downloadEntries()
            true
        }
        downloadEntries.summary = getDate(KEY_DOWNLOAD_ENTRIES)

        val downloadSentences = findPreference<Preference>(KEY_DOWNLOAD_SENTENCES)
        downloadSentences!!.setOnPreferenceClickListener {
            downloadSentences()
            true
        }
        downloadSentences.summary = getDate(KEY_DOWNLOAD_SENTENCES)
    }

    private fun clearHistory() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.settings_clear_history_dialog_title)
            .setPositiveButton(R.string.settings_clear_history_dialog_positive_button) { _, _ ->
                val searchRecentSuggestions =
                    SearchRecentSuggestions(requireContext(), AUTHORITY, MODE)
                searchRecentSuggestions.clearHistory()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun downloadEntries() {
        val languages = findPreference<ListPreference>(KEY_LANGUAGE)

        val intent = Intent(context, DictionaryDownloadJobIntentService::class.java)
        intent.putExtra("lang", languages?.value)
        DictionaryDownloadJobIntentService.enqueueWork(context, intent)
    }

    private fun downloadSentences() {
        val languages = findPreference<ListPreference>(KEY_LANGUAGE)

        val intent = Intent(context, SentencesDownloadJobIntentService::class.java)
        intent.putExtra("lang", languages?.value)
        SentencesDownloadJobIntentService.enqueueWork(context, intent)
    }

    private fun getDate(preferenceName: String): String {
        val date = preferenceManager.sharedPreferences.getLong(preferenceName, 0)
        return when {
            date != 0L -> getString(
                R.string.downloaded_date,
                Instant.ofEpochMilli(date).atZone(ZoneId.of("Z"))
            )
            else -> getString(R.string.downloaded_empty)
        }
    }

    private fun updateSummaryDate(preferenceName: CharSequence) {
        val preference = findPreference<Preference>(preferenceName)
        preference!!.summary = getDate(preferenceName.toString())
    }

    companion object {
        const val KEY_VERSION = "settings_version"
        const val KEY_LANGUAGE = "languageTag"
        const val KEY_CLEAR_HISTORY = "settings_clear_history"
        const val KEY_DOWNLOAD_ENTRIES = "settings_download_entries"
        const val KEY_DOWNLOAD_SENTENCES = "settings_download_sentences"
        const val ACTION = "com.frogdevelopment.nihongo.dico.Downloaded"
    }
}