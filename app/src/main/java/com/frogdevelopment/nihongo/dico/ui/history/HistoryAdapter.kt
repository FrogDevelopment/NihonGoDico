package com.frogdevelopment.nihongo.dico.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frogdevelopment.nihongo.dico.databinding.HistoryRowBinding

class HistoryAdapter(
    context: Context,
    private val items: List<History>,
    private val listener: OnHistoryClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    @FunctionalInterface
    interface OnHistoryClickListener {
        fun onHistoryClick(text: String)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryRowBinding.inflate(mInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.historyDate.text = item.date
        holder.binding.historyText.text = item.text
        holder.binding.historyResult.text = item.result
        holder.itemView.setOnClickListener { listener.onHistoryClick(item.text) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: HistoryRowBinding) : RecyclerView.ViewHolder(binding.root)

}