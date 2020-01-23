package fr.frogdevelopment.nihongo.dico.to_delete.downloads;

import android.content.ContentValues;
import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;

import fr.frogdevelopment.nihongo.dico.contentprovider.ExampleContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;

import static fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider.URI_EXAMPLE;

class ExampleDownLoadTask extends AbstractDownLoadTask {

	static final String PREFERENCES_NAME = "examples_saved";

	private static final String fileName = "examples_jpn_%s.csv";

	public ExampleDownLoadTask(Context context, String language, DownloadListener listener) {
		super(context, String.format(fileName, language), URI_EXAMPLE, PREFERENCES_NAME, listener);
	}

	@Override
	protected void loopOnLines(HttpURLConnection connection) throws IOException {
		super.loopOnLines(connection);
		mContext.getContentResolver().update(NihonGoDicoContentProvider.URI_REBUILD_EXAMPLE, null, null, null);
	}

	@Override
	protected void fillValues(String currentLine) {
		// line format : Jpn_seq_no[TAB]Eng_seq_no[TAB]Japanese sentence[TAB]English sentence[TAB]Indices
		String[] values = currentLine.split("\\t");

		ContentValues contentValues = new ContentValues();
		contentValues.put(ExampleContract.JAPANESE_REF, values[0]);
		contentValues.put(ExampleContract.TRANSLATION_REF, values[1]);
		contentValues.put(ExampleContract.JAPANESE_SENTENCE, values[2]);
		contentValues.put(ExampleContract.TRANSLATION_SENTENCE, values[3]);
		contentValues.put(ExampleContract.INDICES, values[4]);

		mValueList.add(contentValues);
	}
}
