package org.nevack.unitconverter.converter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;

public class ConverterActivity extends AppCompatActivity {

    private static final String CONVERTER_ID_EXTRA = "converter_id";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private int converterId;
    private ConverterContract.Presenter mPresenter;

    public static Intent getIntent(Context context, int converterId) {
        Intent intent = new Intent(context, ConverterActivity.class);
        intent.putExtra(CONVERTER_ID_EXTRA, converterId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converterId = getIntent().getIntExtra(CONVERTER_ID_EXTRA, 0);
        setContentView(R.layout.activity_converter);

        // Setup transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);

        ConverterFragment fragment =
                (ConverterFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = ConverterFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, fragment);
            transaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        mPresenter = new ConverterPresenter(this, fragment, getSupportLoaderManager());
    }

    private void setupDrawerContent(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < EUnitCategory.values().length; i++) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(EUnitCategory.values()[i].getName()));
            menu.getItem(i).setIcon(EUnitCategory.values()[i].getIcon());
        }

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menu.getItem(converterId).setChecked(false);
                    converterId = menuItem.getOrder();
                    mPresenter.setConverter(EUnitCategory.values()[converterId]);

                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setConverter(EUnitCategory.values()[converterId]);
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
        outState.putInt(CONVERTER_ID_EXTRA, converterId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        converterId = savedInstanceState.getInt(CONVERTER_ID_EXTRA);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }
}
