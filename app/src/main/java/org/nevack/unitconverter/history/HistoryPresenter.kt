package org.nevack.unitconverter.history

import android.content.Context
import org.nevack.unitconverter.history.db.HistoryDao
import java.util.ArrayList
import android.content.Intent
import kotlinx.coroutines.*
import org.nevack.unitconverter.history.db.HistoryItem

class HistoryPresenter internal constructor(
    private val context: Context,
    private val view: HistoryContract.View,
    private val db: HistoryDao
) : HistoryContract.Presenter {
    private val items: MutableList<HistoryItem> = mutableListOf()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    init {
        view.setPresenter(this)
    }

    override fun start() {
        fetch()
    }

    override fun filterItems(mask: Int) {
        val items: MutableList<HistoryItem> = ArrayList()
        for (item in this.items) {
            if (item.category == mask) items.add(item)
        }
        if (items.isEmpty()) {
            view.showNoItems()
            return
        }
        view.showHistoryItems(items)
    }

    override fun shareItem(item: HistoryItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val message = String.format(
            "%s %s = %s %s",
            item.valueFrom,
            item.unitFrom,
            item.valueTo,
            item.unitTo
        )
        sendIntent.putExtra(Intent.EXTRA_TEXT, message)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun fetch() {
        scope.launch {
            items.clear()
            withContext(Dispatchers.IO) {
                items.addAll(db.getAll())
            }
            if (items.isEmpty()) {
                view.showNoItems()
            } else {
                view.showHistoryItems(items)
            }
        }
    }

    override fun clearItems() {
        //remove all entries
        scope.launch {
            withContext(Dispatchers.IO) {
                db.deleteAll()
            }
            view.showNoItems()
        }
    }

    override fun removeItem(item: HistoryItem) {
        scope.launch(Dispatchers.IO) {
            db.delete(item)
        }
    }
}