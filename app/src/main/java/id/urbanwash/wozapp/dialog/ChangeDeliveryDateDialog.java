package id.urbanwash.wozapp.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.listener.NewOrderListener;
import id.urbanwash.wozapp.listener.OrderTrackingListener;
import id.urbanwash.wozapp.listener.ProcessOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class ChangeDeliveryDateDialog extends AppCompatDialogFragment {

    private AppCompatActivity mAppCompatActivity;

    private NewOrderListener mNewOrderListener;
    private OrderTrackingListener mOrderTrackingListener;
    private ProcessOrderListener mProcessOrderListener;

    private View rootView;

    private AppCompatTextView mMonthLabel;
    private AppCompatTextView mDateLabel;
    private AppCompatTextView mDayLabel;

    private AppCompatImageView mPrevDayButton;
    private AppCompatImageView mNextDayButton;

    private AppCompatImageView mCloseButton;
    private AppCompatButton mSaveButton;

    private AbstractWheel mDeliveryTimeWheel;

    private ArrayWheelAdapter<String> mDeliveryTimeAdapter;
    private Map<Date, List<Long>> mDeliveryDateTimes;
    private String[] mDeliveryTimes = {""};
    private List<Long> mAvailableTimes;

    private OrderBean mOrderBean;

    private Date mDeliveryDate;
    private Date mMinDeliveryDate;
    private Date mMaxDeliveryDate;

    public ChangeDeliveryDateDialog() {
    }

    public ChangeDeliveryDateDialog(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;

        if (mAppCompatActivity instanceof NewOrderListener) {
            mNewOrderListener = (NewOrderListener) mAppCompatActivity;

        } else if (mAppCompatActivity instanceof OrderTrackingListener) {
            mOrderTrackingListener = (OrderTrackingListener) mAppCompatActivity;

        } else if (mAppCompatActivity instanceof ProcessOrderListener) {
            mProcessOrderListener = (ProcessOrderListener) mAppCompatActivity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Override
    public void onStart() {

        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = (int) (Utility.DisplayUtilty.getDisplayMetricFromWindow(mAppCompatActivity).widthPixels * 0.95);
        windowParams.height = ActionBar.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = Gravity.CENTER;
        window.setAttributes(windowParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.dialog_change_delivery_date, container, false);

        mCloseButton = (AppCompatImageView) rootView.findViewById(R.id.button_close);
        mDeliveryTimeWheel = (AbstractWheel) rootView.findViewById(R.id.wheel_delivery_time);
        mSaveButton = (AppCompatButton) rootView.findViewById(R.id.button_save);

        mMonthLabel = (AppCompatTextView) rootView.findViewById(R.id.label_month);
        mDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_description);
        mDayLabel = (AppCompatTextView) rootView.findViewById(R.id.label_day);

        mPrevDayButton = (AppCompatImageView) rootView.findViewById(R.id.button_prev_day);
        mNextDayButton = (AppCompatImageView) rootView.findViewById(R.id.button_next_day);

        mDeliveryTimeAdapter = new ArrayWheelAdapter<String>(mAppCompatActivity, mDeliveryTimes);
        mDeliveryTimeAdapter.setItemResource(R.layout.content_new_order_schedule_pickup_time);
        mDeliveryTimeAdapter.setItemTextResource(R.id.text_title);
        mDeliveryTimeWheel.setViewAdapter(mDeliveryTimeAdapter);
        mDeliveryTimeWheel.setCurrentItem(0);

        mCloseButton.setOnClickListener(getCloseButtonOnClickListener());
        mSaveButton.setOnClickListener(getSaveButtonOnClickListener());

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        initView();

        return rootView;
    }

    private void initView() {

        mOrderBean = Session.getOrder();

        mMinDeliveryDate = CommonUtil.getDateWithoutTime(mOrderBean.getDeliveryDate());
        mMaxDeliveryDate = CommonUtil.getDateWithoutTime(new Date(mMinDeliveryDate.getTime() + Constant.MAX_DELIVERY_DATE * Constant.TIME_DAY));

        mDeliveryDate = CommonUtil.getDateWithoutTime(mOrderBean.getDeliveryDate());

        mPrevDayButton.setOnClickListener(getPrevDayButtonOnClickListener());
        mNextDayButton.setOnClickListener(getNextDayButtonOnClickListener());

        mDeliveryDateTimes = Session.getDeliveryDateTimes();
        mAvailableTimes = mDeliveryDateTimes.get(mDeliveryDate);

        refreshDeliveryDate();
        refreshDeliveryTime();

        refreshPrevNextDayButtonImage();
    }

    private void refreshDeliveryDate() {

        mMonthLabel.setText(CommonUtil.formatDate(mDeliveryDate, "MMMM"));
        mDateLabel.setText(CommonUtil.formatDate(mDeliveryDate, "dd"));
        mDayLabel.setText(CommonUtil.formatDate(mDeliveryDate, "EEEE"));
    }

    private void refreshDeliveryTime() {

        List<Long> availableTimes = new ArrayList<Long>();

        Date minProcessingTime = new Date(mOrderBean.getCollectionDate().getTime() + Constant.MIN_PROCESSING_TIME_REQUIRED);

        if (mAvailableTimes != null) {

            for (Long time : mAvailableTimes) {

                if (mDeliveryDate.getTime() + time >= minProcessingTime.getTime()) {
                    availableTimes.add(time);
                }
            }

            mDeliveryTimes = new String[availableTimes.size()];

            int i = 0;

            for (Long time : availableTimes) {

                mDeliveryTimes[i] = CommonUtil.getTimeDesc(time);
                i++;
            }

            mAvailableTimes = availableTimes;

            if (availableTimes.size() > 0) {

                mDeliveryTimeAdapter = new ArrayWheelAdapter<String>(mAppCompatActivity, mDeliveryTimes);
                mDeliveryTimeAdapter.setItemResource(R.layout.content_new_order_schedule_pickup_time);
                mDeliveryTimeAdapter.setItemTextResource(R.id.text_title);

                mDeliveryTimeWheel.setViewAdapter(mDeliveryTimeAdapter);
                mDeliveryTimeWheel.setCurrentItem(0);

                mDeliveryTimeWheel.setVisibility(View.VISIBLE);

            } else {

                mDeliveryTimeWheel.setVisibility(View.GONE);
            }
        }
    }

    private void refreshPrevNextDayButtonImage() {

        mPrevDayButton.setImageResource(R.drawable.icon_prev_grey);
        mNextDayButton.setImageResource(R.drawable.icon_next_grey);

        mPrevDayButton.setEnabled(false);
        mNextDayButton.setEnabled(false);

        Date deliveryDate = CommonUtil.getDateWithoutTime(mDeliveryDate);

        if (mMinDeliveryDate.getTime() < deliveryDate.getTime()) {
            mPrevDayButton.setImageResource(R.drawable.icon_prev);
            mPrevDayButton.setEnabled(true);
        }

        if (deliveryDate.getTime() < mMaxDeliveryDate.getTime()) {
            mNextDayButton.setImageResource(R.drawable.icon_next);
            mNextDayButton.setEnabled(true);
        }
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

        if (mMinDeliveryDate.getTime() < mDeliveryDate.getTime()) {

            mDeliveryDate.setTime(mDeliveryDate.getTime() - Constant.TIME_DAY);
            refreshDeliveryDate();
            refreshPrevNextDayButtonImage();

            mAvailableTimes = mDeliveryDateTimes.get(mDeliveryDate);
            refreshDeliveryTime();
        }
    }

    private void selectNextDay() {

        if (mDeliveryDate.getTime() < mMaxDeliveryDate.getTime()) {

            mDeliveryDate.setTime(mDeliveryDate.getTime() + Constant.TIME_DAY);
            refreshDeliveryDate();
            refreshPrevNextDayButtonImage();

            mAvailableTimes = mDeliveryDateTimes.get(mDeliveryDate);
            refreshDeliveryTime();
        }
    }

    private View.OnClickListener getCloseButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNewOrderListener != null) {
                    mNewOrderListener.onDeliveryDateUpdated();

                }

                dismiss();
            }
        };
    }

    private View.OnClickListener getSaveButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long selectedTime = 0l;

                try {
                    selectedTime = mAvailableTimes.get(mDeliveryTimeWheel.getCurrentItem());

                } catch (IndexOutOfBoundsException e) {
                    NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.error_no_available_time));
                    return;
                }

                Date deliveryDate = new Date(mDeliveryDate.getTime() + selectedTime);
                mOrderBean.setDeliveryDate(deliveryDate);

                if (mNewOrderListener != null) {
                    mNewOrderListener.onDeliveryDateUpdated();

                } else if (mOrderTrackingListener != null) {
                    mOrderTrackingListener.onDeliveryDateUpdated();

                } else if (mProcessOrderListener != null) {
                    mProcessOrderListener.onDeliveryDateUpdated();
                }

                dismiss();
            }
        };
    }
}
