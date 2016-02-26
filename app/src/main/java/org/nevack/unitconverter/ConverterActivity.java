package org.nevack.unitconverter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.nevack.unitconverter.fragments.ConverterFragment;
import org.nevack.unitconverter.model.EUnitCategory;

public class ConverterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private int converter_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        converter_id = getIntent().getIntExtra("ConverterID", 0);
        final FragmentManager manager = getFragmentManager();
        final ConverterFragment fragment = ConverterFragment
                .newInstance(EUnitCategory.values()[converter_id]);
        manager.beginTransaction().add(R.id.fragment_container, fragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        final Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < EUnitCategory.values().length; i++) {
            menu.add(Menu.NONE, Menu.NONE, i, EUnitCategory.values()[i].getName(this));
        }
        mNavigationView.getMenu().getItem(converter_id).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mNavigationView.getMenu().getItem(converter_id).setChecked(false);
                menuItem.setChecked(true);
                converter_id = menuItem.getOrder();

                ConverterFragment converterFragment = ConverterFragment
                        .newInstance(EUnitCategory.values()[converter_id]);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, converterFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyResultToClipboard(fragment.getResult(false));
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyResultToClipboard(fragment.getResult(true));
                return true;
            }
        });
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.leave_in_anim, R.anim.leave_out_anim);
    }

    private void copyResultToClipboard(ClipData clipData) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(ConverterActivity.this, R.string.copy_result_toast, Toast.LENGTH_SHORT).show();
    }
}
