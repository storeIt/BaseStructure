package com.example.om.basestructure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.om.basestructure.adapter.RecyclerViewAdapter;
import com.example.om.basestructure.data.RVItemData;
import com.example.om.basestructure.utils.BaseActivity;


public class RVFragment extends Fragment implements RecyclerViewAdapter.ListItemClickListener {

    private static final String LOG_TAG = RVFragment.class.getSimpleName();
    private BaseActivity mBaseActivity;
    private RecyclerView mRecyclerView;
    private RVItemData mRVItemData;
    private String[] mBrandArray = new String[]{"Audi", "BMW", "Cadillac", "Dacia", "Eagle", "Ford"};
    private String[] mYearArray = new String[]{"2000", "2001", "2002", "1999", "1964", "2017"};
    private String[] mColorArray = new String[]{"black", "silver", "green", "red", "yellow", "blue"};
    private RecyclerViewAdapter mAdapter;

    public RVFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView() is called");
        View view = inflater.inflate(R.layout.fragment_rv, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVItemData = new RVItemData(mBrandArray, mYearArray, mColorArray);
        mAdapter = new RecyclerViewAdapter(mRVItemData, this);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.i(LOG_TAG, "onListItemClick() is called");
        Toast.makeText(getContext(), "Row is clicked", Toast.LENGTH_SHORT).show();
        RVDetailsFragment detailsFragment = new RVDetailsFragment();
        mBaseActivity = new BaseActivity();
        mBaseActivity.replaceFragment(R.id.fragment_container, detailsFragment);
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment)
//                .addToBackStack(null)
//                .commit();

    }
}
