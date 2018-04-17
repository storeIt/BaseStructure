package com.example.om.basestructure.ui.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.om.basestructure.R;
import com.example.om.basestructure.ui.fragment.base.BaseDialog;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;
import com.example.om.basestructure.utils.DateTimeUtils;

import org.joda.time.LocalDate;

/**
 * Created by android on 23.01.18.
 */

public class DialogCalendar extends BaseDialog {


    public static final String FRAGMENT_TAG = DialogCalendar.class.getSimpleName();

    public enum DateValidation {
        RESTRICT_PREVIOUS_DATE, RESTRICT_FUTURE_DATE, NO_DATE_RESTRICTIONS
    }

    private DateValidation mDateValidation = DateValidation.NO_DATE_RESTRICTIONS;
    private DatePicker mDatePicker;
    private Button mSetDateBtn, mCancelDateBtn;
    private TextView mTextViewDateValidationErrorMessage;
    private String mCurrentDate;

    private View.OnClickListener mOnClickListenerDialogCalendar = new View.OnClickListener() {

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = null;
//    try {
//            date = sdf.parse(dateOfBirth);
//        } catch (ParseException e) {
//            // handle exception here !
//        }
//
//        String myString = DateFormat.getDateInstance(DateFormat.SHORT).format(date);

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_set_date_dialog) {
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth() + 1;
                int year = mDatePicker.getYear();
                LocalDate date = DateTimeUtils.parseStringToDate(day + "/" + month + "/" + year);
                String currentDate = date.toString(DateTimeUtils.DEFAULT_DATE_FORMAT);
                if (isDateValid(date)) {
                    ((BaseFragment) getTargetFragment()).setEndDate(currentDate);
                    DialogCalendar.this.dismiss();
                } else {
                    mTextViewDateValidationErrorMessage.setVisibility(View.VISIBLE);
                }
            } else if (v.getId() == R.id.btn_cancel_date_dialog) {
                DialogCalendar.this.dismiss();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle(R.string.dialog_calendar_title);
        View view = inflater.inflate(R.layout.dialog_fragment_calendar, container, false);
        mDatePicker = (DatePicker) view.findViewById(R.id.dp_dialog_fragment);
        if (!TextUtils.isEmpty(mCurrentDate)) {
            LocalDate date = DateTimeUtils.parseStringToDate(mCurrentDate);
            mDatePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
        mSetDateBtn = (Button) view.findViewById(R.id.btn_set_date_dialog);
        mSetDateBtn.setOnClickListener(mOnClickListenerDialogCalendar);
        mCancelDateBtn = (Button) view.findViewById(R.id.btn_cancel_date_dialog);
        mCancelDateBtn.setOnClickListener(mOnClickListenerDialogCalendar);
        mTextViewDateValidationErrorMessage = (TextView) view.findViewById(R.id.tv_dialog_date_picker_validation_error_message);
        return view;
    }

    private boolean isDateValid(LocalDate date) {
        if (mDateValidation == DateValidation.RESTRICT_PREVIOUS_DATE) {
            return DateTimeUtils.isAfterToday(date) || DateTimeUtils.isToday(date);
        } else if (mDateValidation == DateValidation.RESTRICT_FUTURE_DATE) {
            return !DateTimeUtils.isAfterToday(date) || DateTimeUtils.isToday(date);
        } else {
            return true;
        }
    }

    @Override
    public void setData(Object... data) {
        if (data.length == 1) {
            mDateValidation = (DateValidation) data[0];
        } else if (data.length == 2) {
            mDateValidation = (DateValidation) data[0];
            mCurrentDate = (String) data[1];

        }
    }
}
