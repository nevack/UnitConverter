package dev.nevack.unitconverter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.nevack.unitconverter.feature.history.databinding.HistoryItemBinding

internal class Differ : DiffUtil.ItemCallback<HistoryRecord>() {
    override fun areItemsTheSame(
        oldItem: HistoryRecord,
        newItem: HistoryRecord,
    ): Boolean = newItem.id == oldItem.id

    override fun areContentsTheSame(
        oldItem: HistoryRecord,
        newItem: HistoryRecord,
    ): Boolean = newItem == oldItem
}

internal class HistoryAdapter(
    private val categoriesById: Map<String, HistoryCategory>,
    private val remove: (HistoryRecord) -> Unit,
    private val share: (HistoryRecord) -> Unit,
) : ListAdapter<HistoryRecord, ViewHolder>(Differ()) {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ViewHolder {
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

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(holder.bindingAdapterPosition)
        val category = categoriesById[item.categoryId]
        holder.bind(category, item)
    }
}

internal class ViewHolder(
    val binding: HistoryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        category: HistoryCategory?,
        item: HistoryRecord,
    ) {
        binding.valueFrom.text = item.valueFrom
        binding.valueTo.text = item.valueTo
        binding.unitFrom.text = item.unitFrom
        binding.unitTo.text = item.unitTo
        if (category == null) {
            binding.categoryName.text = item.categoryId
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.transparent))
            binding.categoryIcon.setImageDrawable(null)
            return
        }
        binding.categoryName.setText(category.nameRes)
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, category.colorRes))
        binding.categoryIcon.setImageDrawable(AppCompatResources.getDrawable(itemView.context, category.iconRes))
    }
}
