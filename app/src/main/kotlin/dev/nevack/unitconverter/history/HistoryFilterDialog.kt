package dev.nevack.unitconverter.history

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.nevack.unitconverter.model.AppConverterCategory

typealias Listener = (String?) -> Unit

class HistoryFilterDialog(
    private val categories: List<AppConverterCategory>,
    private val listener: Listener,
) : DialogFragment() {
    private var categoryId: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val names: MutableList<String> = mutableListOf("None")
        categories.mapTo(names) { getString(it.categoryName) }
        return MaterialAlertDialogBuilder(requireContext())
            .setItems(names.toTypedArray()) { _, which -> categoryId = categories.getOrNull(which - 1)?.id }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener(categoryId)
    }
}
