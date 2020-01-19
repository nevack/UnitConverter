package org.nevack.unitconverter.converter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.converter.ConverterContract.ConvertData;
import org.nevack.unitconverter.history.HistoryActivity;
import org.nevack.unitconverter.model.Unit;

import java.util.List;

public class ConverterFragment extends Fragment implements ConverterContract.View {

    private KeypadView keypadView;
    private ConverterDisplayView displayView;
    private View background;

    private ConverterContract.Presenter presenter;

    public ConverterFragment() {
        super(R.layout.fragment_converter);
    }

    public static ConverterFragment newInstance() {
        return new ConverterFragment();
    }

    @Override
    public void setPresenter(@NonNull ConverterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        final AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        keypadView = view.findViewById(R.id.keypad);
        displayView = view.findViewById(R.id.display);

        displayView.setTextWatcher(new TextChangedWatcher(s -> presenter.convert(displayView.getConvertData())));

        displayView.setSpinnersCallback(() -> presenter.convert(displayView.getConvertData()));

        background = view.findViewById(R.id.background);

        keypadView.setOnCopyListeners(
                new KeypadView.ActionListener() {
                    @Override
                    public void longClick() {
                        copyWithNotification(true);
                    }

                    @Override
                    public void singleClick() {
                        copyWithNotification(false);
                    }
                }
        );

        keypadView.setBackspaceListeners(
                new KeypadView.ActionListener() {
                    @Override
                    public void longClick() {
                        displayView.erase();
                    }

                    @Override
                    public void singleClick() {
                        displayView.removeLastDigit();
                    }
                }
        );

        keypadView.setOnPasteListener(v -> presenter.pasteFromClipboard());

        keypadView.setNumericListener(number -> displayView.appendText(String.valueOf(number)));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_converter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                presenter.saveResultToHistory();
                break;
            case R.id.history:
                startActivity(new Intent(requireContext(), HistoryActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(int title) {
        requireActivity().setTitle(title);
    }

    @Override
    public void setUnits(List<Unit> units) {
        displayView.setUnits(units);
    }

    @Override
    public ConvertData getConvertData() {
        return displayView.getConvertData();
    }

    @Override
    public void setConvertData(ConvertData data) {
        displayView.setConvertData(data);
    }

    @Override
    public void appendText(String text) {
        displayView.appendText(text);
    }

    @Override
    public void showResult(String result) {
        displayView.showResult(result);
    }

    @Override
    public void clear() {
        displayView.clear();
    }

    @Override
    public void showError() {
        displayView.showError();
    }

    @Override
    public void setBackgroundColor(int color) {
        background.setBackgroundColor(ContextCompat.getColor(requireContext(), color));
    }

    private void copyWithNotification(boolean shouldIncludeUnit) {
        presenter.copyResultToClipboard(displayView.getCopyResult(shouldIncludeUnit));
        Snackbar.make(background, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
    }

    private static class TextChangedWatcher implements TextWatcher {

        private interface Callback {
            void onTextChanged(String netText);
        }

        private Callback callback;

        TextChangedWatcher(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No-op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No-op
        }

        @Override
        public void afterTextChanged(Editable s) {
            callback.onTextChanged(s.toString());
        }
    }
}
