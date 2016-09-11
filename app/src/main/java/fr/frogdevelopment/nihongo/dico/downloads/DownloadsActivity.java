package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.frogdevelopment.nihongo.dico.R;

public class DownloadsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloads);

		ButterKnife.bind(this);

		setActionBar(ButterKnife.findById(this, R.id.toolbar));
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}


	@OnClick(R.id.download_dico)
	void onClickDico() {
		new DicoDownLoadTask(this).execute();
	}

	@OnClick(R.id.download_examples)
	void onClickExamples() {
		new ExampleDownLoadTask(this).execute();
	}

//
//	boolean data_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("data_saved", false);
//	if (!data_saved) {
//		// fixme test connection available
//		// fixme warning big file => use Wifi
//		new AlertDialog.Builder(this)
//				.setMessage("no data found. Download it ?")
//				.setPositiveButton(android.R.string.ok, (dialog, id) -> new DicoDownLoadTask(MainActivity.this).execute())
//				.setNegativeButton(android.R.string.cancel, null)
//				.create()
//				.show();
//	}

}
