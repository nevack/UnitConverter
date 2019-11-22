package org.nevack.unitconverter.converter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

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
    private ConverterContract.Presenter presenter;

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

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(Color.TRANSPARENT);

        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);

        setupDrawerContent();

        ConverterFragment fragment =
                (ConverterFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = ConverterFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, fragment);
            transaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        presenter = new ConverterPresenter(this, fragment, LoaderManager.getInstance(this));
    }

    private void setupDrawerContent() {
        EUnitCategory[] units = EUnitCategory.values();

        Menu menu = navigationView.getMenu();

        for (int i = 0; i < units.length; i++) {
            menu.add(Menu.NONE, Menu.NONE, i, getString(units[i].getName()));
            menu.getItem(i).setIcon(units[i].getIcon());
        }

        menu.setGroupCheckable(Menu.NONE, true, true);
        menu.getItem(converterId).setChecked(true);

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    converterId = menuItem.getOrder();
                    presenter.setConverter(units[converterId]);

                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setConverter(EUnitCategory.values()[converterId]);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open drawer when hamburger menu button (☰) pressed in actionbar
        if (item.getItemId() == android.R.id.home) {
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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        converterId = savedInstanceState.getInt(CONVERTER_ID_EXTRA);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }
}
