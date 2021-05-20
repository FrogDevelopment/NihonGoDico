package com.frogdevelopment.nihongo.dico.ui.search

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.frogdevelopment.nihongo.dico.R
import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch
import com.frogdevelopment.nihongo.dico.databinding.SearchRowBinding
import org.apache.commons.lang3.StringUtils
import java.util.*

class ResultAdapter(
    context: Context,
    private val listener: OnEntryClickListener,
    private val entries: List<EntrySearch>
) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    @FunctionalInterface
    interface OnEntryClickListener {
        fun onEntryClick(senseSeq: String)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val colorMatch: Int = ResourcesCompat.getColor(context.resources, R.color.primary, null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchRowBinding.inflate(mInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        handleHeader(holder, entry)
        handleVocabulary(holder, entry)
        holder.itemView.setOnClickListener { listener.onEntryClick(entry.senseSeq) }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    private fun handleHeader(holder: ViewHolder, entry: EntrySearch) {
        if (StringUtils.isNotBlank(entry.kanji)) {
            handleKanjiAndKana(holder, entry)
        } else {
            holder.binding.header.text = null
            holder.binding.header.visibility = View.GONE
            handleKanaOnly(holder, entry)
        }
    }

    private fun handleKanjiAndKana(holder: ViewHolder, entry: EntrySearch) {
        if (entry.kanjiSpannable == null) {
            entry.kanjiSpannable = handleMatches(entry.kanji!!)
        }
        holder.binding.header.text = entry.kanjiSpannable
        holder.binding.header.visibility = View.VISIBLE
        handleKanaOnly(holder, entry)
    }

    private fun handleKanaOnly(holder: ViewHolder, entry: EntrySearch) {
        if (entry.kanaSpannable == null) {
            entry.kanaSpannable = handleMatches(entry.kana)
        }
        holder.binding.subhead.text = entry.kanaSpannable

        if (entry.favorite) {
            holder.binding.favorite.visibility = View.VISIBLE
        } else {
            holder.binding.favorite.visibility = View.INVISIBLE
        }
    }

    private fun handleVocabulary(holder: ViewHolder, entry: EntrySearch) {
        if (entry.vocabularySpannable == null) {
            entry.vocabularySpannable = handleMatches(entry.vocabulary)
        }
        holder.binding.vocabulary.text = entry.vocabularySpannable
    }

    private fun handleMatches(value: String): SpannableString {
        var sb = StringBuilder(value)
        val matches: MutableList<Pair<Int, Int>> = ArrayList()
        var start = sb.indexOf(START_SPAN)
        while (start >= 0) {
            sb = sb.delete(start, start + START_SPAN.length)
            val end = sb.indexOf(END_SPAN)
            sb = sb.delete(end, end + END_SPAN.length)
            matches.add(Pair.create(start, end))
            start = sb.indexOf(START_SPAN)
        }
        val spannableString = SpannableString(sb)
        for (match in matches) {
            spannableString.setSpan(StyleSpan(Typeface.BOLD), match.first, match.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(colorMatch), match.first, match.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    class ViewHolder(val binding: SearchRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val START_SPAN = "<span class=\"keyword\">"
        private const val END_SPAN = "</span>"
    }

}