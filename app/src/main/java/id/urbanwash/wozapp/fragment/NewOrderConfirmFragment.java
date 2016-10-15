package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.TopUpActivity;
import id.urbanwash.wozapp.listener.NewOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.util.Utility;
import id.urbanwash.wozapp.dialog.ChangeDeliveryDateDialog;

/**
 * Created by apridosandyasa on 3/8/16.
 */
@SuppressLint("ValidFragment")
public class NewOrderConfirmFragment extends Fragment {

    private View rootView;

    AppCompatTextView mCollectionDateLabel;
    AppCompatTextView mCollectionTimeLabel;

    AppCompatTextView mDeliveryDateLabel;
    AppCompatTextView mDeliveryTimeLabel;

    AppCompatImageView mPlaceImage;
    AppCompatImageView mSpeedTypeImage;

    AppCompatTextView mPlaceNameLabel;
    AppCompatTextView mPlaceAddressLabel;
    AppCompatTextView mNoteLabel;

    AppCompatTextView mSpeedTypeLabel;
    AppCompatTextView mOrderSummaryLabel;

    AppCompatTextView mTotalCreditsLabel;
    AppCompatTextView mTopUpButton;

    AppCompatTextView mTotalChargeLabel;

    private AppCompatImageView mPaymentModeButton;
    private AppCompatImageView mChangeDeliveryButton;
    private ChangeDeliveryDateDialog mChangeDeliveryDateDialog;

    private AppCompatActivity mAppCompatActivity;
    private NewOrderListener mNewOrderListener;

    private OrderBean mOrderBean;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public NewOrderConfirmFragment() {}

    public NewOrderConfirmFragment(AppCompatActivity aca) {
        mAppCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mChangeDeliveryDateDialog = new ChangeDeliveryDateDialog(mAppCompatActivity);
        mNewOrderListener = (NewOrderListener) mAppCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_new_order_summary, container, false);

        mCollectionDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_date);
        mCollectionTimeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_collection_time);

        mDeliveryDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_date);
        mDeliveryTimeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_time);

        mPlaceImage = (AppCompatImageView) rootView.findViewById(R.id.image_place);
        mSpeedTypeImage = (AppCompatImageView) rootView.findViewById(R.id.image_speed_type);

        mPlaceNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_name);
        mPlaceAddressLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_address);

        mNoteLabel = (AppCompatTextView) rootView.findViewById(R.id.label_note);

        mSpeedTypeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_speed_type);
        mOrderSummaryLabel = (AppCompatTextView) rootView.findViewById(R.id.label_order_summary);

        mTotalChargeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_total_charge);
        mPaymentModeButton = (AppCompatImageView) rootView.findViewById(R.id.button_payment_mode);
        mChangeDeliveryButton = (AppCompatImageView) rootView.findViewById(R.id.button_change_delivery);

        mTotalCreditsLabel = (AppCompatTextView) rootView.findViewById(R.id.label_total_credits);
        mTopUpButton = (AppCompatTextView) rootView.findViewById(R.id.button_top_up);

        mPaymentModeButton.setOnClickListener(getPaymentButtonOnClickListener());

        View.OnClickListener changeDeliveryListener = getChangeDeliveryButtonOnClickListener();
        mDeliveryDateLabel.setOnClickListener(changeDeliveryListener);
        mDeliveryTimeLabel.setOnClickListener(changeDeliveryListener);
        mChangeDeliveryButton.setOnClickListener(changeDeliveryListener);

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        initView();

        return rootView;
    }

    private void initView() {

        mOrderBean = Session.getOrder();

        refreshDateTime();

        if (Constant.PLACE_TYPE_HOME.equals(mOrderBean.getPlaceType())) {
            mPlaceImage.setImageResource(R.drawable.icon_home);

        } else if (Constant.PLACE_TYPE_APARTMENT.equals(mOrderBean.getPlaceType())) {
            mPlaceImage.setImageResource(R.drawable.icon_apartment);

        } else if (Constant.PLACE_TYPE_OFFICE.equals(mOrderBean.getPlaceType())) {
            mPlaceImage.setImageResource(R.drawable.icon_office);
        }

        if (Constant.SPEED_TYPE_REGULAR.equals(mOrderBean.getSpeedType())) {
            mSpeedTypeImage.setImageResource(R.drawable.icon_regular);

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(mOrderBean.getSpeedType())) {
            mSpeedTypeImage.setImageResource(R.drawable.icon_express);

        } else if (Constant.SPEED_TYPE_DELUXE.equals(mOrderBean.getSpeedType())) {
            mSpeedTypeImage.setImageResource(R.drawable.icon_deluxe);
        }

        if (!CommonUtil.isEmpty(mOrderBean.getPlaceName())) {
            mPlaceNameLabel.setText(mOrderBean.getPlaceName());
            mPlaceNameLabel.setVisibility(View.VISIBLE);
        } else {
            mPlaceNameLabel.setVisibility(View.GONE);
        }

        mPlaceAddressLabel.setText(mOrderBean.getPlaceAddress());

        if (!CommonUtil.isEmpty(mOrderBean.getNote())) {
            mNoteLabel.setText("* " + mOrderBean.getNote());
            mNoteLabel.setVisibility(View.VISIBLE);
        } else {
            mNoteLabel.setVisibility(View.GONE);
        }

        mSpeedTypeLabel.setText(CodeUtil.getSpeedTypeLabel(mOrderBean.getSpeedType()));
        mOrderSummaryLabel.setText(CommonUtil.getOrderProductInfo(mOrderBean));

        mTotalCreditsLabel.setText(CommonUtil.formatCurrency(Session.getCustomer().getTotalCredits()));
        mTopUpButton.setOnClickListener(getTopUpButtonOnClickListener());

        mTotalChargeLabel.setText(CommonUtil.formatCurrency(mOrderBean.getTotalCharge()));
    }

    public void refreshDateTime() {

        String collectionDate = CommonUtil.formatDayShortDate(mOrderBean.getCollectionDate());
        String collectionTime = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getCollectionDate());

        mCollectionDateLabel.setText(collectionDate);
        mCollectionTimeLabel.setText(collectionTime);

        String deliveryDate = CommonUtil.formatDayShortDate(mOrderBean.getDeliveryDate());
        String deliveryTime = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getDeliveryDate());

        mDeliveryDateLabel.setText(deliveryDate);
        mDeliveryTimeLabel.setText(deliveryTime);
    }

    private void showChangeDeliveryDateDialog() {

        if (!mChangeDeliveryDateDialog.isAdded()) {

            mChangeDeliveryDateDialog.setCancelable(false);
            mChangeDeliveryDateDialog.show(mAppCompatActivity.getSupportFragmentManager(), Constant.CHANGE_DELIVERY_DATE_DLG_TAG);
        }
    }

    private View.OnClickListener getPaymentButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String paymentType = mOrderBean.getPaymentType();
                float totalCredits = Session.getCustomer().getTotalCredits();

                if (Constant.PAYMENT_TYPE_CASH.equals(paymentType)) {

                    if (totalCredits >= mOrderBean.getTotalCharge()) {
                        mOrderBean.setPaymentType(Constant.PAYMENT_TYPE_WALLET);
                        mPaymentModeButton.setImageResource(R.drawable.icon_payment_wallet_order_summary);
                    } else {
                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_insufficient_credit_balance));
                    }
                } else {

                    mOrderBean.setPaymentType(Constant.PAYMENT_TYPE_CASH);
                    mPaymentModeButton.setImageResource(R.drawable.icon_payment_cash_order_summary);
                }
            }
        };
    }

    private View.OnClickListener getChangeDeliveryButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showChangeDeliveryDateDialog();
            }
        };
    }

    private View.OnClickListener getTopUpButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, TopUpActivity.class);
                startActivity(intent);
            }
        };
    }
}
