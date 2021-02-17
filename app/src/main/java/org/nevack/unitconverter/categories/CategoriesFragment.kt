package org.nevack.unitconverter.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterActivity
import org.nevack.unitconverter.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentCategoriesBinding.bind(view)
        with(binding.recycler) {
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.column_count))
            setHasFixedSize(true)
            adapter = CategoriesAdapter {
                startActivity(ConverterActivity.getIntent(context, it.index))
            }
        }
    }
}