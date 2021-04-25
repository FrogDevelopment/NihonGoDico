package com.frogdevelopment.nihongo.dico.ui.settings

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.provider.SearchRecentSuggestions
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.frogdevelopment.nihongo.dico.BuildConfig
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.contentprovider.NihonGoDicoContentProvider.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("settings_version")!!.summary = BuildConfig.VERSION_NAME

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

        val downloadSentences = findPreference<Preference>(KEY_DOWNLOAD_SENTENCES)
        downloadSentences!!.setOnPreferenceClickListener {
            downloadSentences()
            true
        }
    }

    private fun clearHistory() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_clear_history_dialog_title)
                .setPositiveButton(R.string.settings_clear_history_dialog_positive_button) { _, _ ->
                    val searchRecentSuggestions = SearchRecentSuggestions(requireContext(), AUTHORITY, MODE)
                    searchRecentSuggestions.clearHistory()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun downloadEntries() {
        val languages = findPreference<ListPreference>(KEY_LANGUAGE)
        val lang = languages?.value

        val pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: WakeLock? = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
        wakeLock?.acquire(5 * 60 * 1000L /*5 minutes*/)
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Downloading Dictionary") //fixme
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        context?.contentResolver?.update(CLEAN_DICO_URI, null, null, null)
        EntriesDownLoadTask(context) {
            SensesDownLoadTask(context) {
                context?.contentResolver?.update(REBUILD_DICO_URI, null, null, null)
                wakeLock?.release()
                progressDialog.dismiss()
            }.execute(lang)
        }.execute(lang)
    }

    private fun downloadSentences() {
        val languages = findPreference<ListPreference>(KEY_LANGUAGE)
        val lang = languages?.value

        val pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: WakeLock? = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
        wakeLock?.acquire(5 * 60 * 1000L /*5 minutes*/)
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Downloading Sentences") //fixme
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        context?.contentResolver?.update(CLEAN_SENTENCE_URI, null, null, null)
        SentencesDownLoadTask(context) {
            context?.contentResolver?.update(REBUILD_SENTENCE_URI, null, null, null)
            wakeLock?.release()
            progressDialog.dismiss()
        }.execute(lang)
    }

    companion object {
        const val KEY_LANGUAGE = "languageTag"
        const val LANGUAGE_DEFAULT = "eng"
        const val KEY_CLEAR_HISTORY = "settings_clear_history"
        const val KEY_DOWNLOAD_ENTRIES = "settings_download_entries"
        const val KEY_DOWNLOAD_SENTENCES = "settings_download_sentences"

    }
}