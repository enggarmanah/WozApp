package id.urbanwash.wozapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.OrderDetailAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ProcessOrderListener;
import id.urbanwash.wozapp.dialog.ChangeDeliveryDateDialog;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.ImageManager;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/7/16.
 */
@SuppressLint("ValidFragment")
public class ProcessOrderActivity extends BaseActivity implements ProcessOrderListener {

    private OrderBean mOrderBean;

    private LinearLayout mOrderDetailPanel;
    private LinearLayout mOrderSummaryPanel;

    private AppCompatImageView mOrderDetailExpandButton;
    private RecyclerView mOrderDetailView;
    private LinearLayoutManager linearLayoutManager;
    private OrderDetailAdapter orderDetailAdapter;

    private AppCompatTextView mTitleLabel;

    private AppCompatTextView mCustomerNameLabel;
    private CircleImageView mProfileImage;

    private AppCompatImageView mCustomerCallButton;
    private AppCompatImageView mCustomerSmsButton;

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
    private AppCompatTextView mStatusLabel;
    private AppCompatTextView mPaymentTypeButton;
    private AppCompatTextView mPicNameLabel;

    private CircleImageView mPicImage;

    private LinearLayout mPicPanel;
    private RelativeLayout mBottomPanel;

    private AppCompatImageView mCallButton;
    private AppCompatImageView mSmsButton;

    private LinearLayout mAssignPicButton;
    private AppCompatTextView mSubmitButton;
    private AppCompatImageView mUpdateOrderButton;
    private AppCompatImageView mChangeDeliveryButton;

    private AppCompatActivity mAppCompatActivity;

    private static final int GET_COLLECTION_PIC = 1;
    private static final int GET_DELIVERY_PIC = 2;
    private static final int UPDATE_ORDER = 3;

    private static final int CONFIRM_SUBMIT = 1;
    private static final int CONFIRM_CHANGE_PAYMENT_TYPE = 2;
    private static final int CONFIRM_REPEAT_ORDER = 3;

    private boolean mIsUpdateProduct = false;
    private boolean mIsRepeatOrder = false;

    private String mOrderStatus;

    private String mPaymentType;
    private String mNewPaymentType;

    private ChangeDeliveryDateDialog mChangeDeliveryDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrderBean = Session.getOrder();

        Session.setDeliveryDateTimes(null);

        mAppCompatActivity = this;

        setContentView(R.layout.activity_process_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);

        mCustomerNameLabel = (AppCompatTextView) findViewById(R.id.label_customer_name);
        mProfileImage = (CircleImageView) findViewById(R.id.image_profile);

        mCustomerCallButton = (AppCompatImageView) findViewById(R.id.button_call_customer);
        mCustomerSmsButton = (AppCompatImageView) findViewById(R.id.button_sms_customer);

        mOrderSummaryPanel = (LinearLayout) findViewById(R.id.panel_order_summary);
        mOrderDetailPanel = (LinearLayout) findViewById(R.id.panel_order_detail);

        mOrderDetailExpandButton = (AppCompatImageView) findViewById(R.id.button_order_detail);
        mOrderDetailView = (RecyclerView) findViewById(R.id.view_order_detail);

        mCollectionDayLabel = (AppCompatTextView) findViewById(R.id.label_collection_day);
        mCollectionDateLabel = (AppCompatTextView) findViewById(R.id.label_collection_date);
        mCollectionMonthLabel = (AppCompatTextView) findViewById(R.id.label_collection_month);
        mCollectionTimeLabel = (AppCompatTextView) findViewById(R.id.label_collection_time);
        mDeliveryDayLabel = (AppCompatTextView) findViewById(R.id.label_delivery_day);
        mDeliveryDateLabel = (AppCompatTextView) findViewById(R.id.label_delivery_date);
        mDeliveryMonthLabel = (AppCompatTextView) findViewById(R.id.label_delivery_month);
        mDeliveryTimeLabel = (AppCompatTextView) findViewById(R.id.label_delivery_time);

