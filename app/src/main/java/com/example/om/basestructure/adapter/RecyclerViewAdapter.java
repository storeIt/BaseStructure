package com.example.om.basestructure.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.om.basestructure.R;
import com.example.om.basestructure.data.RVItemData;

/**
 * Created by om on 10/17/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RVViewHolder> {

    private RVItemData mRVItemData;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public RecyclerViewAdapter(RVItemData rVItemData, ListItemClickListener listener) {
        mRVItemData = rVItemData;
        mOnClickListener = listener;
    }

    @Override
    public RecyclerViewAdapter.RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_single_row, parent, false);
        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {
        String currentBrand = mRVItemData.getBrand()[position];
        holder.brand.setText(currentBrand);
        String currentYear = mRVItemData.getYear()[position];
        holder.year.setText(currentYear);
        String currentColor = mRVItemData.getColor()[position];
        holder.color.setText(currentColor);

    }

    @Override
    public int getItemCount() {
        return mRVItemData.getBrand().length;
    }

    public class RVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView brand;
        private TextView year;
        private TextView color;

        public RVViewHolder(View itemView) {
            super(itemView);
            brand = (TextView) itemView.findViewById(R.id.brand);
            year = (TextView) itemView.findViewById(R.id.year);
            color = (TextView) itemView.findViewById(R.id.color);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("TAG", "onClick position: " + getAdapterPosition() + ", id: " + getItemId());
            long clickedPosition = getItemId();
            mOnClickListener.onListItemClick((int) clickedPosition);
        }
    }
}
