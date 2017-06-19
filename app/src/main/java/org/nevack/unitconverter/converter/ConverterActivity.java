package org.nevack.unitconverter.converter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.nevack.unitconverter.history.HistoryActivity;
import org.nevack.unitconverter.R;
import org.nevack.unitconverter.database.HistoryContract;
import org.nevack.unitconverter.database.HistoryDatabaseHelper;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterActivity extends AppCompatActivity implements ConverterDisplayView.DisplayCallback {

    private static final String CATEGORY_ID = "category_id";
    private static final String ID_EXTRA = "converter_id";

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private KeypadView keypadView;
    private ConverterDisplayView display;

    private Converter converter;
    private int converterId;
    private ImageView iconImage;
    private SQLiteDatabase db;

    public static Intent getIntent(Context context, int converterId) {
        Intent intent = new Intent(context, ConverterActivity.class);
        intent.putExtra(ID_EXTRA, converterId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converterId = getIntent().getIntExtra(ID_EXTRA, 0);
        setContentView(R.layout.activity_converter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iconImage = findViewById(R.id.converter_icon);

        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);
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

        display = findViewById(R.id.display);
        keypadView = findViewById(R.id.keypad);
        display.setupWithKeypad(keypadView);

        setConverter();

        HistoryDatabaseHelper helper = new HistoryDatabaseHelper(this);
        db = helper.getWritableDatabase();
    }

    private void setConverter() {
        int color = ContextCompat.getColor(this, EUnitCategory.values()[converterId].getColor());
        toolbar.setBackgroundColor(color);
        display.setBackgroundColor(color);
        iconImage.setImageResource(EUnitCategory.values()[converterId].getIcon());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.HSVToColor(hsv));
        }
        new InitDataAsync().execute();
    }

    public String convert(String value, int from, int to) {
        if (!value.isEmpty()) return converter.convert(value, from, to);
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(HistoryContract.HistoryEntry.COLUMN_NAME_CATEGORY, getTitle().toString());
                values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_FROM, display.getSelectedSourceUnit());
                values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_TO, display.getSelectedResultUnit());
                values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_FROM, display.getSourceValue());
                values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_TO, display.getResultValue());
                long newRowId = db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values);
                break;
            case R.id.history:
                startActivity(new Intent(this, HistoryActivity.class));
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

    private class InitDataAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(ConverterActivity.this);
            mDialog.setMessage(getString(R.string.loading_data));
            mDialog.setCancelable(false);
            if (converterId == EUnitCategory.CURRENCY.ordinal()) mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setTitle(converter.getTitle());

            display.setUnits(converter.getUnits());

            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            converter = EUnitCategory.values()[converterId].getConverter(ConverterActivity.this);
            return null;
        }
    }
}
