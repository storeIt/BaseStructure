package com.example.om.basestructure.ui.screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.ToDoListFragment;
import com.example.om.basestructure.ui.screen.base.BaseActivity;

public class MainActivity extends BaseActivity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean isSomethingChanged = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "onCreate() is called");
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("id") != null && bundle.getString("fragmentTag") != null) {
            replaceFragment(R.id.fragment_container, getIntent().getStringExtra("fragmentTag"), null, bundle);
        } else {
            replaceFragment(R.id.fragment_container, ToDoListFragment.FRAGMENT_TAG, null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 2 && isSomethingChanged) {
            showAppDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                }
            }, R.string.unsaved_changes_dialog, R.string.yes, R.string.no);
        } else {
            super.onBackPressed();
        }
    }
}



