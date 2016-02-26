package org.nevack.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
                        Intent intent = new Intent(MainActivity.this, ConverterActivity.class);
                        intent.putExtra("ConverterID", position);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_in_anim, R.anim.enter_out_anim);
                    }
                })
        );
    }

    private void initData() {
        unitCategories = new ArrayList<>();
        for (EUnitCategory category : EUnitCategory.values()) {
            unitCategories.add(new UnitCategory(category.getName(this), category.getUnitIconResID(), category.getColor(this)));
        }
    }
}
