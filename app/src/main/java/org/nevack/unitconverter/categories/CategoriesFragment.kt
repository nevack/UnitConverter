package org.nevack.unitconverter.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterActivity
import org.nevack.unitconverter.databinding.CategoryItemBinding
import org.nevack.unitconverter.databinding.FragmentCategoriesBinding
import org.nevack.unitconverter.model.ConverterCategory

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentCategoriesBinding.bind(view)
        with(binding.recycler) {
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.column_count))
            setHasFixedSize(true)
            adapter = CategoriesAdapter {
                startActivity(ConverterActivity.getIntent(context, it.ordinal))
            }
        }
    }

    private inner class CategoriesAdapter(
        private val listener: (ConverterCategory) -> Unit,
    ) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
        private val unitCategories = listOf(*ConverterCategory.values())

        inner class ViewHolder(
            private val binding: CategoryItemBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(category: ConverterCategory) = with(binding) {
                categoryName.setText(category.categoryName)
                categoryIcon.setImageResource(category.icon)
                categoryContainer.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), category.color))
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val binding = CategoryItemBinding.inflate(LayoutInflater.from(viewGroup.context))
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
}