package org.nevack.unitconverter;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.Unit;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterActivity extends AppCompatActivity {

    private static final String CATEGORY_ID = "category_id";
    private static final String COPY_RESULT = "converter_result";
    private static final String ID_EXTRA = "CONVERTERID";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private Spinner sourceSpinner;
    private EditText sourceEditText;
    private TextView sourceTextView;
    private EditText resultEditText;
    private TextView resultTextView;
    private Spinner resultSpinner;

    private KeypadView keypadView;

    private Converter converter;

    private int converterId;

    public static Intent getIntent(Context context, int converterId) {
        Intent intent = new Intent(context, ConverterActivity.class);
        intent.putExtra(ID_EXTRA, converterId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        converterId = getIntent().getIntExtra(ID_EXTRA, 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < EUnitCategory.values().length; i++) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(EUnitCategory.values()[i].getName()));
            menu.getItem(i).setIcon(EUnitCategory.values()[i].getIcon());
        }
        menu.getItem(converterId).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menu.getItem(converterId).setChecked(false);
                menuItem.setChecked(true);
                converterId = menuItem.getOrder();
                drawerLayout.closeDrawers();
                setConverter();
                return true;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        sourceSpinner = (Spinner) findViewById(R.id.sourcespinner);
        sourceSpinner.setOnItemSelectedListener(new SpinnerListener());

        resultSpinner = (Spinner) findViewById(R.id.resultspinner);
        resultSpinner.setOnItemSelectedListener(new SpinnerListener());

        sourceEditText = (EditText) findViewById(R.id.sourcevalue);
        sourceEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (sourceEditText.getText().toString().equals(".")) {
                    sourceEditText.setText("0.");
                    sourceEditText.setSelection(sourceEditText.getText().toString().length());
                }

                if (sourceEditText.getText().toString().equals("-.")) {
                    sourceEditText.setText("-0.");
                    sourceEditText.setSelection(sourceEditText.getText().toString().length());
                }

                if (!sourceEditText.getText().toString().equals("-")) convert();
            }
        });
        resultEditText = (EditText) findViewById(R.id.resultvalue);

        sourceTextView = (TextView) findViewById(R.id.sourcevaluesymbol);
        resultTextView = (TextView) findViewById(R.id.resultvaluesymbol);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = sourceEditText.getText().toString();
                sourceEditText.setText(resultEditText.getText().toString());
                resultEditText.setText(temp);
                int position = sourceSpinner.getSelectedItemPosition();
                sourceSpinner.setSelection(resultSpinner.getSelectedItemPosition());
                resultSpinner.setSelection(position);
                convert();
            }
        });

        sourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { sourceSpinner.performClick(); }
        });

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { resultSpinner.performClick(); }
        });

        keypadView = (KeypadView) findViewById(R.id.keypad);
        keypadView.setEditText(sourceEditText);

        keypadView.setCopyListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyResultToClipboard(false);
                    }
                },
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        copyResultToClipboard(true);
                        return true;
                    }
                }
        );

        keypadView.setBackspaceLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    final View myView = findViewById(R.id.reveal_container);

                    int cx = myView.getWidth();
                    int cy = myView.getHeight() / 2;

                    float finalRadius = (float) Math.hypot(cx, cy);

                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

                    myView.setVisibility(View.VISIBLE);
                    anim.start();
                }

                sourceEditText.setText("");
                resultEditText.setText("");
                return true;
            }
        });

        setConverter();
    }

    private void setConverter() {
        new InitDataAsync().execute();
    }

    private void convert() {
        if (sourceEditText.getText().toString().equals("")) resultEditText.setText("");
        else resultEditText.setText(converter.convert(sourceEditText.getText().toString(),
                sourceSpinner.getSelectedItemPosition(),
                resultSpinner.getSelectedItemPosition()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CATEGORY_ID, converterId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        converterId = savedInstanceState.getInt(CATEGORY_ID);
        setConverter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }

    private void copyResultToClipboard(boolean withUnitSymbol) {
        String textToCopy = "";
        if (!resultEditText.getText().toString().equals("")) {
            textToCopy = resultEditText.getText().toString();
        }
        if (withUnitSymbol)
        {
            textToCopy += resultTextView.getText().toString();
        }
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, textToCopy));
        Snackbar.make(keypadView, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            sourceTextView.setText(Html.fromHtml(converter.getUnitSymbol(sourceSpinner.getSelectedItemPosition())));
            resultTextView.setText(Html.fromHtml(converter.getUnitSymbol(resultSpinner.getSelectedItemPosition())));
            convert();
        }

        @Override public void onNothingSelected(AdapterView<?> parent) { convert(); }
    }

    private class InitDataAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sourceEditText.setText("");
            resultEditText.setText("");

            mDialog = new ProgressDialog(ConverterActivity.this);
            mDialog.setMessage(getString(R.string.loading_data));
            mDialog.setCancelable(false);
            if (converterId == EUnitCategory.CURRENCY.ordinal()) mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setTitle(converter.getTitle());

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(ConverterActivity.this, android.R.layout.simple_list_item_1);
            for (Unit unit : converter.getUnits()) {
                adapter.add(unit.getName());
            }

            sourceSpinner.setAdapter(adapter);
            resultSpinner.setAdapter(adapter);
            Spanned symbol = Html.fromHtml(converter.getUnitSymbol(0));
            sourceTextView.setText(symbol);
            resultTextView.setText(symbol);

            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            converter = EUnitCategory.values()[converterId].getConverter(ConverterActivity.this);
            return null;
        }
    }
}
