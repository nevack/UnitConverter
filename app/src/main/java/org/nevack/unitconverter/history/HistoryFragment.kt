package org.nevack.unitconverter.history

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.FragmentHistoryBinding
import org.nevack.unitconverter.databinding.HistoryItemBinding
import org.nevack.unitconverter.model.EUnitCategory
import org.nevack.unitconverter.model.HistoryItem

class HistoryFragment : Fragment(R.layout.fragment_history), HistoryContract.View {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var presenter: HistoryContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setPresenter(presenter: HistoryContract.Presenter) {
        this.presenter = presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        val animation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.layout_animation_fall_down
        )
        binding.recycler.layoutAnimation = animation
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                presenter.clearItems()
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
        newFragment.setListener { mask: Int -> presenter.filterItems(mask) }
        newFragment.show(transaction, "dialog")
    }

    override fun showHistoryItems(items: List<HistoryItem>) {
        binding.recycler.adapter = HistoryAdapter(items.toMutableList())
        binding.nohistory.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
    }

    override fun showNoItems() {
        binding.recycler.visibility = View.GONE
        binding.nohistory.visibility = View.VISIBLE
    }

    private inner class HistoryAdapter constructor(private var items: MutableList<HistoryItem>) :
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
        private inner class ViewHolder constructor(val binding: HistoryItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(category: EUnitCategory, item: HistoryItem) {
                binding.categoryName.setText(category.getName())
                binding.valueFrom.text = item.valueFrom
                binding.valueTo.text = item.valueTo
                binding.unitFrom.text = item.unitFrom
                binding.unitTo.text = item.unitTo
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, category.color)
                )
                binding.categoryIcon.setImageDrawable(
                    AppCompatResources.getDrawable(itemView.context, category.icon)
                )
            }

        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                HistoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            val holder = ViewHolder(binding)
            holder.binding.removeItem.setOnClickListener {
                val position = holder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    presenter.removeItem(items[position])
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            holder.binding.shareItem.setOnClickListener {
                val position = holder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    presenter.shareItem(items[position])
                }
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[holder.bindingAdapterPosition]
            val category = EUnitCategory.values()[item.category]
            holder.bind(category, item)
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}