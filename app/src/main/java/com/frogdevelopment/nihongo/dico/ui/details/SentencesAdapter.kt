package com.frogdevelopment.nihongo.dico.ui.details

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.entities.Sentence
import com.frogdevelopment.nihongo.dico.databinding.SentenceRowBinding

class SentencesAdapter(context: Context, sentences: List<Sentence>) : RecyclerView.Adapter<SentencesAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val kanjiFont: Typeface? = ResourcesCompat.getFont(context, R.font.sawarabi_mincho)
    private val mSentences: List<Sentence> = sentences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SentenceRowBinding.inflate(mInflater, parent, false)
        binding.japanese.typeface = kanjiFont
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.sentence = mSentences[position]
    }

    override fun getItemCount(): Int {
        return mSentences.size
    }

    class ViewHolder(val binding: SentenceRowBinding) : RecyclerView.ViewHolder(binding.root)

}