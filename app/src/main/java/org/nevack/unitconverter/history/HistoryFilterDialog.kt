package org.nevack.unitconverter.history

import android.os.Bundle
import android.app.Dialog
import java.util.ArrayList
import org.nevack.unitconverter.model.EUnitCategory
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

typealias Listener = ((Int) -> Unit)

class HistoryFilterDialog : DialogFragment() {
    private var listener: Listener? = null
    private var mask = 0
    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val names: MutableList<String> = mutableListOf("None")
        EUnitCategory.values().mapTo(names) { getString(it.getName()) }
        builder.setItems(names.toTypedArray()) { _: DialogInterface?, which: Int ->
            mask = which - 1
        }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.invoke(mask)
    }
}