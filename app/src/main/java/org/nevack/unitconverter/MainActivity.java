package org.nevack.unitconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.column_count)));
        recyclerView.setAdapter(new UnitsCategoryAdapter());
    }

    class UnitsCategoryAdapter extends RecyclerView.Adapter<UnitsCategoryAdapter.ViewHolder> {

        private final List<EUnitCategory> unitCategories;

        class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout mContainer;
            private TextView mTextView;
            private ImageView mImageView;

            ViewHolder(View itemView) {
                super(itemView);
                mContainer = (FrameLayout) itemView.findViewById(R.id.category_container);
                mTextView = (TextView) itemView.findViewById(R.id.category_name);
                mImageView = (ImageView) itemView.findViewById(R.id.category_icon);
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(unitCategories.get(position).getName());
            holder.mImageView.setImageResource(unitCategories.get(position).getIcon());
            holder.mContainer.setBackgroundResource(unitCategories.get(position).getColor());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ConverterActivity.getIntent(MainActivity.this, holder.getAdapterPosition()));
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
