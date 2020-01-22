package fr.frogdevelopment.nihongo.dico.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.search.Entry;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class EntriesAdapter extends ArrayAdapter<Entry> {

    private final LayoutInflater mInflater;

    private static final String startSpan = "<span class=\"keyword\">";

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Entry entry = getItem(position);

        handleFirstLine(holder.firstLine, entry);
        handleSecondLine(holder.secondLine, entry);

        return convertView;
    }

    private static final String endSpan = "</span>";

    EntriesAdapter(Context context, List<Entry> entries) {
        super(context, 0, entries);

        mInflater = LayoutInflater.from(context);
    }

    protected void handleFirstLine(TextView textview, Entry entry) {
        if (StringUtils.isBlank(entry.kanji)) {
            textview.setText(entry.kana);
        } else {
            String pre = entry.kanji + " - ";
            int start = pre.length();

            String text = pre + entry.kana;
            int end = text.length();

            SpannableStringBuilder str = new SpannableStringBuilder(text);
            spanKanjiKana(str, start, end);
            textview.setText(str);
        }
    }

    protected void handleSecondLine(TextView textview, Entry entry) {
        if (entry.vocabularySpannable == null) {
            SpannableStringBuilder vocabularySpannable;

            StringBuilder sb = new StringBuilder(entry.vocabulary);
            int start = sb.indexOf(startSpan);
            if (start >= 0) {
                sb = sb.delete(start, start + startSpan.length());
                int end = sb.indexOf(endSpan);
                sb = sb.delete(end, end + endSpan.length());

                vocabularySpannable = new SpannableStringBuilder(sb);
                spanMatchRegion(vocabularySpannable, start, end);
            } else {
                vocabularySpannable = new SpannableStringBuilder(entry.vocabulary);
            }

            entry.vocabularySpannable = vocabularySpannable;
        }

        textview.setText(entry.vocabularySpannable);
    }

    void spanKanjiKana(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new RelativeSizeSpan(0.7f), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    void spanMatchRegion(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new StyleSpan(Typeface.BOLD), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.RED), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static class ViewHolder {

        private final TextView firstLine;
        private final TextView secondLine;

        private ViewHolder(View view) {
            firstLine = view.findViewById(R.id.row_first_line);
            secondLine = view.findViewById(R.id.row_second_line);
        }
    }
}
