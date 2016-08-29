package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.entities.Preview;

public class ResearchByGlossAdapter extends DicoAdapter {

    public ResearchByGlossAdapter(Activity context, List<Preview> items) {
        super(context, items);
    }


    protected void handleSecondLine(TextView textview, Preview item) {
        SpannableStringBuilder str = new SpannableStringBuilder(item.gloss);
        for (Pair<Integer, Integer> indices : item.matchIndices) {
            spanMatchRegion(str, indices.getLeft(), indices.getRight());
        }
        textview.setText(str);
    }
}
