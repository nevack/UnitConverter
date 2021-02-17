package org.nevack.unitconverter.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.nevack.unitconverter.databinding.CategoryItemBinding
import org.nevack.unitconverter.model.ConverterCategory

class ViewHolder(
    private val binding: CategoryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: ConverterCategory) = with(binding) {
        categoryName.setText(category.categoryName)
        categoryIcon.setImageResource(category.icon)
        categoryContainer.setBackgroundColor(
            ContextCompat.getColor(itemView.context, category.color))
    }
}

class CategoriesAdapter(
    private val listener: (ConverterCategory) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {
    private val unitCategories = listOf(*ConverterCategory.values())

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener(unitCategories[position])
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(unitCategories[holder.bindingAdapterPosition])
    }

    override fun getItemCount(): Int = unitCategories.size
}