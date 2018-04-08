package org.nevack.unitconverter.history;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.nevack.unitconverter.model.EUnitCategory;

import java.util.ArrayList;
import java.util.List;

public class HistoryFilterDialog extends DialogFragment {

    public int mask = 0;
    private DialogInterface.OnDismissListener listener;

    public void setListener(DialogInterface.OnDismissListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        List<String> names = new ArrayList<>();
        names.add("None");
        for (EUnitCategory category : EUnitCategory.values()){
            names.add(getString(category.getName()));
        }

        builder.setOnDismissListener(listener);

        builder.setItems(names.toArray(new String[names.size()]), (dialog, which) -> mask = which - 1);

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
