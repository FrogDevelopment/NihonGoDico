package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.frogdevelopment.nihongo.dico.R;

// fixme test connection available
public class DownloadsActivity extends Activity {

	private SharedPreferences defaultSharedPreferences;

	@BindView(R.id.download_language)
	Spinner mLanguages;

	@BindView(R.id.download_dico)
	Button mDico;

	@BindView(R.id.download_examples)
	Button mExample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		ButterKnife.bind(this);

		setActionBar(ButterKnife.findById(this, R.id.toolbar));
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mLanguages.setAdapter(adapter);

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
				case 1:
					dicoTag = "eng";
					exampleTag = "eng";
					break;

				case 2:
					dicoTag = "fre";
					exampleTag = "fra";
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
	private String dicoTag;
	private String exampleTag;

	@OnItemSelected(R.id.download_language)
	void onLanguageSelected(int pos) {
		if (!init) {
			init = true;
			return;
		}

		switch (pos) {
			case 1:
				dicoTag = "eng";
				exampleTag = "eng";
				break;

			case 2:
				dicoTag = "fre";
				exampleTag = "fra";
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

	@OnClick(R.id.download_dico)
	void onClickDico() {
		warningBigFile((dialog, id) -> {
			new DicoDownLoadTask(this, dicoTag).execute();
			mLanguages.setEnabled(false); // todo on finish download
		});
	}

	@OnClick(R.id.download_examples)
	void onClickExamples() {
		warningBigFile((dialog, id) -> {
			new ExampleDownLoadTask(this, exampleTag).execute();
			mLanguages.setEnabled(false); // todo on finish download
		});
	}

}
