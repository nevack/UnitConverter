package org.nevack.unitconverter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterActivity extends AppCompatActivity implements ConverterDisplayView.DisplayCallback {

    private static final String CATEGORY_ID = "category_id";
    private static final String ID_EXTRA = "CONVERTERID";

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private KeypadView keypadView;

    private Converter converter;

    private int converterId;
    private ConverterDisplayView display;

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

        display = (ConverterDisplayView) findViewById(R.id.display);
        keypadView = (KeypadView) findViewById(R.id.keypad);
        display.setupWithKeypad(keypadView);

        keypadView.setCopyListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        display.copyResultToClipboard(false);
                    }
                },
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        display.copyResultToClipboard(true);
                        return true;
                    }
                }
        );

        keypadView.setBackspaceLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                display.erase();
                return true;
            }
        });

        setConverter();
    }

    private void setConverter() {
        new InitDataAsync().execute();
    }

    public String convert(String value, int from, int to) {
        if (!value.isEmpty()) return converter.convert(value, from, to);
        return "";
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



    private class InitDataAsync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //display.erase();

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
