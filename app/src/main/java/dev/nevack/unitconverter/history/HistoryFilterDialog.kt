package dev.nevack.unitconverter.history

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.nevack.unitconverter.model.Categories

typealias Listener = ((Int) -> Unit)

class HistoryFilterDialog(private val listener: Listener) : DialogFragment() {
    private var mask = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val names: MutableList<String> = mutableListOf("None")
        Categories.mapTo(names) { getString(it.categoryName) }
        return MaterialAlertDialogBuilder(requireContext())
            .setItems(names.toTypedArray()) { _, which -> mask = which - 1 }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mask < 0) {
            listener(0)
        } else {
            listener(1 shl mask)
        }
    }
}
