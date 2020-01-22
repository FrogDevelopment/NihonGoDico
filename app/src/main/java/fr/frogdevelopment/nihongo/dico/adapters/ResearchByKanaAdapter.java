package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.search.Match;
import fr.frogdevelopment.nihongo.dico.search.Search;

public class ResearchByKanaAdapter extends DicoAdapter {

    public ResearchByKanaAdapter(Activity context, List<Search> items) {
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

        for (Match match : item.kana.matches) {
            spanMatchRegion(str, start + match.start, start + match.end);
        }

        textview.setText(str);
    }
}
