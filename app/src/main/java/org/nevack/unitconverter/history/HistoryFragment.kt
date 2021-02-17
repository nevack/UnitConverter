package org.nevack.unitconverter.history

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.FragmentHistoryBinding
import org.nevack.unitconverter.history.db.HistoryItem

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    private val model: HistoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)
        with(binding.recycler) {
            layoutManager = LinearLayoutManager(context)
            val animation = AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.layout_animation_fall_down
            )
            layoutAnimation = animation
        }
        adapter = HistoryAdapter(model::removeItem, ::shareItem)
        binding.recycler.adapter = adapter

        model.items.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showHistoryItems(it)
            } else {
                showNoItems()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                model.removeAll()
                return true
            }
            R.id.filter -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFilterDialog() {
        val manager = parentFragmentManager
        val transaction = manager.beginTransaction()
        val prev = manager.findFragmentByTag("dialog")
        if (prev != null) {
            transaction.remove(prev)
        }
        transaction.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = HistoryFilterDialog()
        newFragment.setListener(model::filter)
        newFragment.show(transaction, "dialog")
    }

    private fun showHistoryItems(items: List<HistoryItem>) {
        adapter.submitList(items)
        binding.nohistory.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
    }

    private fun showNoItems() {
        binding.recycler.visibility = View.GONE
        binding.nohistory.visibility = View.VISIBLE
    }

    private fun shareItem(item: HistoryItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val message = "${item.valueFrom} ${item.unitFrom} = ${item.valueTo} ${item.unitTo}"
        sendIntent.putExtra(Intent.EXTRA_TEXT, message)
        sendIntent.type = "text/plain"
        requireContext().startActivity(sendIntent)
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}