        mPlaceNameLabel = (AppCompatTextView) findViewById(R.id.label_place_name);
        mPlaceAddressLabel = (AppCompatTextView) findViewById(R.id.label_place_address);

        mNoteLabel = (AppCompatTextView) findViewById(R.id.label_note);

        mSpeedTypeLabel = (AppCompatTextView) findViewById(R.id.label_speed_type);
        mOrderSummaryLabel = (AppCompatTextView) findViewById(R.id.label_order_summary);

        mPaymentTypeButton = (AppCompatTextView) findViewById(R.id.button_payment_type);
        mTotalChargeLabel = (AppCompatTextView) findViewById(R.id.label_total_charge);
        mStatusLabel = (AppCompatTextView) findViewById(R.id.label_status);
        mPicNameLabel = (AppCompatTextView) findViewById(R.id.label_pic_name);

        mPicImage = (CircleImageView) findViewById(R.id.image_pic);

        mPicPanel = (LinearLayout) findViewById(R.id.panel_pic);
        mBottomPanel = (RelativeLayout) findViewById(R.id.panel_bottom);

        mCallButton = (AppCompatImageView) findViewById(R.id.button_call);
        mSmsButton = (AppCompatImageView) findViewById(R.id.button_sms);

        mAssignPicButton = (LinearLayout) findViewById(R.id.button_assign_pic);
        mSubmitButton = (AppCompatTextView) findViewById(R.id.button_submit);
        mUpdateOrderButton = (AppCompatImageView) findViewById(R.id.button_edit_order);
        mChangeDeliveryButton = (AppCompatImageView) findViewById(R.id.button_change_delivery);

        mPaymentTypeButton.setOnClickListener(getPaymentTypeButtonOnClickListener());

        View.OnClickListener onClickListener = getPlaceInfoOnClickListener();

        mPlaceNameLabel.setOnClickListener(onClickListener);
        mPlaceAddressLabel.setOnClickListener(onClickListener);

        mAssignPicButton.setOnClickListener(getAssignPicOnClickListener());
        mSubmitButton.setOnClickListener(getSubmitButtonOnClickListener());
        mUpdateOrderButton.setOnClickListener(getUpdateOrderOnClickListener());
        mChangeDeliveryButton.setOnClickListener(getChangeDeliveryButtonOnClickListener());

        linearLayoutManager = new LinearLayoutManager(this);
        mOrderDetailView.setHasFixedSize(true);
        mOrderDetailView.setLayoutManager(linearLayoutManager);

        orderDetailAdapter = new OrderDetailAdapter(this, mOrderBean);
        mOrderDetailView.setAdapter(orderDetailAdapter);

        mOrderSummaryPanel.setOnClickListener(getExpandOnClickListener());

        refreshView();

