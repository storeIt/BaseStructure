package com.example.om.basestructure.utils;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by om on 10/17/17.
 */

public class BaseActivity extends AppCompatActivity {

    public BaseActivity() {
    }

    public void addFragment(int fragmentContainer, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(fragmentContainer, fragment).commit();
    }

    public void replaceFragment(int fragmentContainer, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
