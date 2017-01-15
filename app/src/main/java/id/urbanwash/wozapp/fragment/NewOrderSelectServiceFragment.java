package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.NewOrderActivity;
import id.urbanwash.wozapp.activity.NewOrderPromoActivity;
import id.urbanwash.wozapp.activity.NewOrderServiceKgActivity;
import id.urbanwash.wozapp.activity.NewOrderServicePieceActivity;
import id.urbanwash.wozapp.dialog.ChangeDeliveryDateDialog;
import id.urbanwash.wozapp.listener.SelectServiceListener;
import id.urbanwash.wozapp.model.LaundryItemBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;
import id.urbanwash.wozapp.model.OrderProductItemBean;
import id.urbanwash.wozapp.model.ProductBean;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/7/16.
 */
@SuppressLint("ValidFragment")
public class NewOrderSelectServiceFragment extends Fragment {

    private View rootView;

    private AppCompatImageView mServiceRegularButton;
    private AppCompatImageView mServiceExpressButton;
    private AppCompatImageView mServiceDeluxeButton;

    private AppCompatTextView mServiceRegularLabel;
    private AppCompatTextView mServiceExpressLabel;
    private AppCompatTextView mServiceDeluxeLabel;

    private AppCompatTextView mServiceLabel;
    private AppCompatTextView mDeliveryDateLabel;

    private AppCompatImageView mServiceKgButton;
    private AppCompatImageView mServicePieceButton;
    private AppCompatImageView mServiceDryCleanButton;

    private AppCompatTextView mServiceKgLabel;
    private AppCompatTextView mServicePieceLabel;
    private AppCompatTextView mServiceDryCleanLabel;

    private AppCompatTextView mSummaryKgLabel;
    private AppCompatTextView mSummaryPieceLabel;
    private AppCompatTextView mSummaryDryCleanLabel;

    private AppCompatTextView mChargeKgLabel;
    private AppCompatTextView mChargePieceLabel;
    private AppCompatTextView mChargeDryCleanLabel;

    private LinearLayout mPromoPanel;
    private AppCompatTextView mPromoCodeLabel;
    private AppCompatTextView mPromoCodeInfoLabel;
    private AppCompatTextView mPromoValueLabel;

    private AppCompatTextView mTotalChargeLabel;

    private ChangeDeliveryDateDialog mChangeDeliveryDateDialog;

    private AppCompatActivity mAppCompatActivity;

    private SelectServiceListener mSelectServiceListener;

    private OrderBean mOrderBean;

    private String mSpeedType;

    private static final int ACTIVITY_GET_PROMO = 1;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public NewOrderSelectServiceFragment() {
    }

    public NewOrderSelectServiceFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;

