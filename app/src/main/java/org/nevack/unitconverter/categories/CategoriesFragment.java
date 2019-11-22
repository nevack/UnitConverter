package org.nevack.unitconverter.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.EUnitCategory;

import java.util.Arrays;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoriesContract.View {

    private CategoriesContract.Presenter presenter;

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
        columns = getResources().getInteger(R.integer.column_count);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull CategoriesContract.Presenter presenter) {
        this.presenter = presenter;
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
        adapter = new CategoriesAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

        private final List<EUnitCategory> unitCategories;

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.category_name);
                imageView = itemView.findViewById(R.id.category_icon);
            }

            private void bind(EUnitCategory category) {
                textView.setText(category.getName());
                imageView.setImageResource(category.getIcon());
                itemView.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), category.getColor()));
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

                if (position != RecyclerView.NO_POSITION) {
                    presenter.openConverter(unitCategories.get(position));
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            EUnitCategory category = unitCategories.get(holder.getAdapterPosition());

            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return unitCategories.size();
        }
    }
}
