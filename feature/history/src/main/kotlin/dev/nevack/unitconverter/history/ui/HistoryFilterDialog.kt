package dev.nevack.unitconverter.history.ui

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.nevack.unitconverter.history.HistoryCategory

class HistoryFilterDialog : DialogFragment() {
    private var selectedCategoryId: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        selectedCategoryId = savedInstanceState?.getString(KEY_SELECTED_ID)

        val ids = requireArguments().getStringArray(ARG_IDS)!!
        val nameRes = requireArguments().getIntArray(ARG_NAME_RES)!!

        val names = Array(ids.size + 1) { i ->
            if (i == 0) getString(android.R.string.cancel).let { "None" } else getString(nameRes[i - 1])
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setItems(names) { _, which ->
                selectedCategoryId = ids.getOrNull(which - 1)
            }
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_ID, selectedCategoryId)
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(KEY_SELECTED_ID to selectedCategoryId),
        )
    }

    companion object {
        const val REQUEST_KEY = "HistoryFilterDialog"
        const val KEY_SELECTED_ID = "selected_category_id"

        private const val ARG_IDS = "category_ids"
        private const val ARG_NAME_RES = "category_name_res"

        fun newInstance(categories: List<HistoryCategory>): HistoryFilterDialog =
            HistoryFilterDialog().apply {
                arguments =
                    bundleOf(
                        ARG_IDS to categories.map { it.id }.toTypedArray(),
                        ARG_NAME_RES to categories.map { it.nameRes }.toIntArray(),
                    )
            }
    }
}