        if (mAppCompatActivity instanceof SelectServiceListener) {

            mSelectServiceListener = (SelectServiceListener) mAppCompatActivity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrderBean = Session.getOrder();

        mChangeDeliveryDateDialog = new ChangeDeliveryDateDialog(mAppCompatActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_new_order_select_service, container, false);

        mServiceRegularButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_regular);
        mServiceExpressButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_express);
        mServiceDeluxeButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_deluxe);

        mServiceRegularLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_regular);
        mServiceExpressLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_express);
        mServiceDeluxeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_deluxe);

        mServiceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service);
        mDeliveryDateLabel = (AppCompatTextView) rootView.findViewById(R.id.label_delivery_date);

        mServiceKgButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_kg);
        mServicePieceButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_piece);
        mServiceDryCleanButton = (AppCompatImageView) rootView.findViewById(R.id.button_service_dry_clean);

        mServiceKgLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_kg);
        mServicePieceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_piece);
        mServiceDryCleanLabel = (AppCompatTextView) rootView.findViewById(R.id.label_service_dry_clean);

        mSummaryKgLabel = (AppCompatTextView) rootView.findViewById(R.id.label_summary_kg);
        mSummaryPieceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_summary_piece);
        mSummaryDryCleanLabel = (AppCompatTextView) rootView.findViewById(R.id.label_summary_dry_clean);

        mChargeKgLabel = (AppCompatTextView) rootView.findViewById(R.id.label_charge_kg);
        mChargePieceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_charge_piece);
        mChargeDryCleanLabel = (AppCompatTextView) rootView.findViewById(R.id.label_charge_dry_clean);

        mPromoPanel = (LinearLayout) rootView.findViewById(R.id.panel_promo);
        mPromoCodeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_promo_code);
        mPromoCodeInfoLabel = (AppCompatTextView) rootView.findViewById(R.id.label_promo_code_info);
        mPromoValueLabel = (AppCompatTextView) rootView.findViewById(R.id.label_promo_value);

        mTotalChargeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_total_charge);

        mServiceRegularButton.setOnClickListener(getServiceRegularButtonOnClickListener());
        mServiceExpressButton.setOnClickListener(getServiceExpressButtonOnClickListener());
        mServiceDeluxeButton.setOnClickListener(getServiceDeluxeButtonOnClickListener());

        mDeliveryDateLabel.setOnClickListener(getDeliveryDateOnClickListener());

        mServiceKgButton.setOnClickListener(getServiceKgButtonOnClickListener());
        mServicePieceButton.setOnClickListener(getServicePieceButtonOnClickListener());
        mServiceDryCleanButton.setOnClickListener(getServiceDryCleanButtonOnClickListener());

        mPromoPanel.setOnClickListener(getPromoOnClickListener());

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshCharge();
        refreshSpeedType();
        refreshServices();
        refreshPromo();
        refreshTotalCharge();
        refreshDeliveryDate();

        if (mAppCompatActivity instanceof NewOrderActivity) {

            //initDeliveryDate();
        }
    }

    private void showChangeDeliveryDateDialog() {

        if (!mChangeDeliveryDateDialog.isAdded()) {

            mChangeDeliveryDateDialog.setCancelable(false);
            mChangeDeliveryDateDialog.show(mAppCompatActivity.getSupportFragmentManager(), Constant.CHANGE_DELIVERY_DATE_DLG_TAG);
        }
    }

    private void refreshSpeedType() {

        mServiceRegularButton.setImageResource(R.drawable.icon_regular_grey);
        mServiceExpressButton.setImageResource(R.drawable.icon_express_grey);
        mServiceDeluxeButton.setImageResource(R.drawable.icon_deluxe_grey);

        mServiceRegularLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mServiceExpressLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mServiceDeluxeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));

        if (Constant.SPEED_TYPE_REGULAR.equals(mOrderBean.getSpeedType())) {

            mServiceRegularButton.setImageResource(R.drawable.icon_regular);
            mServiceRegularLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(mOrderBean.getSpeedType())) {

            mServiceExpressButton.setImageResource(R.drawable.icon_express);
            mServiceExpressLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));

        } else if (Constant.SPEED_TYPE_DELUXE.equals(mOrderBean.getSpeedType())) {

            mServiceDeluxeButton.setImageResource(R.drawable.icon_deluxe);
            mServiceDeluxeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
        }

        if (mOrderBean.getSpeedType() != null) {
            mServiceLabel.setVisibility(View.VISIBLE);
        } else {
            mServiceLabel.setVisibility(View.GONE);
        }
    }

    private void initDeliveryDate() {

        if (mOrderBean.getSpeedType() != null) {

            int processingDays = CommonUtil.getProcessingDays(mOrderBean.getSpeedType());

            Date collectionDate = mOrderBean.getCollectionDate();

            Date idealDeliveryDate = new Date(collectionDate.getTime() + processingDays * Constant.TIME_DAY);
            Date minProcessingTime = new Date(collectionDate.getTime() + Constant.MIN_PROCESSING_TIME_REQUIRED);

            Date deliveryDate = CommonUtil.getDeliveryDate(minProcessingTime, idealDeliveryDate);

            mOrderBean.setDeliveryDate(deliveryDate);
        }
    }
    
    public void refreshDeliveryDate() {

        mOrderBean = Session.getOrder();

        if (mOrderBean.getDeliveryDate() != null) {

            String time = CommonUtil.formatOperatingHrTimePeriod(mOrderBean.getDeliveryDate());

            mDeliveryDateLabel.setText(CommonUtil.formatDayMonth(mOrderBean.getDeliveryDate()) + "," + Constant.SPACE_STRING + time);
        }
    }

    private void refreshCharge() {

        float orderProductTotalCharge = 0;
        List<OrderProductBean> orderProductBeans = mOrderBean.getOrderProducts();

        if (orderProductBeans != null) {

            for (OrderProductBean orderProductBean : orderProductBeans) {

                float price = 0;
                ProductBean productBean = Session.getProduct(orderProductBean.getProduct().getCode());

                if (Constant.SPEED_TYPE_REGULAR.equals(mOrderBean.getSpeedType())) {
                    price = productBean.getPrice1();

                } else if (Constant.SPEED_TYPE_EXPRESS.equals(mOrderBean.getSpeedType())) {
                    price = productBean.getPrice2();

                } else if (Constant.SPEED_TYPE_DELUXE.equals(mOrderBean.getSpeedType())) {
                    price = productBean.getPrice3();

                }

                if (Constant.PRODUCT_KG_WASH_IRON.equals(orderProductBean.getProduct().getCode())) {

                    price = orderProductBean.getCount() * price;
                    orderProductBean.setCharge(price);

                } else {

                    List<OrderProductItemBean> orderProductItemBeans = orderProductBean.getOrderProductItems();

                    float orderProductItemTotalCharge = 0;

                    if (orderProductItemBeans != null) {

                        for (OrderProductItemBean orderProductItemBean : orderProductItemBeans) {

                            price = 0;
                            LaundryItemBean laundryItemBean = productBean.getLaundryItem(orderProductItemBean.getLaundryItem().getCode());

                            if (Constant.SPEED_TYPE_REGULAR.equals(mOrderBean.getSpeedType())) {
                                price = laundryItemBean.getPrice1();

                            } else if (Constant.SPEED_TYPE_EXPRESS.equals(mOrderBean.getSpeedType())) {
                                price = laundryItemBean.getPrice2();

                            } else if (Constant.SPEED_TYPE_DELUXE.equals(mOrderBean.getSpeedType())) {
                                price = laundryItemBean.getPrice3();
                            }

                            float orderProductItemCharge =  price * orderProductItemBean.getCount();
                            orderProductItemBean.setCharge(orderProductItemCharge);

                            orderProductItemTotalCharge += orderProductItemCharge;
                        }
                    }

                    orderProductBean.setCharge(orderProductItemTotalCharge);
                }

                orderProductTotalCharge += orderProductBean.getCharge();
            }

            PromoBean promoBean = mOrderBean.getPromo();

            if (promoBean != null) {

                float discount = 0f;

                if (orderProductTotalCharge >= promoBean.getMinOrder()) {

                    if (Constant.PROMO_TYPE_AMOUNT.equals(promoBean.getType())) {
                        discount = promoBean.getValue();

                    } else if (Constant.PROMO_TYPE_PERCENTAGE.equals(promoBean.getType())) {
                        discount = promoBean.getValue() / 100 * orderProductTotalCharge;
                    }

                    orderProductTotalCharge -= discount;
                }

                mOrderBean.setDiscount(discount);
            }

            mOrderBean.setTotalCharge(orderProductTotalCharge);
        }
    }

    private boolean isSpeedTypeNotApplicable(String speedType) {

        List<OrderProductBean> orderProductBeans = mOrderBean.getOrderProducts();

        boolean isSpeedTypeNotApplicable = false;

        if (orderProductBeans != null) {

            for (OrderProductBean orderProductBean : orderProductBeans) {

                float price = 0;
                ProductBean productBean = Session.getProduct(orderProductBean.getProduct().getCode());

                if (!Constant.PRODUCT_KG_WASH_IRON.equals(productBean.getCode())) {

                    List<OrderProductItemBean> orderProductItemBeans = orderProductBean.getOrderProductItems();

                    if (orderProductItemBeans != null) {

                        for (OrderProductItemBean orderProductItemBean : orderProductItemBeans) {

                            price = 0;
                            LaundryItemBean laundryItemBean = productBean.getLaundryItem(orderProductItemBean.getLaundryItem().getCode());

                            if (Constant.SPEED_TYPE_REGULAR.equals(speedType)) {
                                price = laundryItemBean.getPrice1();

                            } else if (Constant.SPEED_TYPE_EXPRESS.equals(speedType)) {
                                price = laundryItemBean.getPrice2();

                            } else if (Constant.SPEED_TYPE_DELUXE.equals(speedType)) {
                                price = laundryItemBean.getPrice3();
                            }

                            if (price == 0) {

                                NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_minimum_processing_time_for_product, laundryItemBean.getName()));
                                isSpeedTypeNotApplicable = true;
                            }
                        }
                    }
                }
            }
        }

        return isSpeedTypeNotApplicable;
    }

    private void refreshServices() {

        mServiceKgButton.setImageResource(R.drawable.icon_kg_grey);
        mServicePieceButton.setImageResource(R.drawable.icon_piece_grey);
        mServiceDryCleanButton.setImageResource(R.drawable.icon_dryclean_grey);

        mServiceKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mServicePieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mServiceDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));

        mSummaryKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mSummaryPieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mSummaryDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));

        mChargeKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mChargePieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mChargeDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));

        OrderProductBean orderProductKg = mOrderBean.getOrderProduct(Constant.PRODUCT_KG_WASH_IRON);
        OrderProductBean orderProductPiece = mOrderBean.getOrderProduct(Constant.PRODUCT_ITEM_WASH_IRON);
        OrderProductBean orderProductDryClean = mOrderBean.getOrderProduct(Constant.PRODUCT_DRY_CLEAN);

        mSummaryKgLabel.setText("0 Kg");
        mSummaryPieceLabel.setText("0 Pc");
        mSummaryDryCleanLabel.setText("0 Pc");

        mChargeKgLabel.setText(CommonUtil.formatCurrency(0f));
        mChargePieceLabel.setText(CommonUtil.formatCurrency(0f));
        mChargeDryCleanLabel.setText(CommonUtil.formatCurrency(0f));

        if (orderProductKg != null && orderProductKg.getCount() > 0) {

            mServiceKgButton.setImageResource(R.drawable.icon_kg);
            mServiceKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mSummaryKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
            mChargeKgLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));

            mSummaryKgLabel.setText(CommonUtil.formatNumber(orderProductKg.getCount()) + Constant.SPACE_STRING + getString(R.string.unit_kg));
            mChargeKgLabel.setText(CommonUtil.formatCurrency(orderProductKg.getCharge()));
        }

        if (orderProductPiece != null && orderProductPiece.getCharge() > 0) {

            mServicePieceButton.setImageResource(R.drawable.icon_piece);
            mServicePieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mSummaryPieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
            mChargePieceLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));

            mSummaryPieceLabel.setText(CommonUtil.formatNumber(orderProductPiece.getCount()) + Constant.SPACE_STRING + getString(R.string.unit_pc));
            mChargePieceLabel.setText(CommonUtil.formatCurrency(orderProductPiece.getCharge()));
        }

        if (orderProductDryClean != null && orderProductDryClean.getCharge() > 0) {

            mServiceDryCleanButton.setImageResource(R.drawable.icon_dryclean);
            mServiceDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
            mSummaryDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
            mChargeDryCleanLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));

            mSummaryDryCleanLabel.setText(CommonUtil.formatNumber(orderProductDryClean.getCount()) + Constant.SPACE_STRING + getString(R.string.unit_pc));
            mChargeDryCleanLabel.setText(CommonUtil.formatCurrency(orderProductDryClean.getCharge()));
        }
    }

    private void refreshPromo() {

        mPromoCodeInfoLabel.setVisibility(View.GONE);
        PromoBean promoBean = mOrderBean.getPromo();

        if (promoBean != null) {

            String promoCode = getString(R.string.order_promo) + Constant.SPACE_STRING + promoBean.getCode();
            mPromoCodeLabel.setText(promoCode);

            if (promoBean.getMinOrder() > 0) {
                String promoCodeInfo = getString(R.string.order_promo_min_order, CommonUtil.formatCurrency(promoBean.getMinOrder()));
                mPromoCodeInfoLabel.setText(promoCodeInfo);
                mPromoCodeInfoLabel.setVisibility(View.VISIBLE);
            }

            mPromoValueLabel.setText("-" + Constant.SPACE_STRING + Constant.SPACE_STRING + CommonUtil.formatCurrency(mOrderBean.getDiscount()));
            mPromoValueLabel.setVisibility(View.VISIBLE);

        } else {
            mPromoCodeLabel.setText(getString(R.string.promo_enter_code));
            mPromoValueLabel.setVisibility(View.GONE);
        }
    }

    private void refreshTotalCharge() {

        mTotalChargeLabel.setText(CommonUtil.formatCurrency(mOrderBean.getTotalCharge()));
    }

    private View.OnClickListener getDeliveryDateOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBean.getDeliveryDate() != null) {
                    showChangeDeliveryDateDialog();
                }
            }
        };
    }

    private View.OnClickListener getServiceRegularButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSpeedType = Constant.SPEED_TYPE_REGULAR;

                if (Session.getDeliveryDateTimes() == null) {
                    mSelectServiceListener.onGetDeliveryDates();
                    return;
                }

                selectRegularService();
            }
        };
    }

    private void selectRegularService() {

        mOrderBean.setSpeedType(Constant.SPEED_TYPE_REGULAR);
        refreshSpeedType();
        refreshCharge();
        refreshServices();
        initDeliveryDate();
        refreshPromo();
        refreshTotalCharge();

        showChangeDeliveryDateDialog();
    }

    private View.OnClickListener getServiceExpressButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSpeedTypeNotApplicable(Constant.SPEED_TYPE_EXPRESS)) {
                    return;
                }

                mSpeedType = Constant.SPEED_TYPE_EXPRESS;

                if (Session.getDeliveryDateTimes() == null) {
                    mSelectServiceListener.onGetDeliveryDates();
                    return;
                }

                selectExpressService();
            }
        };
    }

    private void selectExpressService() {

        int processingDays = CommonUtil.getProcessingDays(mOrderBean.getSpeedType());

        Date collectionDate = mOrderBean.getCollectionDate();
        Date idealDeliveryDate = new Date(collectionDate.getTime() + processingDays * Constant.TIME_DAY);
        Date minProcessingTime = new Date(collectionDate.getTime() + Constant.MIN_PROCESSING_TIME_REQUIRED);

        Date deliveryDate = CommonUtil.getDeliveryDate(minProcessingTime, idealDeliveryDate);

        collectionDate = CommonUtil.getDateWithoutTime(collectionDate);
        deliveryDate = CommonUtil.getDateWithoutTime(deliveryDate);

        if (deliveryDate.getTime() != collectionDate.getTime() + processingDays * Constant.TIME_DAY) {
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_no_available_delivery_time));
            return;
        }

        mOrderBean.setSpeedType(Constant.SPEED_TYPE_EXPRESS);
        refreshSpeedType();
        refreshCharge();
        refreshServices();
        initDeliveryDate();
        refreshPromo();
        refreshTotalCharge();

        showChangeDeliveryDateDialog();
    }

    private View.OnClickListener getServiceDeluxeButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSpeedTypeNotApplicable(Constant.SPEED_TYPE_DELUXE)) {
                    return;
                }

                mSpeedType = Constant.SPEED_TYPE_DELUXE;

                if (Session.getDeliveryDateTimes() == null) {
                    mSelectServiceListener.onGetDeliveryDates();
                    return;
                }

                selectDeluxeService();
            }
        };
    }

    private void selectDeluxeService() {

        int processingDays = CommonUtil.getProcessingDays(mOrderBean.getSpeedType());

        Date collectionDate = mOrderBean.getCollectionDate();
        Date idealDeliveryDate = new Date(collectionDate.getTime() + processingDays * Constant.TIME_DAY);
        Date minProcessingTime = new Date(collectionDate.getTime() + Constant.MIN_PROCESSING_TIME_REQUIRED);

        Date deliveryDate = CommonUtil.getDeliveryDate(minProcessingTime, idealDeliveryDate);

        collectionDate = CommonUtil.getDateWithoutTime(collectionDate);
        deliveryDate = CommonUtil.getDateWithoutTime(deliveryDate);

        if (deliveryDate.getTime() != collectionDate.getTime() + processingDays * Constant.TIME_DAY) {
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_no_available_delivery_time));
            return;
        }

        mOrderBean.setSpeedType(Constant.SPEED_TYPE_DELUXE);
        refreshSpeedType();
        refreshCharge();
        refreshServices();
        initDeliveryDate();
        refreshPromo();
        refreshTotalCharge();

        showChangeDeliveryDateDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ACTIVITY_GET_PROMO) {

                PromoBean promoBean = (PromoBean) intent.getSerializableExtra(Constant.DATA_PROMO);
                mOrderBean.setPromo(promoBean);

                refreshCharge();
                refreshPromo();
                refreshTotalCharge();
            }
        }
    }

    public void onReceiveDeliveryDateTimes() {

        if (Constant.SPEED_TYPE_REGULAR.equals(mSpeedType)) {
            selectRegularService();

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(mSpeedType)) {
            selectExpressService();

        } else if (Constant.SPEED_TYPE_DELUXE.equals(mSpeedType)) {
            selectDeluxeService();
        }
    }

    private View.OnClickListener getServiceKgButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBean.getSpeedType() == null || mOrderBean.getDeliveryDate() == null) {
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_select_delivery_date));
                    return;
                }

                Intent intent = new Intent(mAppCompatActivity, NewOrderServiceKgActivity.class);
                intent.putExtra(Constant.INTENT_DATA_PRODUCT, Session.getProduct(Constant.PRODUCT_KG_WASH_IRON));
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getServicePieceButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBean.getSpeedType() == null || mOrderBean.getDeliveryDate() == null) {
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_select_delivery_date));
                    return;
                }

                Intent intent = new Intent(mAppCompatActivity, NewOrderServicePieceActivity.class);
                intent.putExtra(Constant.INTENT_DATA_PRODUCT, Session.getProduct(Constant.PRODUCT_ITEM_WASH_IRON));
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getServiceDryCleanButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBean.getSpeedType() == null || mOrderBean.getDeliveryDate() == null) {
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_select_delivery_date));
                    return;
                }

                Intent intent = new Intent(mAppCompatActivity, NewOrderServicePieceActivity.class);
                intent.putExtra(Constant.INTENT_DATA_PRODUCT, Session.getProduct(Constant.PRODUCT_DRY_CLEAN));
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getPromoOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAppCompatActivity, NewOrderPromoActivity.class);
                startActivityForResult(intent, ACTIVITY_GET_PROMO);
            }
        };
    }
}