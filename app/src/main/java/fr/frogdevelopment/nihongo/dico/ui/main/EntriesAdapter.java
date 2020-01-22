package fr.frogdevelopment.nihongo.dico.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.search.Entry;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    private static final String START_SPAN = "<span class=\"keyword\">";
    private static final String END_SPAN = "</span>";

    private final LayoutInflater mInflater;
    private final List<Entry> entries = new ArrayList<>();
    private final Typeface kanjiFont;
    private final Typeface kanaFont;

    public EntriesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        kanaFont = ResourcesCompat.getFont(context, R.font.sawarabi_gothic);
        kanjiFont = ResourcesCompat.getFont(context, R.font.sawarabi_mincho);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = entries.get(position);

        handleFirstLine(holder.firstLine, entry);
        handleSecondLine(holder.secondLine, entry);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setEntries(@NonNull List<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    protected void handleFirstLine(TextView textview, Entry entry) {
        if (StringUtils.isBlank(entry.kanji)) {
            textview.setTypeface(kanaFont);
            textview.setText(entry.kana);
        } else {
            String pre = entry.kanji + " - ";
            int start = pre.length();

            String text = pre + entry.kana;
            int end = text.length();

            SpannableString str = new SpannableString(text);
            spanKanjiKana(str, start, end);

            str.setSpan(new CustomTypefaceSpan(kanjiFont), 0, entry.kanji.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new CustomTypefaceSpan(kanaFont), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
            textview.setText(str);
        }
    }

    private void spanKanjiKana(SpannableString str, int start, int end) {
        str.setSpan(new RelativeSizeSpan(0.7f), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void handleSecondLine(TextView textview, Entry entry) {
        if (entry.vocabularySpannable == null) {
            StringBuilder sb = new StringBuilder(entry.vocabulary);
            List<Pair<Integer, Integer>> matches = new ArrayList<>();

            int start = sb.indexOf(START_SPAN);
            while (start >= 0) {
                sb = sb.delete(start, start + START_SPAN.length());
                int end = sb.indexOf(END_SPAN);
                sb = sb.delete(end, end + END_SPAN.length());

                matches.add(Pair.create(start, end));
                start = sb.indexOf(START_SPAN);
            }

            entry.vocabularySpannable = new SpannableString(sb);
            for (Pair<Integer, Integer> match : matches) {
                spanMatchRegion(entry.vocabularySpannable, match.first, match.second);
            }
        }

        textview.setText(entry.vocabularySpannable);
    }

    private void spanMatchRegion(SpannableString str, int start, int end) {
        str.setSpan(new StyleSpan(Typeface.BOLD), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.RED), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView firstLine;
        private final TextView secondLine;

        public ViewHolder(View itemView) {
            super(itemView);
            firstLine = itemView.findViewById(R.id.row_first_line);
            secondLine = itemView.findViewById(R.id.row_second_line);
        }
    }
}
