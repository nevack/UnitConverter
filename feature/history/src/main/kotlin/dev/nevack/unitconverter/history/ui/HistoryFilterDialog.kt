package dev.nevack.unitconverter.history.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.nevack.unitconverter.history.HistoryCategory

typealias Listener = (String?) -> Unit

class HistoryFilterDialog(
    private val categories: List<HistoryCategory>,
    private val listener: Listener,
) : DialogFragment() {
    private var categoryId: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val names: MutableList<String> = mutableListOf("None")
        categories.mapTo(names) { getString(it.nameRes) }
        return MaterialAlertDialogBuilder(requireContext())
            .setItems(names.toTypedArray()) { _, which -> categoryId = categories.getOrNull(which - 1)?.id }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener(categoryId)
    }
}
