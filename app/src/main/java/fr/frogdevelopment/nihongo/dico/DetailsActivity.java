package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.frogdevelopment.nihongo.dico.adapters.DetailsAdapter;
import fr.frogdevelopment.nihongo.dico.contentprovider.EntryContract;
import fr.frogdevelopment.nihongo.dico.contentprovider.NihonGoDicoContentProvider;
import fr.frogdevelopment.nihongo.dico.contentprovider.SenseContract;
import fr.frogdevelopment.nihongo.dico.entities.Details;

public class DetailsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	@BindView(R.id.details_kanji)
	TextView mKanji;

	@BindView(R.id.details_reading)
	TextView mReading;

	@BindView(R.id.details_senses)
	ListView mSenses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		ButterKnife.bind(this);

		setActionBar(ButterKnife.findById(this, R.id.toolbar));
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle args = getIntent().getExtras();

		mKanji.setText(args.getString(EntryContract.KANJI));
		mReading.setText(args.getString(EntryContract.READING));

		getLoaderManager().initLoader(0, args, this);
	}

	@OnClick(R.id.fab)
	void onFavoriteFabClick(View view) {
		Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] columns = {SenseContract.POS, SenseContract.FIELD, SenseContract.MISC, SenseContract.INFO, SenseContract.DIAL, SenseContract.GLOSS};
		String selection = SenseContract.ENTRY_ID + "=" + args.getLong(SenseContract.ENTRY_ID);

		return new CursorLoader(this, NihonGoDicoContentProvider.URI_WORD, columns, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		List<Details> details = new ArrayList<>();
		while (data.moveToNext()) {
			Details item = new Details();
			item.pos = data.getString(0);
			item.field = data.getString(1);
			item.misc = data.getString(2);
			item.info = data.getString(3);
			item.dial = data.getString(4);
			item.gloss = data.getString(5);

			details.add(item);
		}

		DetailsAdapter adapter = new DetailsAdapter(this, details);

		mSenses.setAdapter(adapter);

		data.close();
		getLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
