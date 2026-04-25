package dev.nevack.unitconverter.history.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.feature.history.R
import dev.nevack.unitconverter.feature.history.databinding.FragmentHistoryBinding
import dev.nevack.unitconverter.history.HistoryCategoriesProvider
import dev.nevack.unitconverter.history.HistoryCategory
import dev.nevack.unitconverter.history.HistoryRecord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment :
    Fragment(R.layout.fragment_history),
    MenuProvider {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private var categories: List<HistoryCategory> = emptyList()

    private val viewModel: HistoryViewModel by activityViewModels()

    @Inject
    lateinit var historyCategoriesProvider: HistoryCategoriesProvider

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        binding.root.applyInsetter {
            type(navigationBars = true) { padding(horizontal = true) }
        }
        categories = historyCategoriesProvider()

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            val animation =
                AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    R.anim.layout_animation_fall_down,
                )
            layoutAnimation = animation

            applyInsetter {
                type(navigationBars = true) { padding(vertical = true) }
            }
        }
        adapter =
            HistoryAdapter(
                categoriesById = categories.associateBy { it.id },
                remove = viewModel::removeItem,
                share = ::shareItem,
            ).also {
                binding.recycler.adapter = it
            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    adapter.submitList(it)
                    binding.recycler.isInvisible = it.isEmpty()
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(
        menu: Menu,
        menuInflater: MenuInflater,
    ) {
        menuInflater.inflate(R.menu.menu_history, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.delete -> {
                viewModel.removeAll()
                true
            }

            R.id.filter -> {
                HistoryFilterDialog(categories, viewModel::filter).show(childFragmentManager, "dialog")
                true
            }

            else -> {
                false
            }
        }

    private fun shareItem(item: HistoryRecord) {
        val message = "${item.valueFrom} ${item.unitFrom} = ${item.valueTo} ${item.unitTo}"
        val sendIntent =
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
        requireContext().startActivity(Intent.createChooser(sendIntent, message))
    }
}
