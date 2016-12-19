package org.nevack.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.nevack.unitconverter.model.EUnitCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.unitsrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UnitsCategoryAdapter());
    }

    class UnitsCategoryAdapter extends RecyclerView.Adapter<UnitsCategoryAdapter.ViewHolder> {

        private List<EUnitCategory> unitCategories;

        class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout mCardView;
            private TextView mTextView;
            private ImageView mImageView;

            ViewHolder(View itemView) {
                super(itemView);
                mCardView = (FrameLayout) itemView.findViewById(R.id.unit_cardview);
                mTextView = (TextView) itemView.findViewById(R.id.unit_name);
                mImageView = (ImageView) itemView.findViewById(R.id.unit_icon);
            }
        }

        UnitsCategoryAdapter() {
            unitCategories = new ArrayList<>(Arrays.asList(EUnitCategory.values()));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.category_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(unitCategories.get(position).getNameResID());
            holder.mImageView.setImageResource(unitCategories.get(position).getIconResID());
            holder.mCardView.setBackgroundResource(unitCategories.get(position).getColorResID());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ConverterActivity.class);
                    intent.putExtra("ConverterID", position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_in_anim, R.anim.enter_out_anim);
                }
            });
        }

        @Override
        public int getItemCount() {
            return unitCategories.size();
        }
    }
}
