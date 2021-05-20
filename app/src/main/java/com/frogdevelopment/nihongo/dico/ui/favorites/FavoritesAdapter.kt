package com.frogdevelopment.nihongo.dico.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frogdevelopment.nihongo.dico.data.entities.EntryDetails
import com.frogdevelopment.nihongo.dico.databinding.SearchRowBinding
import org.apache.commons.lang3.StringUtils

class FavoritesAdapter(
    context: Context,
    private val listener: OnEntryClickListener,
    private val entries: List<EntryDetails>
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    @FunctionalInterface
    interface OnEntryClickListener {
        fun onEntryClick(entryDetails: EntryDetails)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchRowBinding.inflate(mInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.binding.header.text = entry.kanji
        if (StringUtils.isBlank(entry.kanji)) {
            holder.binding.header.visibility = View.GONE
        } else {
            holder.binding.header.visibility = View.VISIBLE
        }
        holder.binding.subhead.text = entry.kana
        holder.binding.vocabulary.text = entry.gloss
        holder.itemView.setOnClickListener { listener.onEntryClick(entry) }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class ViewHolder(val binding: SearchRowBinding) : RecyclerView.ViewHolder(binding.root)

}