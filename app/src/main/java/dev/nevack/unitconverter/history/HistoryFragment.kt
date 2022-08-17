package dev.nevack.unitconverter.history

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
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.FragmentHistoryBinding
import dev.nevack.unitconverter.history.db.HistoryItem

class HistoryFragment : Fragment(R.layout.fragment_history), MenuProvider {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        binding.root.applyInsetter {
            type(navigationBars = true) { padding(horizontal = true) }
        }

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            val animation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.layout_animation_fall_down
            )
            layoutAnimation = animation

            applyInsetter {
                type(navigationBars = true) { padding(vertical = true) }
            }
        }
        adapter = HistoryAdapter(remove = viewModel::removeItem, share = ::shareItem).also {
            binding.recycler.adapter = it
        }

        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.recycler.isInvisible = it.isEmpty()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_history, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
        R.id.delete -> {
            viewModel.removeAll()
            true
        }
        R.id.filter -> {
            HistoryFilterDialog(viewModel::filter).show(childFragmentManager, "dialog")
            true
        }
        else -> false
    }

    private fun shareItem(item: HistoryItem) {
        val message = "${item.valueFrom} ${item.unitFrom} = ${item.valueTo} ${item.unitTo}"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        requireContext().startActivity(Intent.createChooser(sendIntent, message))
    }
}
