package org.nevack.unitconverter;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterActivity extends AppCompatActivity {

    private static final String CATEGORY_ID = "category_id";
    private static final String COPY_RESULT = "converter_result";

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private Spinner mSourceSpinner;
    private EditText mSourceEditText;
    private TextView mSourceTextView;
    private EditText mResultEditText;
    private TextView mResultTextView;
    private Spinner mResultSpinner;
    private Button mSwapImageButton;

    private Converter mConverter;
    private AsyncTask mTask;
    private SpinnerListener mListener = new SpinnerListener();

    private int converter_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        final Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < EUnitCategory.values().length; i++) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(EUnitCategory.values()[i].getNameResID()));
            menu.getItem(i).setIcon(EUnitCategory.values()[i].getNavIconResID());
        }
        mNavigationView.getMenu().getItem(converter_id).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mNavigationView.getMenu().getItem(converter_id).setChecked(false);
                menuItem.setChecked(true);
                converter_id = menuItem.getOrder();

                mDrawerLayout.closeDrawers();

                setConverter();
                return true;
            }
        });

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyResultToClipboard(false);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyResultToClipboard(true);
                return true;
            }
        });

        mSourceSpinner = (Spinner) findViewById(R.id.sourcespinner);
        mSourceSpinner.setOnItemSelectedListener(new SpinnerListener());

        mResultSpinner = (Spinner) findViewById(R.id.resultspinner);
        mResultSpinner.setOnItemSelectedListener(new SpinnerListener());

        mSourceEditText = (EditText) findViewById(R.id.sourcevalue);
        mSourceEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mSourceEditText.getText().toString().equals(".")) {
                    mSourceEditText.setText("0.");
                    mSourceEditText.setSelection(mSourceEditText.getText().toString().length());
                }
                if (!mSourceEditText.getText().toString().equals("-")) convert();
            }
        });
        mResultEditText = (EditText) findViewById(R.id.resultvalue);

        mSourceTextView = (TextView) findViewById(R.id.sourcevaluesymbol);
        mResultTextView = (TextView) findViewById(R.id.resultvaluesymbol);

        mSwapImageButton = (Button) findViewById(R.id.swapbutton);
        mSwapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mSourceEditText.getText().toString();
                mSourceEditText.setText(mResultEditText.getText().toString());
                mResultEditText.setText(temp);
                int position = mSourceSpinner.getSelectedItemPosition();
                mSourceSpinner.setSelection(mResultSpinner.getSelectedItemPosition());
                mResultSpinner.setSelection(position);
                convert();
            }
        });

        mSourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mSourceSpinner.performClick(); }
        });

        mResultTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mResultSpinner.performClick(); }
        });

        converter_id = getIntent().getIntExtra("ConverterID", 0);
        setConverter();
    }

    private void setConverter() {
        new InitDataAsync().execute();
    }

    private void convert() {
        if (mSourceEditText.getText().toString().equals("")) mResultEditText.setText("");
        else mResultEditText.setText(mConverter.convert(mSourceEditText.getText().toString(),
                mSourceSpinner.getSelectedItemPosition(),
                mResultSpinner.getSelectedItemPosition()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CATEGORY_ID, converter_id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        converter_id = savedInstanceState.getInt(CATEGORY_ID);
        setConverter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }

    private void copyResultToClipboard(boolean withUnitSymbol) {
        String textToCopy = "";
        if (!mResultEditText.getText().toString().equals("")) {
            textToCopy = mResultEditText.getText().toString();
        }
        if (withUnitSymbol)
        {
            textToCopy += mResultTextView.getText().toString();
        }
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, textToCopy));
        Snackbar.make(mCoordinatorLayout, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSourceTextView.setText(mConverter.getUnitSymbol(mSourceSpinner.getSelectedItemPosition()));
            mResultTextView.setText(mConverter.getUnitSymbol(mResultSpinner.getSelectedItemPosition()));
            convert();
        }

        @Override public void onNothingSelected(AdapterView<?> parent) { convert(); }
    }

    private class InitDataAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mSourceEditText.setText("");
            mResultEditText.setText("");

            mDialog = new ProgressDialog(ConverterActivity.this);
            mDialog.setMessage(getString(R.string.loading_data));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setTitle(mConverter.getTitle());

            mSourceSpinner.setAdapter(mConverter.getAdapter());
            mResultSpinner.setAdapter(mConverter.getAdapter());
            mSourceTextView.setText(mConverter.getUnitSymbol(0));
            mResultTextView.setText(mConverter.getUnitSymbol(0));

            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mConverter = EUnitCategory.values()[converter_id].getConverter(ConverterActivity.this);

            return null;
        }
    }
}
