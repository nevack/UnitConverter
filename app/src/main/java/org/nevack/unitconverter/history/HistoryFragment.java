package org.nevack.unitconverter.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements HistoryContract.View {

    private HistoryContract.Presenter presenter;

    private HistoryFragment.HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private View noHistoryView;

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
        adapter = new HistoryFragment.HistoryAdapter(new ArrayList<>());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull HistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        noHistoryView = root.findViewById(R.id.nohistory);
        // Set up tasks view
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(animation);
        adapter = new HistoryAdapter(null);
        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                presenter.clearItems();
                return true;
            case R.id.filter:
                showFilterDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager manager = requireFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment prev = manager.findFragmentByTag("dialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);

        // Create and show the dialog.
        HistoryFilterDialog newFragment = new HistoryFilterDialog();
        newFragment.setListener(presenter::filterItems);
        newFragment.show(transaction, "dialog");
    }

    @Override
    public void showHistoryItems(List<HistoryItem> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

        noHistoryView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoItems() {
        recyclerView.setVisibility(View.GONE);
        noHistoryView.setVisibility(View.VISIBLE);
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private List<HistoryItem> items;

        private class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView categoryName;
            private final ImageView categoryIcon;
            private final ImageView removeItem;
            private final ImageView shareItem;
            private final TextView valueFrom;
            private final TextView valueTo;
            private final TextView unitFrom;
            private final TextView unitTo;

            private ViewHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.category_name);
                categoryIcon = itemView.findViewById(R.id.category_icon);
                removeItem = itemView.findViewById(R.id.remove_item);
                shareItem = itemView.findViewById(R.id.share_item);
                valueFrom = itemView.findViewById(R.id.value_from);
                valueTo = itemView.findViewById(R.id.value_to);
                unitFrom = itemView.findViewById(R.id.unit_from);
                unitTo = itemView.findViewById(R.id.unit_to);
            }

            private void bind(EUnitCategory category, HistoryItem item) {
                categoryName.setText(category.getName());
                valueFrom.setText(item.getValueFrom());
                valueTo.setText(item.getValueTo());
                unitFrom.setText(item.getUnitFrom());
                unitTo.setText(item.getUnitTo());

                itemView.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), category.getColor()));
                categoryIcon.setBackground(
                        itemView.getContext().getDrawable(category.getIcon()));
            }
        }

        HistoryAdapter(List<HistoryItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_item, viewGroup, false);

            ViewHolder holder = new ViewHolder(view);

            holder.removeItem.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    presenter.removeItem(items.get(position));
                    items.remove(position);
                    notifyItemRemoved(position);
                }
            });

            holder.shareItem.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    presenter.shareItem(items.get(position));
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HistoryItem item = items.get(holder.getAdapterPosition());
            EUnitCategory category = EUnitCategory.values()[item.getCategory()];

            holder.bind(category, item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void setItems(List<HistoryItem> items) {
            this.items = items;
        }
    }
}
