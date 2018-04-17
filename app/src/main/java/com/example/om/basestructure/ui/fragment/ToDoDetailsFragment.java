package com.example.om.basestructure.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.om.basestructure.R;
import com.example.om.basestructure.db.service.ToDoDbService;
import com.example.om.basestructure.helper.LocationHelper;
import com.example.om.basestructure.helper.NotificationHelper;
import com.example.om.basestructure.model.Location;
import com.example.om.basestructure.model.ToDo;
import com.example.om.basestructure.notification.ToDoNotificationReceiver;
import com.example.om.basestructure.service.GeofenceNotificationService;
import com.example.om.basestructure.ui.fragment.base.BaseFragment;
import com.example.om.basestructure.ui.fragment.dialog.DialogCalendar;
import com.example.om.basestructure.ui.fragment.dialog.DialogTime;
import com.example.om.basestructure.ui.screen.MapActivity;
import com.example.om.basestructure.ui.screen.base.BaseActivity;
import com.example.om.basestructure.utils.DateTimeUtils;
import com.example.om.basestructure.utils.JsonUtils;
import com.example.om.basestructure.utils.MapUtils;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by android on 17.01.18.
 */

public class ToDoDetailsFragment extends BaseFragment {

    public static final String FRAGMENT_TAG = ToDoDetailsFragment.class.getSimpleName();
    public static final String LOW_PRIORITY = "Low";
    public static final String MEDIUM_PRIORITY = "Medium";
    public static final String HIGH_PRIORITY = "High";
    private EditText mEditTextToDoName, mDescription;
    private TextView mTextViewEndDate, mTextViewEndTime, mTextViewGls;
    private LinearLayout mLayoutWhenDate, mLayoutWhenTime, mLayoutGls;
    private String mPriority = LOW_PRIORITY;
    private Button mBtnSave;
    private RadioGroup mPriorityRadioGroup;
    private ToDoDbService mToDoDbService = ToDoDbService.getInstance();
    private Switch mNotificationSwitch;
    private TextView mTextViewDateWarning, mTextViewTimeWarning;
    private Location mLocation;

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_dp_when_to_do_details_fragment:
                    ((BaseActivity) getActivity()).showDialog(
                            DialogCalendar.FRAGMENT_TAG,
                            ToDoDetailsFragment.this,
                            true,
                            DialogCalendar.DateValidation.RESTRICT_PREVIOUS_DATE,
                            mTextViewEndDate.getText().toString());
                    break;
                case R.id.layout_tp_when_to_do_details_fragment:
                    DialogTime.TimeValidation timeValidation = DialogTime.TimeValidation.NO_TIME_RESTRICTIONS;
                    String date = mTextViewEndDate.getText().toString();
                    LocalDate localDate = DateTimeUtils.parseStringToDate(date);
                    if (localDate.isEqual(new LocalDate())) {
                        timeValidation = DialogTime.TimeValidation.RESTRICT_PREVIOUS_TIME;
                    }
                    ((BaseActivity) getActivity()).showDialog(
                            DialogTime.FRAGMENT_TAG,
                            ToDoDetailsFragment.this,
                            true,
                            timeValidation,
                            mTextViewEndTime.getText().toString());
                    break;
                case R.id.layout_gls_where_to_do_details_fragment:
                    Intent intent = new Intent(getActivity(), MapActivity.class);

