package dev.nevack.unitconverter.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.converter.ConverterActivity
import dev.nevack.unitconverter.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentCategoriesBinding.bind(view)
        with(binding.recycler) {
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.column_count))
            setHasFixedSize(true)
            adapter = CategoriesAdapter {
                startActivity(ConverterActivity.getIntent(requireContext(), it))
            }

            applyInsetter {
                type(navigationBars = true) { padding() }
            }
        }
    }
}
