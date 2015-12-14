package org.nevack.unitconverter.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.UnitCategory;

import java.util.List;

public class UnitsSelectAdapter extends RecyclerView.Adapter<UnitsSelectAdapter.ViewHolder> {
    private List<UnitCategory> unitCategories;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.unit_cardview);
            textView = (TextView) itemView.findViewById(R.id.unit_name);
            imageView = (ImageView) itemView.findViewById(R.id.unit_icon);
        }
    }

    public UnitsSelectAdapter(List<UnitCategory> unitCategories) {
        this.unitCategories = unitCategories;
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
        holder.textView.setText(unitCategories.get(position).getName());
        holder.imageView.setImageResource(unitCategories.get(position).getIcon());
        holder.cardView.setCardBackgroundColor(unitCategories.get(position).getColor());

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