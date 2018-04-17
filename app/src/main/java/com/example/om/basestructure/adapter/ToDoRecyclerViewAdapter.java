package com.example.om.basestructure.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.om.basestructure.R;
import com.example.om.basestructure.model.ToDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by om on 10/17/17.
 */

public class ToDoRecyclerViewAdapter extends BaseRecyclerViewAdapter {

    private List<ToDo> mOriginItems;
    private static final int TYPE_HEADER = 0;

    public ToDoRecyclerViewAdapter(List<ToDo> toDoList, ListItemClickListener listener) {
        super(listener);
        mOriginItems = toDoList;
    }

    @Override
    public ToDoRecyclerViewAdapter.RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_single_row, parent, false);
        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        RVViewHolder rvViewHolder = (RVViewHolder) holder;
        Log.e("RVAdapter LOG", "looped " + position);
        String toDoStr = mOriginItems.get(position).getToDo();
        rvViewHolder.toDo.setText(toDoStr);
        String descriptionStr = mOriginItems.get(position).getDescription();
        rvViewHolder.description.setText(descriptionStr);
        String priority = mOriginItems.get(position).getPriority();
        rvViewHolder.priority.setText(priority);


    }

    public void filter(String searchStr, List<ToDo> toDoList) {
        mOriginItems = toDoList;
        notifyDataSetChanged();
        List<ToDo> filteredList = new ArrayList<>();
        for (ToDo mOriginItem : mOriginItems) {
            if (mOriginItem.getToDo().toLowerCase().contains(searchStr) ||
                    mOriginItem.getDescription().toLowerCase().contains(searchStr)
                    || String.valueOf(mOriginItem.getPriority()).toLowerCase().contains(searchStr)) {
                filteredList.add(mOriginItem);
            }
        }
        mOriginItems = filteredList;
        notifyDataSetChanged();
    }

    public void refreshItems(List<ToDo> items) {
        mOriginItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mOriginItems.size();
    }

    public class RVViewHolder extends BaseViewHolder {
        public TextView toDo;
        public TextView description;
        public TextView priority;

        public RVViewHolder(View itemView) {
            super(itemView);
            toDo = (TextView) itemView.findViewById(R.id.tv_to_do_single_row);
            description = (TextView) itemView.findViewById(R.id.tv_to_do_description_single_row);
            priority = (TextView) itemView.findViewById(R.id.tv_to_do_priority_single_row);
        }
    }
}
