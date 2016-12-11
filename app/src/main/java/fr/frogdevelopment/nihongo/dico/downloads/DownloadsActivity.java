package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import fr.frogdevelopment.nihongo.dico.R;

// fixme test connection available
public class DownloadsActivity extends AppCompatActivity {

	public static final int LANGUAGE_ENG = 1;
	public static final int LANGUAGE_FRA = 2;

	private SharedPreferences defaultSharedPreferences;

	private Button  mDico;
	private Button  mExample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		Spinner mLanguages = (Spinner) findViewById(R.id.download_language);
		mLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				onLanguageSelected(i);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		mDico = (Button) findViewById(R.id.download_dico);
		mDico.setOnClickListener(view -> warningBigFile((dialog, id) -> {
			new DicoDownLoadTask(this, languageTag).execute();
			mLanguages.setEnabled(false); // todo on finish download
		}));
		mExample = (Button) findViewById(R.id.download_examples);
		mExample.setOnClickListener(view -> warningBigFile((dialog, id) -> {
			new ExampleDownLoadTask(this, languageTag).execute();
			mLanguages.setEnabled(false); // todo on finish download
		}));

		defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		int language_saved = defaultSharedPreferences.getInt("language", 0);
		if (language_saved != 0) {

			boolean entries_saved = defaultSharedPreferences.getBoolean(DicoDownLoadTask.PREFERENCES_NAME, false);
			mDico.setEnabled(!entries_saved);

			boolean examples_saved = defaultSharedPreferences.getBoolean(ExampleDownLoadTask.PREFERENCES_NAME, false);
			mExample.setEnabled(!examples_saved);

			mLanguages.setSelection(language_saved);
			mLanguages.setEnabled(!entries_saved && !examples_saved);

			switch (language_saved) {
				case LANGUAGE_ENG:
					languageTag = "eng";
					break;

				case LANGUAGE_FRA:
					languageTag = "fra";
					break;
			}
		} else {
			mLanguages.setEnabled(true);
		}
	}

	private void warningBigFile(DialogInterface.OnClickListener onClickListener) {
		// todo force WiFi ?
		new AlertDialog.Builder(this)
				.setMessage(R.string.download_big_file_warning)
				.setPositiveButton(android.R.string.ok, onClickListener)
				.setNegativeButton(android.R.string.cancel, null)
				.create()
				.show();
	}

	// FIXME
	private boolean init = false;

	// fixme better way
	private String languageTag;

	private void onLanguageSelected(int pos) {
		if (!init) {
			init = true;
			return;
		}

		switch (pos) {
			case LANGUAGE_ENG:
				languageTag = "eng";
				break;

			case LANGUAGE_FRA:
				languageTag = "fra";
				break;

			default:
				mDico.setEnabled(false);
				mExample.setEnabled(false);
				return;
		}

		SharedPreferences.Editor editor = defaultSharedPreferences.edit();
		editor.putInt("language", pos);
		if (editor.commit()) {
			mDico.setEnabled(true);
			mExample.setEnabled(true);
		}
	}
}
