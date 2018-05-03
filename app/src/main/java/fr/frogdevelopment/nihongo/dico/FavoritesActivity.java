package fr.frogdevelopment.nihongo.dico;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.adapters.DicoAdapter;
import fr.frogdevelopment.nihongo.dico.adapters.ResearchByGlossAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Preview;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_INIT = 0;

	private ProgressBar mProgressBar;
	private ListView mListView;
	private DicoAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		mProgressBar = (ProgressBar) findViewById(R.id.main_progress);

		mListView = (ListView) findViewById(R.id.main_entries);
		mListView.setOnItemClickListener((adapterView, view, i, l) -> onItemClick(i));

		getLoaderManager().initLoader(LOADER_INIT, null, this);
	}

	private void onItemClick(int position) {
//		Preview item = mAdapter.getItem(position);
//		Intent intent = new Intent(this, DetailsActivity.class);
//		intent.putExtra(EntryContract.KANJI, item.kanji);
//		intent.putExtra(EntryContract.READING, item.reading);
//		intent.putExtra(SenseContract.GLOSS, item.gloss);
//		intent.putExtra(SenseContract._ID, item.sense_id);
//
//		startActivity(intent);
//		overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mProgressBar.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.INVISIBLE);

		return new CursorLoader(this, NihonGoDicoContentProvider.URI_FAVORITE, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		if (data.getCount() > 0) {
//			List<Preview> previews = new ArrayList<>();
//			while (data.moveToNext()) {
//				Preview preview = new Preview();
//				preview.kanji = data.getString(0);
//				preview.reading = data.getString(1);
//				preview.gloss = data.getString(2);
//				preview.sense_id = data.getLong(3);
//
//				previews.add(preview);
//			}
//
//			mAdapter = new ResearchByGlossAdapter(this, previews);
//			mListView.setAdapter(mAdapter);
//
//			mListView.setVisibility(View.VISIBLE);
//		} else {
//			Snackbar.make(findViewById(R.id.activity_main), R.string.no_favorites, Snackbar.LENGTH_LONG).show();
//		}
//
//		data.close();
//		getLoaderManager().destroyLoader(LOADER_INIT);
//
//		mProgressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

}
