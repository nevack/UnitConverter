package org.nevack.unitconverter.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.converter.ConverterContract.ConvertData;
import org.nevack.unitconverter.history.HistoryActivity;
import org.nevack.unitconverter.model.Unit;

import java.util.List;

public class ConverterFragment extends Fragment implements ConverterContract.View{

    private KeypadView keypadView;
    private ConverterDisplayView displayView;
    private View background;

    private ConverterContract.Presenter presenter;

    public ConverterFragment() {
        // Requires empty public constructor
    }

    public static ConverterFragment newInstance() {
        return new ConverterFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull ConverterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_converter, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        keypadView = root.findViewById(R.id.keypad);
        displayView = root.findViewById(R.id.display);

        displayView.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                presenter.convert(displayView.getConvertData());
            }
        });

        displayView.setSpinnersCallback(() -> presenter.convert(displayView.getConvertData()));

        background = root.findViewById(R.id.background);

        keypadView.setOnCopyListeners(
                v -> {
                    presenter.copyResultToClipboard(displayView.getCopyResult(false));
                    Snackbar.make(background,
                            R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
                },
                v -> {
                    presenter.copyResultToClipboard(displayView.getCopyResult(true));
                    Snackbar.make(background,
                            R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
        );

        keypadView.setBackspaceListeners(
                v -> displayView.removeLastDigit(),
                v -> {
                    displayView.erase();
                    return true;
                }
        );

        keypadView.setOnPasteListener(v -> presenter.pasteFromClipboard());

        keypadView.setNumericListener(v -> displayView.appendText(((Button) v).getText().toString()));

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                startActivity(new Intent(getContext(), HistoryActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
        background.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }
}
