package com.example.om.basestructure.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.base.BaseDialog;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;

/**
 * Created by android on 28.01.18.
 */

public class DialogTime extends BaseDialog {
    public static final String FRAGMENT_TAG = DialogTime.class.getSimpleName();
    private TimePicker mTimePicker;
    private Button mSaveDateBtn;

    private View.OnClickListener mOnClickListenerDialogTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int minutes = 0;
            int hour = 0;
            if (v.getId() == R.id.btn_save_time_dialog) {

                if (Build.VERSION.SDK_INT >= 23) {
                    hour = mTimePicker.getHour();
                    minutes = mTimePicker.getMinute();
                } else {
                    hour = mTimePicker.getCurrentHour();
                    minutes = mTimePicker.getCurrentMinute();
                }
                String date = hour + "/" + minutes + "/";
                ((BaseFragment) getTargetFragment()).setDate(date);
            } else if (v.getId() == R.id.btn_cancel_time_picker_dialog) {
                dismiss();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_fragment_calendar, container, false);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker_dialog_fragment);
        mSaveDateBtn = (Button) view.findViewById(R.id.btn_save_time_dialog);
        mSaveDateBtn.setOnClickListener(mOnClickListenerDialogTime);
        return view;
    }
}
