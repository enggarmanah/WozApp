package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.NewPlaceActivity;
import id.urbanwash.wozapp.activity.SelectPlaceActivity;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.NewOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.model.ServiceAreaBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by apridosandyasa on 3/7/16.
 */
@SuppressLint("ValidFragment")
public class NewOrderSchedulePickupFragment extends BaseFragment {

    private View rootView;

    private RelativeLayout mPlacePanel;
    private RelativeLayout mNewPlacePanel;

    private AppCompatTextView mAddNewPlaceLabel;

    private AppCompatImageView mPlaceImage;
    private AppCompatTextView mPlaceNameLabel;
    private AppCompatTextView mPlaceAddressLabel;

    private AppCompatTextView mInfoLabel;
    private AppCompatTextView mScheduleLabel;

    private RelativeLayout mSchedulePanel;
    private RelativeLayout mScheduleTimePanel;
    private LinearLayout mNotePanel;

    private AppCompatTextView mMonthLabel;
    private AppCompatTextView mDateLabel;
    private AppCompatTextView mDayLabel;

    private AppCompatImageView mPrevDayButton;
    private AppCompatImageView mNextDayButton;

    private AppCompatEditText mNoteText;
    private AppCompatTextView mNoteOkButton;

    private AppCompatActivity mAppCompatActivity;
    private NewOrderListener mNewOrderListener;

    private AbstractWheel mCollectionTimeWheel;
    private ArrayWheelAdapter<String> mCollectionTimeAdapter;
    private Map<Date, List<Long>> mCollectionDateTimes;
    private String[] mCollectionTimes = {""};
    private List<Long> mAvailableTimes;

    OrderBean mOrderBean;

    private Date mCollectionDate;
    private Date mMinCollectionDate;
    private Date mMaxCollectionDate;

    private static final int NEW_PLACE = 0;
    private static final int SELECT_PLACE = 1;

    boolean mIsScheduleAvailable = false;

    public NewOrderSchedulePickupFragment() {
    }

    public NewOrderSchedulePickupFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mNewOrderListener = (NewOrderListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_new_order_schedule_pickup, container, false);

