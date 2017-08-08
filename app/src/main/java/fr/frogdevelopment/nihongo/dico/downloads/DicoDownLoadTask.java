package fr.frogdevelopment.nihongo.dico.downloads;

import android.content.ContentValues;
import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;

import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;

class DicoDownLoadTask extends AbstractDownLoadTask {

	static final String PREFERENCES_NAME = "entries_saved";

	private static final String fileName = "entries_%s.txt";

	private int key = 0;

	DicoDownLoadTask(Context context, String language, DownloadListener listener) {
		super(context, String.format(fileName, language), NihonGoDicoContentProvider.URI_WORD, PREFERENCES_NAME,listener);
	}

	@Override
	protected void loopOnLines(HttpURLConnection connection) throws IOException {
		super.loopOnLines(connection);
		mContext.getContentResolver().update(NihonGoDicoContentProvider.URI_REBUILD_DICO, null, null, null);
	}

	@Override
	protected void fillValues(String currentLine) {

		// fixme delete first line with total lines
		if (key++ == 0) {
			return;
		}

		String[] values = currentLine.split("\\|", 3);

		ContentValues entryValues = new ContentValues();
		entryValues.put("tag", "entry");
		entryValues.put("key", key);
		entryValues.put(EntryContract.KANJI, values[0]);
		entryValues.put(EntryContract.READING, values[1]);

		mValueList.add(entryValues);

		String[] senses = values[2].split("\\|");
		for (String sense : senses) {
			String[] values2 = sense.split("/");
			if (values2.length == 6) { // fixme voir pourquoi
				ContentValues senseValues = new ContentValues();
				senseValues.put("tag", "sense");
				senseValues.put("key", key);
				senseValues.put(SenseContract.POS, values2[0].replaceAll(";", ", "));
				senseValues.put(SenseContract.FIELD, values2[1].replaceAll(";", ", "));
				senseValues.put(SenseContract.MISC, values2[2].replaceAll(";", ", "));
				senseValues.put(SenseContract.INFO, values2[3]);
				senseValues.put(SenseContract.DIAL, values2[4].replaceAll(";", ", "));
				senseValues.put(SenseContract.GLOSS, values2[5].replaceAll(";", ", "));

				mValueList.add(senseValues);
			}
		}
	}
}
