package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.OrderDetailAdapter;
import id.urbanwash.wozapp.listener.OrderTrackingListener;
import id.urbanwash.wozapp.dialog.ChangeDeliveryDateDialog;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.ImageManager;

/**
 * Created by apridosandyasa on 3/7/16.
 */
@SuppressLint("ValidFragment")
public class OrderStatusFragment extends Fragment {

    private View rootView;

    private LinearLayout mOrderDetailPanel;
    private LinearLayout mOrderSummaryPanel;

    private AppCompatImageView mOrderDetailExpandButton;
    private RecyclerView mOrderDetailView;
    private LinearLayoutManager linearLayoutManager;
    private OrderDetailAdapter orderDetailAdapter;
    private MagicProgressCircle mOrderStatusCircle;

    private AppCompatImageView mOrderStatusImage;
    private AppCompatImageView mChangeDeliveryButton;
    private AppCompatTextView mOrderStatusLabel;

    private AppCompatTextView mCollectionDayLabel;
    private AppCompatTextView mCollectionDateLabel;
    private AppCompatTextView mCollectionMonthLabel;
    private AppCompatTextView mCollectionTimeLabel;
    private AppCompatTextView mDeliveryDayLabel;
    private AppCompatTextView mDeliveryDateLabel;
    private AppCompatTextView mDeliveryMonthLabel;
    private AppCompatTextView mDeliveryTimeLabel;

    private AppCompatTextView mPlaceNameLabel;
    private AppCompatTextView mPlaceAddressLabel;

    private AppCompatTextView mNoteLabel;

    private AppCompatTextView mSpeedTypeLabel;
    private AppCompatTextView mOrderSummaryLabel;

    private AppCompatTextView mTotalChargeLabel;
    private AppCompatTextView mPaymentTypeLabel;
    private AppCompatTextView mPicNameLabel;

    private LinearLayout mPicPanel;

    private CircleImageView mPicImage;
    private LinearLayout mActionPanel;
    private AppCompatImageView mCallButton;
    private AppCompatImageView mSmsButton;
    private AppCompatButton mCancelButton;

    private OrderBean mOrderBean;

    private int mProgress = 0;
    private int mProgressPercentage = 0;
    private int mAnimationTime = 3000;

    private AppCompatActivity mAppCompatActivity;

    private ChangeDeliveryDateDialog mChangeDeliveryDateDialog;

    private OrderTrackingListener mListener;

    public OrderStatusFragment() {}

    public OrderStatusFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mListener = (OrderTrackingListener) appCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChangeDeliveryDateDialog = new ChangeDeliveryDateDialog(mAppCompatActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_order_status, container, false);

        mOrderSummaryPanel = (LinearLayout) rootView.findViewById(R.id.panel_order_summary);
        mOrderDetailPanel = (LinearLayout) rootView.findViewById(R.id.panel_order_detail);

        mOrderStatusCircle = (MagicProgressCircle) rootView.findViewById(R.id.circle_order_status);
        mOrderDetailExpandButton = (AppCompatImageView) rootView.findViewById(R.id.button_order_detail);
        mOrderDetailView = (RecyclerView) rootView.findViewById(R.id.view_order_detail);

        mChangeDeliveryButton = (AppCompatImageView) rootView.findViewById(R.id.button_change_delivery);
        mOrderStatusImage = (AppCompatImageView) rootView.findViewById(R.id.image_order_status);

        mOrderStatusLabel = (AppCompatTextView) rootView.findViewById(R.id.label_order_status);

        mCollectionDayLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_day);
        mCollectionDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_date);
        mCollectionMonthLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_month);
        mCollectionTimeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_time);
        mDeliveryDayLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_day);
        mDeliveryDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_date);
        mDeliveryMonthLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_month);
        mDeliveryTimeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_time);

        mPlaceNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_name);
        mPlaceAddressLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_address);

        mSpeedTypeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_speed_type);
        mOrderSummaryLabel = (AppCompatTextView) rootView.findViewById(R.id.label_order_summary);

        mNoteLabel = (AppCompatTextView) rootView.findViewById(R.id.label_note);

        mPaymentTypeLabel = (AppCompatTextView) rootView.findViewById(R.id.button_payment_type);
        mTotalChargeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_total_charge);
        mPicNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_pic_name);

        mPicPanel = (LinearLayout) rootView.findViewById(R.id.panel_pic);
        mPicImage = (CircleImageView) rootView.findViewById(R.id.image_pic);

        mActionPanel = (LinearLayout) rootView.findViewById(R.id.panel_action);

        mCallButton = (AppCompatImageView) rootView.findViewById(R.id.button_call);
        mSmsButton = (AppCompatImageView) rootView.findViewById(R.id.button_sms);
        mCancelButton = (AppCompatButton) rootView.findViewById(R.id.button_cancel);

        mChangeDeliveryButton.setOnClickListener(getChangeDeliveryButtonOnClickListener());
        mPaymentTypeLabel.setOnClickListener(getPaymentTypeButtonOnClickListener());
        mCancelButton.setOnClickListener(getCancelButtonOnClickListener());

        linearLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mOrderDetailView.setHasFixedSize(true);
        mOrderDetailView.setLayoutManager(linearLayoutManager);

        orderDetailAdapter = new OrderDetailAdapter(mAppCompatActivity, Session.getOrder());
        mOrderDetailView.setAdapter(orderDetailAdapter);

        mOrderSummaryPanel.setOnClickListener(getExpandOnClickListener());
        mOrderStatusCircle.setSmoothPercent(0f);

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        refreshView();
    }

    public void refreshView() {

        mOrderBean = Session.getOrder();

        mOrderStatusLabel.setText(CommonUtil.getCircleOrderStatusLabel(mOrderBean.getStatus()));

        String day = CommonUtil.formatDay(mOrderBean.getCollectionDate()) + ",";
        String date = CommonUtil.formatDateOfMonth(mOrderBean.getCollectionDate());
        String month = CommonUtil.formatMonth(mOrderBean.getCollectionDate());
        String time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getCollectionDate());

        mCollectionDayLabel.setText(day);
        mCollectionDateLabel.setText(date);
        mCollectionMonthLabel.setText(month);
        mCollectionTimeLabel.setText(time);

        mChangeDeliveryButton.setVisibility(View.GONE);

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {

            mChangeDeliveryButton.setVisibility(View.VISIBLE);
        }

        if (!CommonUtil.isEmpty(mOrderBean.getPlaceName())) {
            mPlaceNameLabel.setText(mOrderBean.getPlaceName());
            mPlaceNameLabel.setVisibility(View.VISIBLE);
        } else {
            mPlaceNameLabel.setVisibility(View.GONE);
        }

        if (!CommonUtil.isEmpty(mOrderBean.getNote())) {
            mNoteLabel.setText("* " + mOrderBean.getNote());
            mNoteLabel.setVisibility(View.VISIBLE);
        } else {
            mNoteLabel.setVisibility(View.GONE);
        }

        mPlaceAddressLabel.setText(mOrderBean.getPlaceAddress());

        mSpeedTypeLabel.setText(CodeUtil.getSpeedTypeLabel(mOrderBean.getSpeedType()));
        mOrderSummaryLabel.setText(CommonUtil.getOrderProductInfo(mOrderBean));

        mTotalChargeLabel.setText(CommonUtil.formatCurrency(mOrderBean.getTotalCharge()));

        refreshDeliveryAndPaymentInfo();
        refreshPicInfo();

        mOrderStatusLabel.setText(CommonUtil.getCircleOrderStatusLabel(mOrderBean.getStatus()));

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                mAppCompatActivity.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {

                        mOrderStatusCircle.setSmoothPercent(0f);
                        mOrderStatusCircle.setSmoothPercent(CommonUtil.getProgressByStatus(mOrderBean.getStatus()),
                                (long) (CommonUtil.getProgressByStatus(mOrderBean.getStatus()) * mAnimationTime));

                    }
                });
            }
        }, 1000);

        mProgress = 0;

        final Timer progressTimer = new Timer();

        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                System.out.println("Progress : " + mProgressPercentage);
                //System.out.println("getProgressByStatus : " + CommonUtil.getProgressByStatus(mOrderBean.getStatus()) * 100);

                if (mProgressPercentage < Math.round(CommonUtil.getProgressByStatus(mOrderBean.getStatus()) * 100)) {

                    final Drawable drawable = CommonUtil.getCircleOrderProgressImage(mProgressPercentage);

                    mAppCompatActivity.runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {

                            if (drawable != null) {
                                mOrderStatusImage.setImageDrawable(drawable);
                            }
                        }
                    });

                } else if (mProgressPercentage == Math.round(CommonUtil.getProgressByStatus(mOrderBean.getStatus()) * 100)) {

                    mAppCompatActivity.runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {

                            mOrderStatusImage.setImageDrawable(CommonUtil.getCircleOrderStatusImage(mOrderBean.getStatus()));
                        }
                    });

                } else {

                    progressTimer.cancel();
                    progressTimer.purge();
                }

                mProgressPercentage += 5;

            }
        }, 1000, (mAnimationTime) / 20);
    }

    public void refreshDeliveryAndPaymentInfo() {

        String day = CommonUtil.formatDay(mOrderBean.getDeliveryDate()) + ",";
        String date = CommonUtil.formatDateOfMonth(mOrderBean.getDeliveryDate());
        String month = CommonUtil.formatMonth(mOrderBean.getDeliveryDate());
        String time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getDeliveryDate());

        mDeliveryDayLabel.setText(day);
        mDeliveryDateLabel.setText(date);
        mDeliveryMonthLabel.setText(month);
        mDeliveryTimeLabel.setText(time);

        mPaymentTypeLabel.setText(CodeUtil.getPaymentTypeLabel(mOrderBean.getPaymentType()));
    }

    private View.OnClickListener getExpandOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderDetailPanel.isShown()) {
                    mOrderDetailPanel.setVisibility(View.GONE);
                    mOrderDetailExpandButton.setImageResource(R.drawable.icon_arrow_right);
                } else {
                    mOrderDetailPanel.setVisibility(View.VISIBLE);
                    mOrderDetailExpandButton.setImageResource(R.drawable.icon_dropdown);
                }
            }
        };
    }

    private View.OnClickListener getCancelButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onCancelOrder(mOrderBean);
            }
        };
    }

    private void refreshPicInfo() {

        mCancelButton.setVisibility(View.GONE);

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus())) {

            mCancelButton.setVisibility(View.VISIBLE);
        }

        EmployeeBean picBean = null;

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {

            mPicPanel.setVisibility(View.VISIBLE);

            picBean = mOrderBean.getCollectionPic();

            if (picBean != null) {

                mPicNameLabel.setText(picBean.getName());

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(mPicImage, picBean.getImage());
            }

        } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
                Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {

            mPicPanel.setVisibility(View.VISIBLE);

            picBean = mOrderBean.getDeliveryPic();

            if (picBean != null) {

                mPicNameLabel.setText(picBean.getName());

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(mPicImage, picBean.getImage());
            }
        }

        mCallButton.setOnClickListener(getCallButtonOnClickListener(picBean));
        mSmsButton.setOnClickListener(getSmsButtonOnClickListener(picBean));
    }

    public void showChangeDeliveryDateDialog() {

        if (!mChangeDeliveryDateDialog.isAdded()) {

            mChangeDeliveryDateDialog.setCancelable(false);
            mChangeDeliveryDateDialog.show(mAppCompatActivity.getSupportFragmentManager(), Constant.CHANGE_DELIVERY_DATE_DLG_TAG);
        }
    }

    private View.OnClickListener getChangeDeliveryButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onChangeOrderDeliveryDate(mOrderBean);
            }
        };
    }

    private View.OnClickListener getPaymentTypeButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {
                    return;
                }


                String paymentType = Constant.PAYMENT_TYPE_CASH;

                if (Constant.PAYMENT_TYPE_CASH.equals(mOrderBean.getPaymentType())) {
                    paymentType = Constant.PAYMENT_TYPE_WALLET;
                }

                mListener.onChangePaymentType(paymentType);
            }
        };
    }

    private View.OnClickListener getCallButtonOnClickListener(final EmployeeBean employeeBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (employeeBean != null) {
                    mListener.onCall(employeeBean.getName(), employeeBean.getImage(), employeeBean.getMobile());
                } else {
                    mListener.onCall(null, null, null);
                }
            }
        };
    }

    private View.OnClickListener getSmsButtonOnClickListener(final EmployeeBean employeeBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (employeeBean != null) {
                    mListener.onSms(employeeBean.getName(), employeeBean.getImage(), employeeBean.getMobile());
                } else {
                    mListener.onSms(null, null, null);
                }
            }
        };
    }
}
