package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.MenuItem;
import android.view.View;

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

/**
 * Created by apridosandyasa on 4/11/16.
 */
public class NewOrderCompleteActivity extends BaseActivity implements OrderTrackingListener {

    private OrderStatusFragment mOrderStatusFragment;
    private AppCompatImageView mCloseButton;

    private AppCompatActivity mAppCompatActivity;

    private static final int CONFIRM_CANCEL = 1;
    private static final int CONFIRM_CHANGE_PAYMENT_TYPE = 2;

    private static final int ACK_CANCEL_SUCCESS = 1;

    String mNewPaymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_order_complete);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        mOrderStatusFragment = new OrderStatusFragment(this);

        mCloseButton = (AppCompatImageView) findViewById(R.id.button_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, MainActivity.class);
                startService(intent);
                finish();
            }
        });

        addFragment(mOrderStatusFragment, Constant.ORDER_STATUS_FRAGMENT_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            showMainActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onCancelOrder(OrderBean orderBean) {

        showConfirmDlg(CONFIRM_CANCEL, getString(R.string.confirm_cancel_order));
    }

    @Override
    public void onChangeOrderDeliveryDate(OrderBean orderBean) {

        showProgressDlg(getString(R.string.message_async_loading));

        Date date = orderBean.getDeliveryDate() != null ? CommonUtil.getDateWithoutTime(orderBean.getDeliveryDate()) : CommonUtil.getCurrentDate();

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.initDeliveryTimes(date, orderBean.getServiceArea());
    }

    @Override
    public void onDeliveryDateUpdated() {

        showProgressDlg(getString(R.string.message_async_processing));

        OrderBean orderBean = Session.getOrder();

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.updateOrderDeliveryDate(orderBean);
    }

    @Override
    public void onChangePaymentType(String paymentType) {

        mNewPaymentType = paymentType;

        showConfirmDlg(CONFIRM_CHANGE_PAYMENT_TYPE, getString(R.string.confirm_change_payment_type, CodeUtil.getPaymentTypeLabel(paymentType)));
    }

    public void onConfirm(int id) {

        OrderBean orderBean = Session.getOrder();

        if (id == CONFIRM_CANCEL) {

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.cancelOrder(orderBean);

        } else if (id == CONFIRM_CHANGE_PAYMENT_TYPE) {

            orderBean.setPaymentType(mNewPaymentType);

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.updateOrderPaymentType(orderBean);
        }
    }

    public void onAcknowledge(int id) {

        if (id == ACK_CANCEL_SUCCESS) {

            finish();
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
        mOrderStatusFragment.refreshDeliveryAndPaymentInfo();
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
