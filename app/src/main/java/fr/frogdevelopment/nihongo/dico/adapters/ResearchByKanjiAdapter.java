package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.entities.Preview;

public class ResearchByKanjiAdapter extends DicoAdapter {

    public ResearchByKanjiAdapter(Activity context, List<Preview> items) {
        super(context, items);
    }


    protected void handleFirstLine(TextView textview, Preview item) {
        String pre = StringUtils.isBlank(item.kanji) ? " " : item.kanji + " - ";
        int start = pre.length();

        String text = pre + item.reading;
        int end = text.length();
        SpannableStringBuilder str = new SpannableStringBuilder(text);
        for (Pair<Integer, Integer> indices : item.matchIndices) {
            str.setSpan(new StyleSpan(Typeface.BOLD), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.RED), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textview.setText(str);
    }
}
