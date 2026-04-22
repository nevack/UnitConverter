package dev.nevack.unitconverter.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.FragmentCategoriesBinding
import dev.nevack.unitconverter.model.ConverterCatalog
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private val viewModel: CategoriesViewModel by activityViewModels()

    @Inject
    lateinit var catalog: ConverterCatalog

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentCategoriesBinding.bind(view)
        with(binding.recycler) {
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.column_count))
            setHasFixedSize(true)
            adapter = CategoriesAdapter(catalog.categories, viewModel::openConverter)

            applyInsetter { type(navigationBars = true) { padding() } }
        }
    }
}
