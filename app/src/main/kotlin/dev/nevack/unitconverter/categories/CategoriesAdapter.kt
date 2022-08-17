package dev.nevack.unitconverter.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.nevack.unitconverter.databinding.CategoryItemBinding
import dev.nevack.unitconverter.model.Categories
import dev.nevack.unitconverter.model.ConverterCategory

internal class ViewHolder(
    private val binding: CategoryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: ConverterCategory) = with(binding) {
        categoryName.setText(category.categoryName)
        categoryIcon.setImageResource(category.icon)
        categoryContainer.setBackgroundColor(
            ContextCompat.getColor(itemView.context, category.color)
        )
    }
}

internal class CategoriesAdapter(
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener(Categories[position].index)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Categories[holder.bindingAdapterPosition])
    }

    override fun getItemCount(): Int = Categories.size
}
