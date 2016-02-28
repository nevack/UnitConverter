package org.nevack.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.nevack.unitconverter.adapters.UnitsCategoryAdapter;
import org.nevack.unitconverter.views.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.unitsrecycler);
        final UnitsCategoryAdapter adapter = new UnitsCategoryAdapter(this);
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
}