        mChangeDeliveryDateDialog = new ChangeDeliveryDateDialog(mAppCompatActivity);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        refreshView();
    }

    private void refreshView() {

        mOrderBean = Session.getOrder();

        String title = mOrderBean.getId() != null ? "ORDER ID # " + mOrderBean.getOrderNo() : mOrderBean.getOrderNo();

        mTitleLabel.setText(title);

        CustomerBean customerBean = mOrderBean.getCustomer();

        mCustomerNameLabel.setText(customerBean.getName());

        ImageBean customerImageBean = customerBean.getImage();

        ImageManager imageManager = new ImageManager(mAppCompatActivity);
        imageManager.setImage(mProfileImage, customerImageBean);

        mCustomerCallButton.setOnClickListener(getCallButtonOnClickListener(customerBean));
        mCustomerSmsButton.setOnClickListener(getSmsButtonOnClickListener(customerBean));

        String day = CommonUtil.formatDay(mOrderBean.getCollectionDate()) + ", ";
        String date = CommonUtil.formatDateOfMonth(mOrderBean.getCollectionDate());
        String month = CommonUtil.formatMonth(mOrderBean.getCollectionDate());
        String time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getCollectionDate());

        mCollectionDayLabel.setText(day);
        mCollectionDateLabel.setText(date);
        mCollectionMonthLabel.setText(month);
        mCollectionTimeLabel.setText(time);

        day = CommonUtil.formatDay(mOrderBean.getDeliveryDate()) + ", ";
        date = CommonUtil.formatDateOfMonth(mOrderBean.getDeliveryDate());
        month = CommonUtil.formatMonth(mOrderBean.getDeliveryDate());
        time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getDeliveryDate());

        mDeliveryDayLabel.setText(day);
        mDeliveryDateLabel.setText(date);
        mDeliveryMonthLabel.setText(month);
        mDeliveryTimeLabel.setText(time);

        refreshOrderDetail();
        refreshPicInfo();
        refreshPaymentInfo();

        mUpdateOrderButton.setVisibility(View.GONE);

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_assigned_for_collection));

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_collection_in_progress));

        } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus())) {
            mUpdateOrderButton.setVisibility(View.VISIBLE);
            mSubmitButton.setText(getString(R.string.process_order_collected));

        } else if (Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_cleaning_in_progress));

        } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_cleaned));

        } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_assigned_for_delivery));

        } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_delivery_in_progress));

        } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_completed));

        } else if (Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {
            mSubmitButton.setText(getString(R.string.process_order_repeat));
        }

        if (Session.isTransporterUser() && Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {

            mBottomPanel.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshOrderDetail() {

        mChangeDeliveryButton.setVisibility(View.GONE);

        if (Session.getDeliveryDateTimes() != null) {
            mChangeDeliveryButton.setVisibility(View.VISIBLE);
        }

        String day = CommonUtil.formatDay(mOrderBean.getCollectionDate()) + ",";
        String date = CommonUtil.formatDateOfMonth(mOrderBean.getCollectionDate());
        String month = CommonUtil.formatMonth(mOrderBean.getCollectionDate());
        String time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getCollectionDate());

        mCollectionDayLabel.setText(day);
        mCollectionDateLabel.setText(date);
        mCollectionMonthLabel.setText(month);
        mCollectionTimeLabel.setText(time);

        day = CommonUtil.formatDay(mOrderBean.getDeliveryDate()) + ",";
        date = CommonUtil.formatDateOfMonth(mOrderBean.getDeliveryDate());
        month = CommonUtil.formatMonth(mOrderBean.getDeliveryDate());
        time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getDeliveryDate());

        mDeliveryDayLabel.setText(day);
        mDeliveryDateLabel.setText(date);
        mDeliveryMonthLabel.setText(month);
        mDeliveryTimeLabel.setText(time);

        mCollectionDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mCollectionDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mCollectionMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mCollectionTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));

        mDeliveryDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mDeliveryDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mDeliveryMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
        mDeliveryTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus())||
            Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {

            mCollectionDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mCollectionDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mCollectionMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mCollectionTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomDarkGreen));
        }

        if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus()) ||
            Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {

            mDeliveryDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mDeliveryDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mDeliveryMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mDeliveryTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomDarkGreen));
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
        mStatusLabel.setText(CodeUtil.getOrderStatusLabel(mOrderBean.getStatus()));
        mTotalChargeLabel.setText(CommonUtil.formatCurrency(mOrderBean.getTotalCharge()));

        mPaymentTypeButton.setVisibility(View.VISIBLE);

        if (!Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus())) {
            mPaymentTypeButton.setVisibility(View.GONE);
        }

        orderDetailAdapter.refreshDataSet();
    }

    private void refreshPicInfo() {

        EmployeeBean picBean = null;

        mCallButton.setVisibility(View.GONE);
        mSmsButton.setVisibility(View.GONE);

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

                mCallButton.setVisibility(View.VISIBLE);
                mSmsButton.setVisibility(View.VISIBLE);

            } else if (Session.getPic() != null) {

                picBean = Session.getPic();
                mPicNameLabel.setText(picBean.getName());

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(mPicImage, picBean.getImage());

                mCallButton.setVisibility(View.VISIBLE);
                mSmsButton.setVisibility(View.VISIBLE);

            } else {
                mPicNameLabel.setText(getString(R.string.assign_transporter_collection));
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

                mCallButton.setVisibility(View.VISIBLE);
                mSmsButton.setVisibility(View.VISIBLE);

            } else if (Session.getPic() != null) {

                picBean = Session.getPic();
                mPicNameLabel.setText(picBean.getName());

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(mPicImage, picBean.getImage());

                mCallButton.setVisibility(View.VISIBLE);
                mSmsButton.setVisibility(View.VISIBLE);

            } else {
                mPicNameLabel.setText(getString(R.string.assign_transporter_delivery));
            }

        } else {

            mPicPanel.setVisibility(View.GONE);
        }

        if (Session.isTransporterUser()) {

            mPicPanel.setVisibility(View.GONE);
        }

        if (picBean != null) {

            mCallButton.setOnClickListener(getCallButtonOnClickListener(picBean));
            mSmsButton.setOnClickListener(getSmsButtonOnClickListener(picBean));
        }
    }

    private void refreshPaymentInfo() {

        mPaymentTypeButton.setText(CodeUtil.getPaymentTypeLabel(mOrderBean.getPaymentType()));
    }

    private View.OnClickListener getExpandOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBean.getOrderProductItems().size() > 0) {

                    if (mOrderDetailPanel.isShown()) {
                        mOrderDetailPanel.setVisibility(View.GONE);
                        mOrderDetailExpandButton.setImageResource(R.drawable.icon_arrow_right);
                    } else {
                        mOrderDetailPanel.setVisibility(View.VISIBLE);
                        mOrderDetailExpandButton.setImageResource(R.drawable.icon_dropdown);
                    }
                }
            }
        };
    }

    private View.OnClickListener getAssignPicOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus())) {

                    showProgressDlg(getString(R.string.message_async_loading));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.getCollectionPics(mOrderBean.getServiceArea(), mOrderBean.getCollectionDate());

                } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {

                    showProgressDlg(getString(R.string.message_async_loading));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.getDeliveryPics(mOrderBean.getServiceArea(), mOrderBean.getDeliveryDate());
                }
            }
        };
    }

    private View.OnClickListener getUpdateOrderOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, UpdateOrderActivity.class);
                intent.putExtra(Constant.DATA_REPEAT_ORDER, mIsRepeatOrder);

                startActivityForResult(intent, UPDATE_ORDER);
            }
        };
    }

    @Override
    public void onAsyncGetPics(List<EmployeeBean> employeeBeans) {

        hideProgressDlgNow();

        Session.setPics(employeeBeans);

        Intent intent = new Intent(mAppCompatActivity, SelectPicActivity.class);

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus())) {
            startActivityForResult(intent, GET_COLLECTION_PIC);

        } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {
            startActivityForResult(intent, GET_DELIVERY_PIC);
        }
    }

    @Override
    public void onAsyncUpdateOrderStatus(OrderBean orderBean) {

        hideProgressDlgNow();

        // return to manage order

        if (Session.isAdminUser() ||
                (Session.isTransporterUser() && Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus()))) {

            Intent returnIntent = new Intent();

            if (Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {

                if (mIsRepeatOrder) {
                    returnIntent.putExtra(Constant.INTENT_DATA_STATUS, Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY);
                } else {
                    returnIntent.putExtra(Constant.INTENT_DATA_STATUS, Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION);
                }

            } /* else if (Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {
                returnIntent.putExtra(Constant.INTENT_DATA_STATUS, Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY);
            } */

            setResult(Activity.RESULT_OK, returnIntent);

            Session.setPic(null);

            finish();

        } else {

            refreshView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == GET_COLLECTION_PIC) {

                mOrderBean.setCollectionPic(Session.getPic());
                refreshPicInfo();

            } else if (requestCode == GET_DELIVERY_PIC) {

                mOrderBean.setDeliveryPic(Session.getPic());
                refreshPicInfo();

            } else if (requestCode == UPDATE_ORDER) {

                mIsUpdateProduct = true;
                refreshOrderDetail();
            }

        } else if (resultCode == RESULT_CANCELED) {

            if (requestCode == UPDATE_ORDER) {

                mIsUpdateProduct = true;
                refreshOrderDetail();
            }
        }
    }

    private View.OnClickListener getChangeDeliveryButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mChangeDeliveryDateDialog.isAdded()) {

                    mChangeDeliveryDateDialog.setCancelable(false);
                    mChangeDeliveryDateDialog.show(mAppCompatActivity.getSupportFragmentManager(), Constant.CHANGE_DELIVERY_DATE_DLG_TAG);
                }
            }
        };
    }

    private View.OnClickListener getPaymentTypeButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                    return;
                }

                mNewPaymentType = Constant.PAYMENT_TYPE_CASH;

                if (Constant.PAYMENT_TYPE_CASH.equals(mOrderBean.getPaymentType())) {
                    mNewPaymentType = Constant.PAYMENT_TYPE_WALLET;
                }

                showConfirmDlg(CONFIRM_CHANGE_PAYMENT_TYPE, getString(R.string.confirm_change_payment_type, CodeUtil.getPaymentTypeLabel(mNewPaymentType)));
            }
        };
    }

    private View.OnClickListener getSubmitButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus())) {

                    if (mOrderBean.getCollectionPic() == null) {
                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_transporter_need_to_be_assigned));
                    } else {
                        showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_assign_for_collection));
                    }

                } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_collection_in_progress));

                } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus())) {

                    if (mOrderBean.isEmptyOrder()) {

                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_empty_service));
                        return;
                    }

                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_collected));

                } else if (Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_cleaning_in_progress));

                } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_cleaned));

                } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {

                    if (mOrderBean.getDeliveryPic() == null) {
                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_transporter_need_to_be_assigned));
                    } else {
                        showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_assigned_for_delivery));
                    }

                } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_delivery_in_progress));

                } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_complete));

                } else if (Constant.ORDER_STATUS_COMPLETED.equals(mOrderBean.getStatus())) {
                    showConfirmDlg(CONFIRM_REPEAT_ORDER, getString(R.string.confirm_repeat_order));
                }
            }
        };
    }

    public void onConfirm(int id) {

        mOrderStatus = mOrderBean.getStatus();
        mPaymentType = mOrderBean.getPaymentType();

        if (id == CONFIRM_SUBMIT) {

            if (Constant.ORDER_STATUS_NEW_ORDER.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION);

            } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS);

            } else if (Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_COLLECTED);

            } else if (Constant.ORDER_STATUS_COLLECTED.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_CLEANING_IN_PROGRESS);

            } else if (Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_CLEANED);

            } else if (Constant.ORDER_STATUS_CLEANED.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY);

            } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS);

            } else if (Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS.equals(mOrderBean.getStatus())) {
                mOrderBean.setStatus(Constant.ORDER_STATUS_COMPLETED);
                mOrderBean.setCompletedDate(new Date());
            }

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);

            if (mIsUpdateProduct) {
                httpAsyncManager.updateOrderServiceAndStatus(mOrderBean);
            } else {
                httpAsyncManager.updateOrderStatus(mOrderBean);
            }

        } else if (id == CONFIRM_CHANGE_PAYMENT_TYPE) {

            mOrderBean.setPaymentType(mNewPaymentType);

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.updateOrderPaymentType(mOrderBean);

        } else if (id == CONFIRM_REPEAT_ORDER) {

            mIsRepeatOrder = true;

            showProgressDlg(getString(R.string.message_async_loading));

            Date date = new Date(mOrderBean.getDeliveryDate().getTime() + Constant.TIME_DAY);
            date = CommonUtil.getDateWithoutTime(date);

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.repeatOrder(date, mOrderBean.getServiceArea());
        }
    }

    private View.OnClickListener getPlaceInfoOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ShowLocationActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onAsyncError(String message) {

        mOrderBean.setPaymentType(mPaymentType);
        mOrderBean.setStatus(mOrderStatus);

        hideProgressDlgNow();
        NotificationUtil.showErrorMessage(this, message);
    }

    @Override
    public void onAsyncUpdateOrderPaymentType(OrderBean orderBean) {

        hideProgressDlgNow();
        refreshPaymentInfo();
    }

    @Override
    public void onAsyncRepeatOrder() {

        OrderBean orderBean = new OrderBean();

        orderBean.setOrderNo("NEW ORDER");

        orderBean.setCustomer(mOrderBean.getCustomer());

        orderBean.setCollectionDate(mOrderBean.getDeliveryDate());
        orderBean.setCollectionPic(mOrderBean.getDeliveryPic());

        int processingDays = CommonUtil.getProcessingDays(orderBean.getSpeedType());

        Date collectionDate = orderBean.getCollectionDate();
        Date idealDeliveryDate = new Date(collectionDate.getTime() + processingDays * Constant.TIME_DAY);
        Date minProcessingTime = new Date(collectionDate.getTime() + Constant.MIN_PROCESSING_TIME_REQUIRED);

        Date deliveryDate = CommonUtil.getDeliveryDate(minProcessingTime, idealDeliveryDate);

        orderBean.setDeliveryDate(deliveryDate);

        orderBean.setPlaceType(mOrderBean.getPlaceType());
        orderBean.setPlaceName(mOrderBean.getPlaceName());
        orderBean.setPlaceAddress(mOrderBean.getPlaceAddress());

        orderBean.setServiceArea(mOrderBean.getServiceArea());

        orderBean.setPaymentType(Constant.PAYMENT_TYPE_CASH);
        orderBean.setStatus(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS);

        orderBean.setLat(mOrderBean.getLat());
        orderBean.setLng(mOrderBean.getLng());

        orderBean.setSpeedType(mOrderBean.getSpeedType());
        orderBean.setTotalCharge(0f);

        orderBean.setOrderDate(new Date());

        mOrderBean = orderBean;
        Session.setOrder(mOrderBean);

        hideProgressDlgNow();
        refreshView();
    }

    @Override
    public void onOrderSelected(OrderBean orderBean) {}

    @Override
    public void onDeliveryDateUpdated() {

        refreshView();
    }

    private View.OnClickListener getCallButtonOnClickListener(final EmployeeBean employeeBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (employeeBean != null) {
                    showContactDlg(employeeBean.getName(), employeeBean.getImage(), employeeBean.getMobile(), Constant.CONTACT_CALL);
                }
            }
        };
    }

    private View.OnClickListener getSmsButtonOnClickListener(final EmployeeBean employeeBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (employeeBean != null) {
                    showContactDlg(employeeBean.getName(), employeeBean.getImage(), employeeBean.getMobile(), Constant.CONTACT_SMS);
                }
            }
        };
    }

    private View.OnClickListener getCallButtonOnClickListener(final CustomerBean customerBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerBean != null) {
                    showContactDlg(customerBean.getName(), customerBean.getImage(), customerBean.getMobile(), Constant.CONTACT_CALL);
                }
            }
        };
    }

    private View.OnClickListener getSmsButtonOnClickListener(final CustomerBean customerBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerBean != null) {
                    showContactDlg(customerBean.getName(), customerBean.getImage(), customerBean.getMobile(), Constant.CONTACT_SMS);
                }
            }
        };
    }
}