        mPlacePanel = (RelativeLayout) rootView.findViewById(R.id.panel_place);
        mPlaceImage = (AppCompatImageView) rootView.findViewById(R.id.image_place);
        mPlaceNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_name);
        mPlaceAddressLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_address);

        mNewPlacePanel = (RelativeLayout) rootView.findViewById(R.id.panel_new_place);
        mAddNewPlaceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_add_new_place);

        mInfoLabel = (AppCompatTextView) rootView.findViewById(R.id.label_info);
        mScheduleLabel = (AppCompatTextView) rootView.findViewById(R.id.label_schedule);

        mSchedulePanel = (RelativeLayout) rootView.findViewById(R.id.panel_schedule);
        mScheduleTimePanel = (RelativeLayout) rootView.findViewById(R.id.panel_schedule_time);
        mNotePanel = (LinearLayout) rootView.findViewById(R.id.panel_note);

        mMonthLabel = (AppCompatTextView) rootView.findViewById(R.id.label_month);
        mDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_description);
        mDayLabel = (AppCompatTextView) rootView.findViewById(R.id.label_day);

        mPrevDayButton = (AppCompatImageView) rootView.findViewById(R.id.button_prev_day);
        mNextDayButton = (AppCompatImageView) rootView.findViewById(R.id.button_next_day);

        mCollectionTimeWheel = (AbstractWheel) rootView.findViewById(R.id.wheel_collection_time);
        mNoteText = (AppCompatEditText) rootView.findViewById(R.id.text_note);
        mNoteOkButton = (AppCompatTextView) rootView.findViewById(R.id.button_note_ok);

        mPrevDayButton.setOnClickListener(getPrevDayButtonOnClickListener());
        mNextDayButton.setOnClickListener(getNextDayButtonOnClickListener());

        mNoteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteText.setFocusableInTouchMode(true);
                mNoteText.requestFocus();
                showSoftKeyboard(mNoteText);
                mNoteOkButton.setVisibility(View.VISIBLE);
            }
        });

        mNoteText.setFocusable(false);

        mNoteOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(mNoteText);
                mNoteOkButton.setVisibility(View.GONE);
            }
        });

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        initView();

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        refreshSchedule();
    }

    private void initView() {

        mOrderBean = Session.getOrder();

        if (CommonUtil.isEmpty(mOrderBean.getPlaceAddress())) {

            PlaceBean defaultPlace = Session.getDefaultPlace();

            System.out.println("default place : " + defaultPlace);

            if (defaultPlace != null) {

                mOrderBean.setPlaceType(defaultPlace.getType());
                mOrderBean.setPlaceName(defaultPlace.getName());
                mOrderBean.setPlaceAddress(defaultPlace.getAddress());
                mOrderBean.setLat(defaultPlace.getLat());
                mOrderBean.setLng(defaultPlace.getLng());
            }
        }

        refreshPlaceInfo();

        mPlacePanel.setOnClickListener(getPlacePanelOnClickListener());
        mNewPlacePanel.setOnClickListener(getNewPlacePanelOnClickListener());

        mMinCollectionDate = CommonUtil.getCurrentDate();
        mMaxCollectionDate = new Date(mMinCollectionDate.getTime() + (Constant.MAX_COLLECTION_DATE - 1) * Constant.TIME_DAY);

        mCollectionDate = mOrderBean.getCollectionDate() != null ? CommonUtil.getDateWithoutTime(mOrderBean.getCollectionDate()) : CommonUtil.getCurrentDate();

        mCollectionTimeAdapter = new ArrayWheelAdapter<String>(mAppCompatActivity, mCollectionTimes);
        mCollectionTimeAdapter.setItemResource(R.layout.content_new_order_schedule_pickup_time);
        mCollectionTimeAdapter.setItemTextResource(R.id.text_title);

        mCollectionTimeWheel.setViewAdapter(mCollectionTimeAdapter);
        mCollectionTimeWheel.setCurrentItem(0);

        refreshCollectionDate();
        refreshPrevNextDayButtonImage();
        refreshCollectionTime();
    }

    public void refreshCollectionDateTimes() {

        mCollectionDateTimes = Session.getCollectionDateTimes();
        mAvailableTimes = mCollectionDateTimes.get(mCollectionDate);

        refreshCollectionTime();
    }

    private void refreshPlaceInfo() {

        Drawable drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        if (Constant.PLACE_TYPE_HOME.equals(mOrderBean.getPlaceType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        } else if (Constant.PLACE_TYPE_APARTMENT.equals(mOrderBean.getPlaceType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_apartment);

        } else if (Constant.PLACE_TYPE_OFFICE.equals(mOrderBean.getPlaceType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_office);
        }

        mPlaceImage.setImageDrawable(drawable);

        if (!CommonUtil.isEmpty(mOrderBean.getPlaceName())) {
            mPlaceNameLabel.setText(mOrderBean.getPlaceName());
            mPlaceNameLabel.setVisibility(View.VISIBLE);
        } else {
            mPlaceNameLabel.setVisibility(View.GONE);
        }

        mPlaceAddressLabel.setText(mOrderBean.getPlaceAddress());
    }

    private void refreshCollectionTime() {

        List<Long> availableTimes = new ArrayList<Long>();

        if (mAvailableTimes != null) {

            for (Long time : mAvailableTimes) {

                if (new Date().getTime() + Constant.EARLIEST_HOUR_FOR_ORDER * Constant.TIME_HOUR < mCollectionDate.getTime() + time) {
                    availableTimes.add(time);
                }
            }

            mCollectionTimes = new String[availableTimes.size()];

            int i = 0;

            for (Long time : availableTimes) {

                mCollectionTimes[i] = CommonUtil.getTimeDesc(time);
                i++;
            }

            mCollectionTimeAdapter = new ArrayWheelAdapter<String>(mAppCompatActivity, mCollectionTimes);
            mCollectionTimeAdapter.setItemResource(R.layout.content_new_order_schedule_pickup_time);
            mCollectionTimeAdapter.setItemTextResource(R.id.text_title);

            mCollectionTimeWheel.setViewAdapter(mCollectionTimeAdapter);
            mCollectionTimeWheel.setCurrentItem(0);

            mAvailableTimes = availableTimes;

            if (mAvailableTimes.size() == 0) {
                selectNextDay();
            }
        }
    }

    private void refreshCollectionDate() {

        mMonthLabel.setText(CommonUtil.formatDate(mCollectionDate, "MMMM"));
        mDateLabel.setText(CommonUtil.formatDate(mCollectionDate, "dd"));
        mDayLabel.setText(CommonUtil.formatDate(mCollectionDate, "EEEE"));
    }

    private void refreshPrevNextDayButtonImage() {

        mPrevDayButton.setImageResource(R.drawable.icon_prev_grey);
        mNextDayButton.setImageResource(R.drawable.icon_next_grey);

        mPrevDayButton.setEnabled(false);
        mNextDayButton.setEnabled(false);

        if (mMinCollectionDate.getTime() < mCollectionDate.getTime()) {
            mPrevDayButton.setImageResource(R.drawable.icon_prev);
            mPrevDayButton.setEnabled(true);
        }

        if (mCollectionDate.getTime() < mMaxCollectionDate.getTime()) {
            mNextDayButton.setImageResource(R.drawable.icon_next);
            mNextDayButton.setEnabled(true);
        }
    }

    public void setScheduleAvailable(boolean isScheduleAvailable) {

        mIsScheduleAvailable = isScheduleAvailable;

        refreshSchedule();
    }

    public void refreshSchedule() {

        if (!isAdded()) {
            return;
        }

        if (mIsScheduleAvailable) {

            mInfoLabel.setVisibility(View.GONE);
            mScheduleLabel.setVisibility(View.VISIBLE);

            mSchedulePanel.setVisibility(View.VISIBLE);
            mScheduleTimePanel.setVisibility(View.VISIBLE);
            mNotePanel.setVisibility(View.VISIBLE);

        } else {

            mInfoLabel.setVisibility(View.VISIBLE);
            mScheduleLabel.setVisibility(View.GONE);

            mSchedulePanel.setVisibility(View.GONE);
            mScheduleTimePanel.setVisibility(View.GONE);
            mNotePanel.setVisibility(View.GONE);
        }

        mInfoLabel.setText(getString(R.string.alert_out_of_service_area));
        mAddNewPlaceLabel.setText(getString(R.string.schedule_add_address));
        mPlacePanel.setVisibility(View.VISIBLE);

        if (Session.getPlaces().size() == 0) {

            mInfoLabel.setText(getString(R.string.alert_no_location_within_service_area));
            mAddNewPlaceLabel.setText(getString(R.string.schedule_select_other_address));
            mPlacePanel.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getPlacePanelOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAppCompatActivity, SelectPlaceActivity.class);
                startActivityForResult(intent, SELECT_PLACE);
            }
        };
    }

    private View.OnClickListener getNewPlacePanelOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAppCompatActivity, NewPlaceActivity.class);
                startActivityForResult(intent, NEW_PLACE);
            }
        };
    }

    private View.OnClickListener getPrevDayButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPrevDay();
            }
        };
    }

    private View.OnClickListener getNextDayButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectNextDay();
            }
        };
    }

    private void selectPrevDay() {

        if (mMinCollectionDate.getTime() < mCollectionDate.getTime()) {

            mCollectionDate.setTime(mCollectionDate.getTime() - Constant.TIME_DAY);
            refreshCollectionDate();
            refreshPrevNextDayButtonImage();

            mAvailableTimes = mCollectionDateTimes.get(mCollectionDate);
            refreshCollectionTime();
        }
    }

    private void selectNextDay() {

        Date collectionDate = CommonUtil.getDateWithoutTime(mCollectionDate);

        if (collectionDate.getTime() < mMaxCollectionDate.getTime()) {

            mCollectionDate.setTime(mCollectionDate.getTime() + Constant.TIME_DAY);
            refreshCollectionDate();
            refreshPrevNextDayButtonImage();

            mAvailableTimes = mCollectionDateTimes.get(mCollectionDate);
            refreshCollectionTime();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if ((requestCode == SELECT_PLACE) || (requestCode == NEW_PLACE)) {

                PlaceBean placeBean = Session.getPlace();

                mOrderBean.setPlaceType(placeBean.getType());
                mOrderBean.setPlaceName(placeBean.getName());
                mOrderBean.setPlaceAddress(placeBean.getAddress());
                mOrderBean.setLat(placeBean.getLat());
                mOrderBean.setLng(placeBean.getLng());

                refreshPlaceInfo();

                mNewOrderListener.onChangePlace(placeBean);
            }
        }
    }

    public void saveCollectionTime() {

        long selectedTime = mAvailableTimes.get(mCollectionTimeWheel.getCurrentItem());

        Date collectionDate = new Date(mCollectionDate.getTime() + selectedTime);

        if (mOrderBean.getCollectionDate() != null && mOrderBean.getCollectionDate().getTime() != collectionDate.getTime()) {

            // reset speed type & delivery date
            mOrderBean.setSpeedType(null);
            mOrderBean.setDeliveryDate(null);
        }

        mOrderBean.setCollectionDate(collectionDate);
        mOrderBean.setNote(mNoteText.getText().toString());
    }
}
