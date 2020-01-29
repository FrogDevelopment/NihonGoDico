package fr.frogdevelopment.nihongo.dico.ui.search;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import fr.frogdevelopment.nihongo.dico.data.rest.Entry;
import fr.frogdevelopment.nihongo.dico.ui.main.CustomTypefaceSpan;

import static android.graphics.Typeface.BOLD;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    @FunctionalInterface
    public interface OnEntryClickListener {
        void onEntryClick(String senseSeq);
    }

    private static final String START_SPAN = "<span class=\"keyword\">";
    private static final String END_SPAN = "</span>";

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
        View view = mInflater.inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entry entry = entries.get(position);

        handleHeader(holder, entry);
        handleVocabulary(holder, entry);

        holder.itemView.setOnClickListener(v -> listener.onEntryClick(entry.senseSeq));
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

    protected void handleHeader(ViewHolder holder, Entry entry) {
        if (StringUtils.isNotBlank(entry.kanji)) {
            handleKanjiAndKana(holder, entry);
        } else {
            handleKanaOnly(holder.header, entry);
        }
    }

    private void handleKanjiAndKana(ViewHolder holder, Entry entry) {
        if (entry.kanjiSpannable == null) {
            entry.kanjiSpannable = handleMatches(entry.kanji);
            entry.kanjiSpannable.setSpan(new CustomTypefaceSpan(kanjiFont), 0, entry.kanjiSpannable.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.header.setText(entry.kanjiSpannable);

        handleKanaOnly(holder.subhead, entry);
    }

    private void handleKanaOnly(TextView textView, Entry entry) {
        if (entry.kanaSpannable == null) {
            entry.kanaSpannable = handleMatches(entry.kana);
            entry.kanaSpannable.setSpan(new CustomTypefaceSpan(kanaFont), 0, entry.kanaSpannable.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(entry.kanaSpannable);
    }

    protected void handleVocabulary(ViewHolder holder, Entry entry) {
        if (entry.vocabularySpannable == null) {
            entry.vocabularySpannable = handleMatches(entry.vocabulary);
        }

        holder.vocabulary.setText(entry.vocabularySpannable);
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

        private final TextView header;
        private final TextView subhead;
        private final TextView vocabulary;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.row_header);
            subhead = itemView.findViewById(R.id.row_subhead);
            vocabulary = itemView.findViewById(R.id.row_vocabulary);
        }
    }
}
