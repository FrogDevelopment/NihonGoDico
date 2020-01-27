package fr.frogdevelopment.nihongo.dico.ui.settings;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import fr.frogdevelopment.nihongo.dico.R;

import static fr.frogdevelopment.nihongo.dico.BuildConfig.VERSION_NAME;
import static java.lang.Boolean.TRUE;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_LANGUAGE = "languageTag";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        setSummary("settings_version", VERSION_NAME);

        SwitchPreference online = findPreference("settings_offline");
        if (online != null) {
            online.setOnPreferenceChangeListener((preference, newValue) -> {
                handleDownloadsVisibility(TRUE.equals(newValue));
                return true;
            });

            handleDownloadsVisibility(online.isChecked());
        }
    }

    private void handleDownloadsVisibility(boolean visible) {
        Preference preference = findPreference("settings_download_category");
        if (preference != null) {
            preference.setVisible(visible);
        }
    }

    private void setSummary(String key, String summary) {
        Preference preference = findPreference(key);
        if (preference != null) {
            preference.setSummary(summary);
        }
    }
}
