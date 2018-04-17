package com.example.om.basestructure.ui.fragment.base;

import android.support.v4.app.Fragment;

/**
 * Created by android on 22.10.17.
 */

public class BaseFragment extends Fragment {

    private Object mData;
    private String mDate;
    private String mTime;

    public String getDate() {
        return mDate;
    }

    public void setEndDate(String date) {
        mDate = date;
    }

    public String getEndTime() {
        return mTime;
    }

    public void setEndTime(String endTime) {
        mTime = endTime;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }

}
