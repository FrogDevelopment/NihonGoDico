package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.frogdevelopment.nihongo.dico.R;

// fixme test connection available
public class DownloadsActivity extends AppCompatActivity {

	private static final List<String> AVAILABLE_LANG = Arrays.asList("eng", "fra", "dut", "ger", "rus", "hun", "ita", "esp", "slv", "swe");

	private String languageTag;

	private SharedPreferences defaultSharedPreferences;

	private Button mDico;
	private Button mExample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		mDico = findViewById(R.id.download_dico);
		mDico.setOnClickListener(view -> warningBigFile((dialog, id) -> new DicoDownLoadTask(this, languageTag, () -> mDico.setEnabled(false)).execute()));

		mExample = findViewById(R.id.download_examples);
		mExample.setOnClickListener(view -> warningBigFile((dialog, id) -> new ExampleDownLoadTask(this, languageTag, () -> mExample.setEnabled(false)).execute()));


		defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		languageTag = defaultSharedPreferences.getString("languageTag", "");
		if (!TextUtils.isEmpty(languageTag)) {
			boolean entries_saved = defaultSharedPreferences.getBoolean(DicoDownLoadTask.PREFERENCES_NAME, false);
			mDico.setEnabled(!entries_saved);

			boolean examples_saved = defaultSharedPreferences.getBoolean(ExampleDownLoadTask.PREFERENCES_NAME, false);
			mExample.setEnabled(!examples_saved);
		}

		TextView selectedLanguageView = findViewById(R.id.download_selected_language);


		String[] languages = Locale.getISOLanguages();
		final String[] items = new String[AVAILABLE_LANG.size()];
		int nbItems = 0;
		Map<String, Locale> localeMap = new HashMap<>(languages.length);
		for (String language : languages) {
			Locale locale = new Locale(language);
			if (AVAILABLE_LANG.contains(locale.getISO3Language())) {
				String lang = StringUtils.capitalize(locale.getDisplayLanguage());
				localeMap.put(lang, locale);
				items[nbItems++] = lang;

				if (locale.getISO3Language().equals(languageTag)) {
					selectedLanguageView.setText(lang);
				}
			}
		}

		String[] copyOf = Arrays.copyOf(items, nbItems);

		findViewById(R.id.download_change_language).setOnClickListener(v -> new AlertDialog.Builder(DownloadsActivity.this)
				.setTitle(R.string.downloads_entries_hint)
				.setItems(copyOf, (dialog, which) -> {
					String language = items[which];

					selectedLanguageView.setText(language);

					Locale locale = localeMap.get(language);
					languageTag = locale.getISO3Language();

					SharedPreferences.Editor editor = defaultSharedPreferences.edit();
					editor.putString("languageTag", languageTag);
					if (editor.commit()) {
						mDico.setEnabled(true);
						mExample.setEnabled(true);
					}
				}).create()
				.show());

	}

	private void warningBigFile(DialogInterface.OnClickListener onClickListener) {
		// todo force WiFi ?
		new AlertDialog.Builder(this)
				.setMessage(R.string.downloads_big_file_warning)
				.setPositiveButton(android.R.string.ok, onClickListener)
				.setNegativeButton(android.R.string.cancel, null)
				.create()
				.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_bottom, R.anim.exit_to_top);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				overridePendingTransition(R.anim.enter_from_bottom, R.anim.exit_to_top);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
