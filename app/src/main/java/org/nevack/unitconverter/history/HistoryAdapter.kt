package org.nevack.unitconverter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.nevack.unitconverter.databinding.HistoryItemBinding
import org.nevack.unitconverter.history.db.HistoryItem
import org.nevack.unitconverter.model.EUnitCategory

internal class Differ: DiffUtil.ItemCallback<HistoryItem>() {
    override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
        return newItem == oldItem
    }
}

internal class HistoryAdapter constructor(
    private val remove: (HistoryItem) -> Unit,
    private val share: (HistoryItem) -> Unit
) : ListAdapter<HistoryItem, ViewHolder>(Differ()) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HistoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = ViewHolder(binding)
        holder.binding.removeItem.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                remove(getItem(position))
            }
        }
        holder.binding.shareItem.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                share(getItem(position))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(holder.bindingAdapterPosition)
        val category = EUnitCategory.values()[item.category!!]
        holder.bind(category, item)
    }
}

internal class ViewHolder constructor(val binding: HistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(category: EUnitCategory, item: HistoryItem) {
        binding.categoryName.setText(category.getName())
        binding.valueFrom.text = item.valueFrom
        binding.valueTo.text = item.valueTo
        binding.unitFrom.text = item.unitFrom
        binding.unitTo.text = item.unitTo
        itemView.setBackgroundColor(
            ContextCompat.getColor(itemView.context, category.color)
        )
        binding.categoryIcon.setImageDrawable(
            AppCompatResources.getDrawable(itemView.context, category.icon)
        )
    }
}