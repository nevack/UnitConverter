package org.nevack.unitconverter.categories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;

import java.util.Arrays;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoriesContract.View {

    private CategoriesContract.Presenter mPresenter;

    private CategoriesAdapter adapter;
    private RecyclerView recyclerView;

    private int columns;

    public CategoriesFragment() {
        // Requires empty public constructor
    }

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CategoriesAdapter();
        columns = getResources().getInteger(R.integer.column_count);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull CategoriesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), columns));
        recyclerView.setHasFixedSize(true);

        showCategories();

        return root;
    }

    @Override
    public void showCategories() {
        recyclerView.setAdapter(adapter);
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

        private final List<EUnitCategory> unitCategories;

        class ViewHolder extends RecyclerView.ViewHolder {
            private final FrameLayout container;
            private final TextView textView;
            private final ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);

                container = itemView.findViewById(R.id.category_container);
                textView = itemView.findViewById(R.id.category_name);
                imageView = itemView.findViewById(R.id.category_icon);
            }
        }

        CategoriesAdapter() {
            unitCategories = Arrays.asList(EUnitCategory.values());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.category_item, viewGroup, false);

            ViewHolder holder = new ViewHolder(view);

            view.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {
                    mPresenter.openConverter(unitCategories.get(position));
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            EUnitCategory category = unitCategories.get(holder.getAdapterPosition());

            holder.textView.setText(category.getName());
            holder.imageView.setImageResource(category.getIcon());
            holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), category.getColor()));
        }

        @Override
        public int getItemCount() {
            return unitCategories.size();
        }
    }
}
