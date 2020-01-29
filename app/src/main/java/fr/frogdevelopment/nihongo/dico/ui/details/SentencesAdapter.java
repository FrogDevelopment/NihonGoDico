package fr.frogdevelopment.nihongo.dico.ui.details;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence;
import fr.frogdevelopment.nihongo.dico.databinding.SentenceRowBinding;

public class SentencesAdapter extends RecyclerView.Adapter<SentencesAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Typeface kanjiFont;
    private final List<Sentence> mSentences;

    public SentencesAdapter(Context context, List<Sentence> sentences) {
        mInflater = LayoutInflater.from(context);
        mSentences = sentences;
        this.kanjiFont = ResourcesCompat.getFont(context, R.font.sawarabi_mincho);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SentenceRowBinding binding = SentenceRowBinding.inflate(mInflater, parent, false);
        binding.japanese.setTypeface(kanjiFont);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setSentence(mSentences.get(position));
    }

    @Override
    public int getItemCount() {
        return mSentences.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final SentenceRowBinding binding;

        public ViewHolder(SentenceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
