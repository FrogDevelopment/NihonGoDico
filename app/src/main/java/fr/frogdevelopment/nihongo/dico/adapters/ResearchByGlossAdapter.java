package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
            str.setSpan(new StyleSpan(Typeface.BOLD), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.RED), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textview.setText(str);
    }
}
