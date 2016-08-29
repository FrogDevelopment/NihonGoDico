package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.entities.Preview;

public class ResearchByKanaAdapter extends DicoAdapter {

    public ResearchByKanaAdapter(Activity context, List<Preview> items) {
        super(context, items);
    }


    protected void handleFirstLine(TextView textview, Preview item) {
        String pre = StringUtils.isBlank(item.kanji) ? " " : item.kanji + " - ";
        int start = pre.length();

        String text = pre + item.reading;
        int end = text.length();
        SpannableStringBuilder str = new SpannableStringBuilder(text);
        spanKanjiKana(str, start, end);

        for (Pair<Integer, Integer> indices : item.matchIndices) {
            spanMatchRegion(str, start + indices.getLeft(), start + indices.getRight());
        }

        textview.setText(str);
    }
}
