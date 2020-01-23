package fr.frogdevelopment.nihongo.dico.ui.main;

import android.content.Context;
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

import static android.graphics.Typeface.BOLD;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    @FunctionalInterface
    public interface OnEntryClickListener {
        void onEntryClick(Entry item);
    }

    private static final String START_SPAN = "<span class=\"keyword\">";
    private static final String END_SPAN = "</span>";
    public static final String KANJI_KANA_SEPARATOR = " - ";
    public static final float KANA_RELATIVE_SIZE = 0.8f;

    private final LayoutInflater mInflater;
    private final List<Entry> entries = new ArrayList<>();

    private final Typeface kanjiFont;
    private final Typeface kanaFont;
    private final int colorMatch;
    private final OnEntryClickListener listener;

    public EntriesAdapter(Context context, OnEntryClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;

        this.kanaFont = ResourcesCompat.getFont(context, R.font.sawarabi_gothic);
        this.kanjiFont = ResourcesCompat.getFont(context, R.font.sawarabi_mincho);
        this.colorMatch = ResourcesCompat.getColor(context.getResources(), R.color.primary, null);
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

        holder.itemView.setOnClickListener(v -> listener.onEntryClick(entry));
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

    protected void handleFirstLine(TextView textView, Entry entry) {
        if (StringUtils.isNotBlank(entry.kanji)) {
            handleKanjiAndKana(textView, entry);
        } else {
            handleKanaOnly(textView, entry);
        }
    }

    private void handleKanjiAndKana(TextView textView, Entry entry) {
        if (entry.kanjiSpannable == null) {
            entry.kanjiSpannable = handleMatches(entry.kanji + KANJI_KANA_SEPARATOR + entry.kana);

            String text = entry.kanjiSpannable.toString();

            int kanjiStart = 0;
            int kanjiEnd = text.indexOf(KANJI_KANA_SEPARATOR) - 1;
            int kanaStart = text.indexOf(KANJI_KANA_SEPARATOR) + 1;
            int kanaEnd = text.length();

            entry.kanjiSpannable.setSpan(new CustomTypefaceSpan(kanjiFont), kanjiStart, kanjiEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
            entry.kanjiSpannable.setSpan(new RelativeSizeSpan(KANA_RELATIVE_SIZE), kanaStart, kanaEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
            entry.kanjiSpannable.setSpan(new CustomTypefaceSpan(kanaFont), kanaStart, kanaEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(entry.kanjiSpannable);
    }

    private void handleKanaOnly(TextView textView, Entry entry) {
        if (entry.kanaSpannable == null) {
            entry.kanaSpannable = handleMatches(entry.kana);
            entry.kanaSpannable.setSpan(new CustomTypefaceSpan(kanaFont), 0, entry.kanaSpannable.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(entry.kanaSpannable);
    }

    protected void handleSecondLine(TextView textView, Entry entry) {
        if (entry.vocabularySpannable == null) {
            entry.vocabularySpannable = handleMatches(entry.vocabulary);
        }

        textView.setText(entry.vocabularySpannable);
    }

    private SpannableString handleMatches(String value) {
        StringBuilder sb = new StringBuilder(value);
        List<Pair<Integer, Integer>> matches = new ArrayList<>();

        int start = sb.indexOf(START_SPAN);
        while (start >= 0) {
            sb = sb.delete(start, start + START_SPAN.length());
            int end = sb.indexOf(END_SPAN);
            sb = sb.delete(end, end + END_SPAN.length());

            matches.add(Pair.create(start, end));
            start = sb.indexOf(START_SPAN);
        }

        SpannableString spannableString = new SpannableString(sb);
        for (Pair<Integer, Integer> match : matches) {
            spannableString.setSpan(new StyleSpan(BOLD), match.first, match.second, SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(colorMatch), match.first, match.second, SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
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
