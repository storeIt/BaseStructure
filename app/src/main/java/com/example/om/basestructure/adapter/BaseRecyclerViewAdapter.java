package com.example.om.basestructure.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.om.basestructure.R;

/**
 * Created by android on 19.10.17.
 */

public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

        void onDeleteButtonItemClick(int clickedItemIndex);
    }

    public BaseRecyclerViewAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.contentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onListItemClick(holder.getAdapterPosition());
                }
            }
        });
        baseViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onDeleteButtonItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public ImageView delete;
        public LinearLayout contentRow;

        public BaseViewHolder(View itemView) {
            super(itemView);
            delete = (ImageView) itemView.findViewById(R.id.trash);
            contentRow = (LinearLayout) itemView.findViewById(R.id.content_row_layout);
        }
    }
}
