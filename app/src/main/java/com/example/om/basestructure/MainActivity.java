package com.example.om.basestructure;

import android.os.Bundle;
import android.util.Log;

import com.example.om.basestructure.utils.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RVFragment mRVFragment;
    private RVDetailsFragment mRVDetailsFragment;
    private BaseActivity mBaseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate() is called");

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        mBaseActivity = new BaseActivity();
        mRVFragment = new RVFragment();
        //TODO:IllegalStateException why?
        //mBaseActivity.addFragment(R.id.fragment_container, mRVFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mRVFragment)
                .addToBackStack(null)
                .commit();
    }
}
