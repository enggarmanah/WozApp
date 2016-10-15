package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import java.util.Date;
import java.util.List;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.OrderTrackingListener;
import id.urbanwash.wozapp.fragment.OrderStatusFragment;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 4/12/16.
 */
public class TrackingOrderActivity extends BaseActivity implements OrderTrackingListener {

    private AppCompatActivity mAppCompatActivity;

    private OrderStatusFragment mOrderStatusFragment;

    private static final int CONFIRM_CANCEL = 1;
    private static final int CONFIRM_CHANGE_PAYMENT_TYPE = 2;

    private static final int ACK_CANCEL_SUCCESS = 1;
    private static final int ACK_CHANGE_DELIVERY_DATE = 2;
    private static final int ACK_CHANGE_DELIVERY_SUCCESS = 3;

    OrderBean mOrderBean;
    String mNewPaymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tracking_order);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        OrderBean orderBean = Session.getOrder();
        setNavigationBarTitle("ORDER ID # " + orderBean.getOrderNo());

        mOrderStatusFragment = new OrderStatusFragment(this);

        addFragment(mOrderStatusFragment, Constant.ORDER_STATUS_FRAGMENT_TAG);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        mOrderStatusFragment.refreshView();
    }

    private void setNavigationBarTitle(String text) {
        ((AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title))
                .setText(text);
    }

    @Override
    public void onCancelOrder(OrderBean orderBean) {

        showConfirmDlg(CONFIRM_CANCEL, getString(R.string.confirm_cancel_order));
    }

    @Override
    public void onChangeOrderDeliveryDate(OrderBean orderBean) {

        if (orderBean.getRescheduleCount() >= Constant.MAX_RESCHEDULE_COUNT) {
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_reschedule_no_more));

        } else if (orderBean.getRescheduleCount() == Constant.MAX_RESCHEDULE_COUNT - 1) {
            showAcknowlegementDlg(ACK_CHANGE_DELIVERY_DATE, getString(R.string.ack_reschedule_1_time));

        } else if (orderBean.getRescheduleCount() < Constant.MAX_RESCHEDULE_COUNT) {

            String allowedRescheduleCount = CommonUtil.formatNumber(Constant.MAX_RESCHEDULE_COUNT - orderBean.getRescheduleCount());
            showAcknowlegementDlg(ACK_CHANGE_DELIVERY_DATE, getString(R.string.ack_reschedule_n_times, allowedRescheduleCount));
        }
    }

    @Override
    public void onDeliveryDateUpdated() {

        showProgressDlg(getString(R.string.message_async_processing));

        mOrderBean = Session.getOrder();

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.updateOrderDeliveryDate(mOrderBean);
    }

    @Override
    public void onChangePaymentType(String paymentType) {

        mNewPaymentType = paymentType;

        showConfirmDlg(CONFIRM_CHANGE_PAYMENT_TYPE, getString(R.string.confirm_change_payment_type, CodeUtil.getPaymentTypeLabel(paymentType)));
    }

    private void getDeliveryTimes() {

        showProgressDlg(getString(R.string.message_async_loading));

        Date date = mOrderBean.getDeliveryDate() != null ? CommonUtil.getDateWithoutTime(mOrderBean.getDeliveryDate()) : CommonUtil.getCurrentDate();

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.initDeliveryTimes(date, mOrderBean.getServiceArea());
    }

    public void onConfirm(int id) {

        mOrderBean = Session.getOrder();

        if (id == CONFIRM_CANCEL) {

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.cancelOrder(mOrderBean);

        } else if (id == CONFIRM_CHANGE_PAYMENT_TYPE) {

            mOrderBean.setPaymentType(mNewPaymentType);

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.updateOrderPaymentType(mOrderBean);
        }
    }

    public void onAcknowledge(int id) {

        mOrderBean = Session.getOrder();

        if (id == ACK_CANCEL_SUCCESS) {

            finish();

        } else if (id == ACK_CHANGE_DELIVERY_DATE) {

            getDeliveryTimes();

        } else if (id == ACK_CHANGE_DELIVERY_SUCCESS) {

            mOrderStatusFragment.refreshDeliveryAndPaymentInfo();
        }
    }

    @Override
    public void onAsyncGetDeliveryTimes(Map<Date, List<Long>> deliveryDateTimes) {

        hideProgressDlgNow();
        mOrderStatusFragment.showChangeDeliveryDateDialog();
    }

    @Override
    public void onAsyncCancelOrder(List<OrderBean> orders) {

        hideProgressDlgNow();
        showAcknowlegementDlg(ACK_CANCEL_SUCCESS, getString(R.string.ack_cancel_order_success));
    }

    @Override
    public void onAsyncUpdateOrderPaymentType(OrderBean orderBean) {

        hideProgressDlgNow();
        mOrderStatusFragment.refreshDeliveryAndPaymentInfo();
    }

    @Override
    public void onAsyncUpdateOrderDeliveryDate(OrderBean orderBean) {

        hideProgressDlgNow();
        mOrderBean.setRescheduleCount(mOrderBean.getRescheduleCount() + 1);
        showAcknowlegementDlg(ACK_CHANGE_DELIVERY_SUCCESS, getString(R.string.ack_reschedule_success));
    }

    @Override
    public void onCall(String name, ImageBean image, String mobile) {

        showContactDlg(name, image, mobile, Constant.CONTACT_CALL);
    }

    @Override
    public void onSms(String name, ImageBean image, String mobile) {

        showContactDlg(name, image, mobile, Constant.CONTACT_SMS);
    }
}