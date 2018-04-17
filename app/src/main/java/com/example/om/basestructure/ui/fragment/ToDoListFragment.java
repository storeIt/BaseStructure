package com.example.om.basestructure.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.QueryOptions;
import com.daimajia.swipe.SwipeLayout;
import com.example.om.basestructure.R;
import com.example.om.basestructure.adapter.ToDoRecyclerViewAdapter;
import com.example.om.basestructure.db.service.DbService;
import com.example.om.basestructure.db.view.ToDoView;
import com.example.om.basestructure.model.ToDo;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;
import com.example.om.basestructure.ui.screen.base.BaseActivity;

import java.util.List;

/**
 * Created by android on 17.01.18.
 */

public class ToDoListFragment extends BaseFragment implements ToDoRecyclerViewAdapter.ListItemClickListener {
    public static final String FRAGMENT_TAG = ToDoListFragment.class.getSimpleName();
    private static final String LOG_TAG = ToDoListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<ToDo> mToDoList;
    private ToDoRecyclerViewAdapter mAdapter;
    private FloatingActionButton mFab;
    private EditText mEditTextSearch;
    private SwipeLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView() is called");
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_to_do_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mToDoList = DbService.getInstance().fetchItems(ToDoView.NAME, new QueryOptions(), ToDo.class);
        mAdapter = new ToDoRecyclerViewAdapter(mToDoList, this);
        mRecyclerView.setAdapter(mAdapter);
        mEditTextSearch = (EditText) view.findViewById(R.id.et_search_fragment_to_do_list);
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.filter(s.toString().toLowerCase(), mToDoList);
            }
        });
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_to_do_list);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).replaceFragment(R.id.fragment_container, ToDoDetailsFragment.FRAGMENT_TAG, null, null);
            }
        });
        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.i(LOG_TAG, "onListItemClick() is called");
        Toast.makeText(getActivity(), "Row is clicked", Toast.LENGTH_SHORT).show();
        ((BaseActivity) getActivity()).replaceFragment(R.id.fragment_container, ToDoDetailsFragment.FRAGMENT_TAG, mToDoList.get(clickedItemIndex), null);
    }

    @Override
    public void onDeleteButtonItemClick(int clickedItemIndex) {
        DbService.getInstance().deleteDocument(mToDoList.get(clickedItemIndex).getId());
        mToDoList = DbService.getInstance().fetchItems(ToDoView.NAME, new QueryOptions(), ToDo.class);
        mAdapter.refreshItems(mToDoList);
    }
}
