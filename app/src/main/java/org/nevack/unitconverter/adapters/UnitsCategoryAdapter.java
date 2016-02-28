package org.nevack.unitconverter.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitsCategoryAdapter extends RecyclerView.Adapter<UnitsCategoryAdapter.ViewHolder> {

    private List<EUnitCategory> unitCategories;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.unit_cardview);
            mTextView = (TextView) itemView.findViewById(R.id.unit_name);
            mImageView = (ImageView) itemView.findViewById(R.id.unit_icon);
        }
    }

    public UnitsCategoryAdapter(Context context) {
        mContext = context;
        unitCategories = new ArrayList<>(Arrays.asList(EUnitCategory.values()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                         int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                 .inflate(R.layout.unitcardview, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mContext.getString(unitCategories.get(position).getNameResID()));
        holder.mImageView.setImageResource(unitCategories.get(position).getIconResID());
        holder.mCardView.setCardBackgroundColor(
                ContextCompat.getColor(mContext, unitCategories.get(position).getColorResID()));
    }

    @Override
    public int getItemCount() {
        return unitCategories.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}