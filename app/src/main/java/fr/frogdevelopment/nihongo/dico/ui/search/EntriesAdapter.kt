package fr.frogdevelopment.nihongo.dico.ui.search

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import fr.frogdevelopment.nihongo.dico.R
import fr.frogdevelopment.nihongo.dico.data.Entry
import fr.frogdevelopment.nihongo.dico.databinding.SearchRowBinding
import fr.frogdevelopment.nihongo.dico.ui.main.CustomTypefaceSpan
import org.apache.commons.lang3.StringUtils
import java.util.*

class EntriesAdapter(context: Context, private val listener: OnEntryClickListener, entries: List<Entry>) : RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {

    @FunctionalInterface
    interface OnEntryClickListener {
        fun onEntryClick(senseSeq: String)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mEntries: List<Entry> = entries
    private val kanjiFont: Typeface? = ResourcesCompat.getFont(context, R.font.sawarabi_mincho)
    private val kanaFont: Typeface? = ResourcesCompat.getFont(context, R.font.sawarabi_gothic)
    private val colorMatch: Int = ResourcesCompat.getColor(context.resources, R.color.primary, null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchRowBinding.inflate(mInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = mEntries[position]
        handleHeader(holder, entry)
        handleVocabulary(holder, entry)
        holder.itemView.setOnClickListener { listener.onEntryClick(entry.senseSeq!!) }
    }

    override fun getItemCount(): Int {
        return mEntries.size
    }

    private fun handleHeader(holder: ViewHolder, entry: Entry) {
        if (StringUtils.isNotBlank(entry.kanji)) {
            handleKanjiAndKana(holder, entry)
        } else {
            handleKanaOnly(holder.binding.header, entry)
        }
    }

    private fun handleKanjiAndKana(holder: ViewHolder, entry: Entry) {
        if (entry.kanjiSpannable == null) {
            entry.kanjiSpannable = handleMatches(entry.kanji!!)
            entry.kanjiSpannable!!.setSpan(CustomTypefaceSpan(kanjiFont!!), 0, entry.kanjiSpannable!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.header.text = entry.kanjiSpannable
        handleKanaOnly(holder.binding.subhead, entry)
    }

    private fun handleKanaOnly(textView: TextView, entry: Entry) {
        if (entry.kanaSpannable == null) {
            entry.kanaSpannable = handleMatches(entry.kana!!)
            entry.kanaSpannable!!.setSpan(CustomTypefaceSpan(kanaFont!!), 0, entry.kanaSpannable!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textView.text = entry.kanaSpannable
    }

    private fun handleVocabulary(holder: ViewHolder, entry: Entry) {
        if (entry.vocabularySpannable == null) {
            entry.vocabularySpannable = handleMatches(entry.vocabulary!!)
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