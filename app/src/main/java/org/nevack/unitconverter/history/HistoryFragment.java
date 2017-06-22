package org.nevack.unitconverter.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements HistoryContract.View {

    private HistoryContract.Presenter mPresenter;

    private HistoryFragment.HistoryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private View mNoHistoryView;

    public HistoryFragment() {
        // Requires empty public constructor
    }

    @NonNull
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HistoryFragment.HistoryAdapter(new ArrayList<>());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        mNoHistoryView = root.findViewById(R.id.nohistory);
        // Set up tasks view
        mRecyclerView = root.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void showHistoryItems(List<HistoryItem> items) {
        mAdapter = new HistoryAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        mNoHistoryView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoItems() {
        mRecyclerView.setVisibility(View.GONE);
        mNoHistoryView.setVisibility(View.VISIBLE);
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final List<HistoryItem> items;

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView category;
            private final TextView valuefrom;
            private final TextView valueto;
            private final TextView unitfrom;
            private final TextView unitto;

            ViewHolder(View itemView) {
                super(itemView);
                category  = itemView.findViewById(R.id.category_name);
                valuefrom = itemView.findViewById(R.id.valuefrom);
                valueto  = itemView.findViewById(R.id.valueto);
                unitfrom = itemView.findViewById(R.id.unitnamefrom);
                unitto = itemView.findViewById(R.id.unitnameto);
            }
        }

        HistoryAdapter(List<HistoryItem> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.category.setText(items.get(position).getCategory());
            holder.valuefrom.setText(items.get(position).getValuefrom());
            holder.valueto.setText(items.get(position).getValueto());
            holder.unitfrom.setText(items.get(position).getUnitfrom());
            holder.unitto.setText(items.get(position).getUnitto());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
