package org.nevack.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.nevack.unitconverter.adapters.UnitsSelectAdapter;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.UnitCategory;
import org.nevack.unitconverter.views.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<UnitCategory> unitCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.unitsrecycler);
        final UnitsSelectAdapter adapter = new UnitsSelectAdapter(unitCategories);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, ConvertBaseActivity.class);
                        intent.putExtra("ConverterID", position);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_in_anim, R.anim.enter_out_anim);
                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        unitCategories = new ArrayList<>();
        for (EUnitCategory category : EUnitCategory.values()) {
            unitCategories.add(new UnitCategory(category.getName(this), category.getUnitIconResID(), category.getColor(this)));
        }
    }
}
