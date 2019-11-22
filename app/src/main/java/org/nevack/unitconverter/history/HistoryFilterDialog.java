package org.nevack.unitconverter.history;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import org.nevack.unitconverter.model.EUnitCategory;

import java.util.ArrayList;
import java.util.List;

public class HistoryFilterDialog extends DialogFragment {



    public interface Listener {
        void OnResult(int result);
    }

    private Listener listener;
    private int mask = 0;

    public void setListener(Listener listener) {
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


        builder.setItems(names.toArray(new String[0]), (dialog, which) -> mask = which - 1);
        builder.setOnDismissListener(dialog -> listener.OnResult(mask));

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
