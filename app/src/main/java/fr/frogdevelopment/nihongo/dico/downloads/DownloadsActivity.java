package fr.frogdevelopment.nihongo.dico.downloads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.frogdevelopment.nihongo.dico.R;

// fixme test connection available
public class DownloadsActivity extends Activity {

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

        boolean entries_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DicoDownLoadTask.PREFERENCES_NAME, false);
        mDico.setEnabled(!entries_saved);

        boolean examples_saved = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(ExampleDownLoadTask.PREFERENCES_NAME, false);
        mExample.setEnabled(!examples_saved);
    }

    private void warningBigFile(DialogInterface.OnClickListener onClickListener) {
        // todo force WiFi ?
        new AlertDialog.Builder(this)
                .setMessage("Big File. Use WiFi. Continue ?")
                .setPositiveButton(android.R.string.ok, onClickListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @OnClick(R.id.download_dico)
    void onClickDico() {
        // fixme select language
        String language = "fre";
        warningBigFile((dialog, id) -> new DicoDownLoadTask(this, language).execute());
    }

    @OnClick(R.id.download_examples)
    void onClickExamples() {
        // fixme select language
        String language = "fra";
        warningBigFile((dialog, id) -> new ExampleDownLoadTask(this, language).execute());
    }

}
