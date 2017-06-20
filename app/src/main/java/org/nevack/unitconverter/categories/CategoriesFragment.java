package org.nevack.unitconverter.categories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoriesContract.View {

    private CategoriesContract.Presenter mPresenter;

    private CategoriesAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public CategoriesFragment() {
        // Requires empty public constructor
    }

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CategoriesAdapter();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        // Set up tasks view
        mRecyclerView = root.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.column_count)));
        showCategories();

        return root;
    }

    @Override
    public void showCategories() {
        mRecyclerView.setAdapter(mAdapter);
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

        private final List<EUnitCategory> unitCategories;

        class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout mContainer;
            private TextView mTextView;
            private ImageView mImageView;

            ViewHolder(View itemView) {
                super(itemView);
                mContainer = itemView.findViewById(R.id.category_container);
                mTextView = itemView.findViewById(R.id.category_name);
                mImageView = itemView.findViewById(R.id.category_icon);
            }
        }

        CategoriesAdapter() {
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
            holder.itemView.setOnClickListener(v -> {
                mPresenter.openConverter(unitCategories.get(position));
                getActivity().overridePendingTransition(R.anim.enter_in_anim, R.anim.enter_out_anim);
            });
        }

        @Override
        public int getItemCount() {
            return unitCategories.size();
        }
    }
}