                    if (mLocation != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(MapActivity.COORDINATES, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
                        intent.putExtras(bundle);
                    }
                    startActivityForResult(intent, 123);
                    break;
                case R.id.btn_save_to_do:
                    //  getServer();
                    onSaveToDo();
                    break;
            }
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_to_do_details, container, false);
        mEditTextToDoName = (EditText) view.findViewById(R.id.et_to_do_details_fragment);
        mDescription = (EditText) view.findViewById(R.id.et_to_do_description_fragment_details);
        mTextViewEndDate = (TextView) view.findViewById(R.id.tv_when_date_to_do_details_fragment);
        mLayoutWhenDate = (LinearLayout) view.findViewById(R.id.layout_dp_when_to_do_details_fragment);
        mLayoutWhenDate.setOnClickListener(mClickListener);
        mTextViewEndTime = (TextView) view.findViewById(R.id.tv_end_time_to_do_details_fragment);
        mLayoutWhenTime = (LinearLayout) view.findViewById(R.id.layout_tp_when_to_do_details_fragment);
        mLayoutWhenTime.setOnClickListener(mClickListener);
        mLayoutGls = (LinearLayout) view.findViewById(R.id.layout_gls_where_to_do_details_fragment);
        mLayoutGls.setOnClickListener(mClickListener);
        mTextViewGls = (TextView) view.findViewById(R.id.tv_gls);
        mPriorityRadioGroup = (RadioGroup) view.findViewById(R.id.priority_radio_group);
        mPriorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_low:
                        mPriority = LOW_PRIORITY;
                        break;
                    case R.id.radio_button_medium:
                        mPriority = MEDIUM_PRIORITY;
                        break;
                    case R.id.radio_button_high:
                        mPriority = HIGH_PRIORITY;
                        break;
                }
            }
        });
        mNotificationSwitch = (Switch) view.findViewById(R.id.switch_to_do_details_fragment);
        mNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TextUtils.isEmpty(mTextViewEndTime.getText())) {
                        mTextViewTimeWarning.setVisibility(View.VISIBLE);
                        mTextViewTimeWarning.setText("First choose correct time to set notification!");
                        mNotificationSwitch.setChecked(false);

                    }
                }
            }
        });
        mBtnSave = (Button) view.findViewById(R.id.btn_save_to_do);
        mBtnSave.setOnClickListener(mClickListener);
        mTextViewDateWarning = (TextView) view.findViewById(R.id.tv_warning_when_date_to_do_details_fragment);
        mTextViewTimeWarning = (TextView) view.findViewById(R.id.tv_warning_when_time_to_do_details_fragment);
        if (getArguments() != null && getData() == null) {
            String id = getArguments().getString("id");
            setData(ToDoDbService.getInstance().getTodoById(id));
        }

        populateToDo();

        return view;
    }

    private void populateToDo() {
        if (getData() != null) {
            ToDo currentToDo = (ToDo) getData();
            mEditTextToDoName.setText(currentToDo.getToDo());
            mDescription.setText(currentToDo.getDescription());
            mPriority = currentToDo.getPriority();
            switch (mPriority) {
                case LOW_PRIORITY:
                    mPriorityRadioGroup.check(R.id.radio_button_low);
                    break;
                case MEDIUM_PRIORITY:
                    mPriorityRadioGroup.check(R.id.radio_button_medium);
                    break;
                case HIGH_PRIORITY:
                    mPriorityRadioGroup.check(R.id.radio_button_high);
                    break;
                default:
                    mPriorityRadioGroup.check(R.id.radio_button_low);
                    break;
            }
            if (DateTimeUtils.isBeforeToday(DateTimeUtils.parseStringToDate(currentToDo.getEndDate()))) {
                mTextViewDateWarning.setVisibility(View.VISIBLE);
                mTextViewTimeWarning.setVisibility(View.VISIBLE);
            }
            mTextViewEndDate.setText(currentToDo.getEndDate());
            if (!TextUtils.isEmpty(currentToDo.getEndTime())) {
                if (DateTimeUtils.isTimeBefore(DateTimeUtils.parseStringToTime(currentToDo.getEndTime()))) {
                    mTextViewTimeWarning.setVisibility(View.VISIBLE);
                }
            }
            mTextViewEndTime.setText(currentToDo.getEndTime());
            mNotificationSwitch.setChecked(currentToDo.isSendNotification());
            if (currentToDo.getLocation() != null) {
                mLocation = currentToDo.getLocation();
                mTextViewGls.setText(currentToDo.getLocation().getAddress());
            }

        } else {
            String today = DateTimeUtils.parseDateToString(new LocalDate());
            mTextViewEndDate.setText(today);
        }
    }

    @Override
    public void setEndDate(String date) {
        if (isVisible()) {
            mTextViewEndDate.setText(date);
            mTextViewDateWarning.setVisibility(View.GONE);
            mTextViewTimeWarning.setVisibility(View.GONE);
            LocalDate currentDate = DateTimeUtils.parseStringToDate(mTextViewEndDate.getText().toString());
//            if (currentDate.isAfter(currentDate)) {
//
//            }
//            String textViewTime = mTextViewEndTime.getText().toString();
//            LocalTime currentTime = DateTimeUtils.parseStringToTime(textViewTime);

            if (currentDate.isBefore(new LocalDate()) && (DateTimeUtils.parseStringToTime(mTextViewEndTime.getText().toString()))
                    .isBefore(new LocalTime())) {
                mTextViewTimeWarning.setText(R.string.warning_incorrect_time_to_do_details_fragment);
                mTextViewTimeWarning.setVisibility(View.VISIBLE);
            } else {
                mTextViewTimeWarning.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void setEndTime(String endTime) {
        if (isVisible()) {
            LocalTime currentTime = DateTimeUtils.parseStringToTime(endTime);
            LocalDate currentDate = DateTimeUtils.parseStringToDate(mTextViewEndDate.getText().toString());
            if (currentDate.isBefore(new LocalDate()) && currentTime.isBefore(new LocalTime())) {
                mTextViewTimeWarning.setVisibility(View.VISIBLE);
            } else {
                mTextViewTimeWarning.setVisibility(View.GONE);
            }
            mTextViewEndTime.setText(endTime);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String address = bundle.getString(MapActivity.ADDRESS);
                LatLng locationCoordinates = bundle.getParcelable(MapActivity.COORDINATES);
                if (mLocation == null) {
                    mLocation = new Location();
                }
                mLocation.setAddress(address);
                mLocation.setLatitude(locationCoordinates.latitude);
                mLocation.setLongitude(locationCoordinates.longitude);
                if (!TextUtils.isEmpty(address)) {
                    mTextViewGls.setText(address);
                }
            }
        }
    }

    private void onSaveToDo() {
        ToDo currentToDo = new ToDo();
        String toDo = mEditTextToDoName.getText().toString();
        String description = mDescription.getText().toString();
        currentToDo.setToDo(toDo);
        currentToDo.setDescription(description);
        currentToDo.setPriority(mPriority);
        currentToDo.setEndDate(mTextViewEndDate.getText().toString());
        currentToDo.setEndTime(mTextViewEndTime.getText().toString());
        currentToDo.setSendNotification(mNotificationSwitch.isChecked());
        currentToDo.setLocation(mLocation);
        if (getData() != null) {
            if (MapUtils.isDataChanged(JsonUtils.objectToMap(getData()), JsonUtils.objectToMap(currentToDo))) {
                if (isDataValid()) {
                    saveData();
                }
            }
            ((BaseActivity) getActivity()).replaceFragment(R.id.fragment_container, ToDoListFragment.FRAGMENT_TAG, null, null);
        } else {
            if (isDataValid()) {
                saveData();
                ((BaseActivity) getActivity()).replaceFragment(R.id.fragment_container, ToDoListFragment.FRAGMENT_TAG, null, null);
            }
        }
    }

    private void saveData() {
        ToDo currentToDo = (ToDo) getData();
        if (currentToDo == null) {
            currentToDo = new ToDo();
        }
        String toDoName = mEditTextToDoName.getText().toString();
        String description = mDescription.getText().toString();
        String endDate = (String) mTextViewEndDate.getText();
        currentToDo.setToDo(toDoName);
        currentToDo.setDescription(description);
        currentToDo.setPriority(mPriority);
        currentToDo.setEndDate(endDate);
        String endTime = (String) mTextViewEndTime.getText();
        currentToDo.setEndTime(endTime);
        currentToDo.setLocation(mLocation);
        if (currentToDo.getLocation() != null) {
            setupGeofence();
        }
        if (mNotificationSwitch.isChecked()) {
            currentToDo.setSendNotification(true);
        } else {
            ((BaseActivity) getActivity()).stopNotificationAlarm(currentToDo.getNotificationId(), ToDoNotificationReceiver.class);
            currentToDo.setNotificationId(0);
        }
        String todoId = mToDoDbService.saveToDo(currentToDo);
        if (mNotificationSwitch.isChecked()) {
            currentToDo.setSendNotification(true);
            int notificationId = generateUUID();
            currentToDo.setNotificationId(notificationId);
            buildNotification(notificationId, todoId);
        } else if (currentToDo.getNotificationId() > 0) {
            ((BaseActivity) getActivity()).stopNotificationAlarm(currentToDo.getNotificationId(), ToDoNotificationReceiver.class);
            currentToDo.setNotificationId(0);
        }
        Toast.makeText(getContext(), "To Do is saved", Toast.LENGTH_SHORT).show();
    }

    private boolean isDataValid() {
        boolean isValid = true;
        String toDo = mEditTextToDoName.getText().toString();
        if (TextUtils.isEmpty(toDo)) {
            isValid = false;
            mEditTextToDoName.setError("Enter To Do");
        }
        return isValid;
    }

    private int generateUUID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

    private void buildNotification(int id, String todoId) {
        String toDo = mEditTextToDoName.getText().toString();
        String title = getString(R.string.expire_to_do_notification_title);
        String description = String.format(getString(R.string.expire_to_do_notification_content), toDo);
        String currentDate = mTextViewEndDate.getText().toString();
        LocalDate localDate = DateTimeUtils.parseStringToDate(currentDate);
        String currentTime = mTextViewEndTime.getText().toString();
        LocalTime localTime = DateTimeUtils.parseStringToTime(currentTime);
        LocalDateTime localDateTime = new LocalDateTime(localDate.getYear(),
                localDate.getMonthOfYear(), localDate.getDayOfMonth(), localTime.getHourOfDay(), localTime.getMinuteOfHour());
        ((BaseActivity) getActivity()).scheduleNotification(id, title, description
                , R.drawable.ic_action_warning, localDateTime.minusMinutes(1).toDate().getTime(), todoId, FRAGMENT_TAG, ToDoNotificationReceiver.class);
    }

    private void setupGeofence() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        GeofencingClient geofencingClient = LocationHelper.getGeofencingClient(getActivity());
        Geofence geofence = LocationHelper.createGeofences(mLocation.getLatitude(), mLocation.getLongitude(),
                LocationHelper.DEFAULT_RADIUS_IN_METERS, "kjdfjakljf");
        Intent intent = new Intent(getActivity(), GeofenceNotificationService.class);
        Notification notification = NotificationHelper.buildNotification(getActivity(), 1,
                "This is your destination", "fdaf", R.drawable.ic_action_warning);
        intent.putExtra("notification", notification);
        intent.putExtra("notificationId", 1);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        GeofencingRequest geofencingRequest = LocationHelper.createGeofenceRequest(geofence);

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

}
