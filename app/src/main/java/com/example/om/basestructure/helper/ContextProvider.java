package com.example.om.basestructure.helper;

import android.content.Context;

/**
 * Created by android on 22.10.17.
 */

public class ContextProvider {

    private static ContextProvider instance;
    private Context mContext;

    private ContextProvider() {
    }

    public static ContextProvider getInstance() {
        if (instance == null) {
            instance = new ContextProvider();
        }
        return instance;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
