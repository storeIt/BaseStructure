package com.example.om.basestructure.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.base.BaseDialog;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;

import org.joda.time.LocalTime;

/**
 * Created by android on 28.01.18.
 */

public class DialogTime extends BaseDialog {

    public static final String FRAGMENT_TAG = DialogTime.class.getSimpleName();

    public enum TimeValidation {
        RESTRICT_PREVIOUS_TIME, RESTRICT_FUTURE_TIME, NO_TIME_RESTRICTIONS
    }

    private TimeValidation mTimeValidation = TimeValidation.RESTRICT_PREVIOUS_TIME;
    private TimePicker mTimePicker;
    private Button mSetTimeBtn, mCancelTimeBtn;
    private TextView mTextViewValidationErrorMessage;

    private View.OnClickListener mOnClickListenerDialogTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int minutes = 0;
            int hour = 0;
            if (v.getId() == R.id.btn_set_time_dialog) {
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = mTimePicker.getHour();
                    minutes = mTimePicker.getMinute();
                } else {
                    hour = mTimePicker.getCurrentHour();
                    minutes = mTimePicker.getCurrentMinute();
                }
                String time = hour + ":" + minutes;
                if (isTimeValid(time)) {
                    ((BaseFragment) getTargetFragment()).setEndTime(time);
                    dismiss();
                } else {
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }
            } else if (v.getId() == R.id.btn_cancel_time_picker_dialog) {
                dismiss();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_fragment_time_picker, container, false);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker_dialog_fragment);
        mTimePicker.setIs24HourView(Boolean.TRUE);
        mSetTimeBtn = (Button) view.findViewById(R.id.btn_set_time_dialog);
        mSetTimeBtn.setOnClickListener(mOnClickListenerDialogTime);
        mCancelTimeBtn = (Button) view.findViewById(R.id.btn_cancel_time_picker_dialog);
        mCancelTimeBtn.setOnClickListener(mOnClickListenerDialogTime);
        mTextViewValidationErrorMessage = (TextView) view.findViewById(R.id.tv_dialog_time_picker_validation_error_message);
        return view;
    }

    private boolean isTimeValid(String time) {
        LocalTime currentTime = new LocalTime();
        LocalTime validationTime = new LocalTime(time);
        if (mTimeValidation == TimeValidation.RESTRICT_PREVIOUS_TIME) {
            return validationTime.isAfter(currentTime);
        } else if (mTimeValidation == TimeValidation.RESTRICT_FUTURE_TIME) {
            return validationTime.isBefore(currentTime);
        } else {
            return true;
        }
    }
}
