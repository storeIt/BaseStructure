package com.example.om.basestructure.ui.fragment.dialog;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.base.BaseDialog;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;
import com.example.om.basestructure.utils.DateTimeUtils;

import org.joda.time.LocalTime;

/**
 * Created by android on 28.01.18.
 */

public class DialogTime extends BaseDialog implements TimePickerDialog.OnTimeSetListener {

    public static final String FRAGMENT_TAG = DialogTime.class.getSimpleName();


    public enum TimeValidation {
        RESTRICT_PREVIOUS_TIME, RESTRICT_FUTURE_TIME, NO_TIME_RESTRICTIONS
    }

    private TimeValidation mTimeValidation = TimeValidation.RESTRICT_PREVIOUS_TIME;
    private TimePicker mTimePicker;
    private Button mSetTimeBtn, mCancelTimeBtn;
    private TextView mTextViewValidationErrorMessage;
    private String mCurrentTime;

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
                String time = formatTime(hour, minutes);
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
        getDialog().setTitle(R.string.dialog_time_title);
        View view = inflater.inflate(R.layout.dialog_fragment_time_picker, container, false);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker_dialog_fragment);
        if (!TextUtils.isEmpty(mCurrentTime)) {
            LocalTime time = DateTimeUtils.parseStringToTime(mCurrentTime);
            int minutes = time.getMinuteOfHour();
            int hour = time.getHourOfDay();
            if (Build.VERSION.SDK_INT >= 23) {
                mTimePicker.setHour(hour);
                mTimePicker.setMinute(minutes);
            } else {
                mTimePicker.setCurrentHour(hour);
                mTimePicker.setCurrentMinute(minutes);
            }
        }
        mTimePicker.setIs24HourView(Boolean.TRUE);
        mSetTimeBtn = (Button) view.findViewById(R.id.btn_set_time_dialog);
        mSetTimeBtn.setOnClickListener(mOnClickListenerDialogTime);
        mCancelTimeBtn = (Button) view.findViewById(R.id.btn_cancel_time_picker_dialog);
        mCancelTimeBtn.setOnClickListener(mOnClickListenerDialogTime);
        mTextViewValidationErrorMessage = (TextView) view.findViewById(R.id.tv_dialog_time_picker_validation_error_message);
        return view;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

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

    private String formatTime(int hour, int minutes) {
        String time;
        String h;
        String m;
        if (hour < 10) {
            h = "0" + hour;
        } else {
            h = Integer.toString(hour);
        }
        if (minutes < 10) {
            m = "0" + minutes;
        } else {
            m = Integer.toString(minutes);
        }
        time = h + ":" + m;
        return time;
    }

    @Override
    public void setData(Object... data) {
        if (data.length == 1) {
            mTimeValidation = (DialogTime.TimeValidation) data[0];
        } else if (data.length == 2) {
            mTimeValidation = (DialogTime.TimeValidation) data[0];
            mCurrentTime = (String) data[1];
        }
    }


}
