package fr.frogdevelopment.nihongo.dico.to_delete.adapters;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Match;
import fr.frogdevelopment.nihongo.dico.search.Search;

public class ResearchByKanjiAdapter extends DicoAdapter {

    public ResearchByKanjiAdapter(Activity context, List<Search> items) {
        super(context, items);
    }

    @Override
    protected void handleFirstLine(TextView textview, Search item) {
        String pre = TextUtils.isEmpty(item.kanji.value) ? " " : item.kanji.value + " - ";
        int start = pre.length();

        String text = pre + item.kana;
        int end = text.length();
        SpannableStringBuilder str = new SpannableStringBuilder(text);

        spanKanjiKana(str, start, end);

        for (Match match : item.kanji.matches) {
            spanMatchRegion(str, match.start, match.end);
        }

        textview.setText(str);
    }
}
