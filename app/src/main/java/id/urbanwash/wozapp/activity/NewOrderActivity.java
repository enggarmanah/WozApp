package id.urbanwash.wozapp.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.NewOrderListener;
import id.urbanwash.wozapp.fragment.NewOrderConfirmFragment;
import id.urbanwash.wozapp.fragment.NewOrderSchedulePickupFragment;
import id.urbanwash.wozapp.fragment.NewOrderSelectServiceFragment;

import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.model.ServiceAreaBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/19/16.
 */
public class NewOrderActivity extends BaseActivity implements NewOrderListener {
    
    private int startOrderStep = 0;
    
    private ProgressBar mNewOrderProgressBar;
    
    private AppCompatImageView mNextButton;
    private AppCompatTextView mConfirmButton;
    private AppCompatTextView mTitleLabel;
    private RelativeLayout mBottomPanel;

    private FrameLayout mFragmentPanel;

    private NewOrderSchedulePickupFragment mNewOrderSchedulePickupFragment;
    private NewOrderSelectServiceFragment mNewOrderSelectServiceFragment;
    private NewOrderConfirmFragment mNewOrderConfirmFragment;

    private AppCompatActivity mAppCompatActivity;

    private static final int CONFIRM_CANCEL_ORDER = 1;
    private static final int CONFIRM_SUBMIT_ORDER = 2;

    ServiceAreaBean mServiceAreaBean;

    private boolean mIsInitialize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_order);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackArrowColor());

        mNewOrderProgressBar = (ProgressBar) findViewById(R.id.pb_new_order);
        mNextButton = (AppCompatImageView) findViewById(R.id.button_next);
        mConfirmButton = (AppCompatTextView) findViewById(R.id.button_confirm);

        mFragmentPanel = (FrameLayout) findViewById(R.id.panel_fragment);

        mNextButton.setOnClickListener(getNextStepButtonOnClickListener());
        mConfirmButton.setOnClickListener(getConfirmButtonOnClickListener());

        mNewOrderSchedulePickupFragment = new NewOrderSchedulePickupFragment(this);
        mNewOrderSelectServiceFragment = new NewOrderSelectServiceFragment(this);
        mNewOrderConfirmFragment = new NewOrderConfirmFragment(this);

        mBottomPanel = (RelativeLayout) findViewById(R.id.panel_bottom);

        if (Session.getDefaultPlace() != null) {
            mServiceAreaBean = Session.getDefaultPlace().getServiceArea();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setProgressBarOnTop(startOrderStep);
        setTitleOnNavigationBar(startOrderStep);

        if (!mIsInitialize) {

            refreshSchedule();
            mIsInitialize = true;
        }
    }

    private void refreshSchedule() {

        OrderBean orderBean = Session.getOrder();
        Date date = orderBean.getCollectionDate() != null ? CommonUtil.getDateWithoutTime(orderBean.getCollectionDate()) : CommonUtil.getCurrentDate();

        boolean isScheduleAvailable = mServiceAreaBean != null;
        mNewOrderSchedulePickupFragment.setScheduleAvailable(isScheduleAvailable);

        if (isScheduleAvailable) {

            showProgressDlg(getString(R.string.message_async_loading));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.initOrder(Session.getCustomer(), date, mServiceAreaBean);
        }
    }


    private void goToPrevStep() {

        if (startOrderStep == 0) {
            showConfirmDlg(CONFIRM_CANCEL_ORDER, getString(R.string.confirm_cancel_order));
        }

        if (startOrderStep > 0) {
            startOrderStep--;
        }

        setReverseProgressBarOnTop(startOrderStep);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            goToPrevStep();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        goToPrevStep();
    }

    @Override
    public void onOrderCompleted() {

        Intent orderCompleteIntent = new Intent(this, NewOrderCompleteActivity.class);
        startActivity(orderCompleteIntent);
        finish();
    }

    @Override
    public void onOrderCancelled() {

        finish();
    }

    private View.OnClickListener getNextStepButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startOrderStep == 0) {

                    if (mServiceAreaBean == null) {
                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_out_of_service_area));
                        return;
                    }

                    mNewOrderSchedulePickupFragment.saveCollectionTime();

                } else if (startOrderStep == 1) {

                    OrderBean orderBean = Session.getOrder();

                    if (orderBean.getSpeedType() == null || orderBean.getDeliveryDate() == null) {

                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_select_delivery_date));
                        return;

                    } else if (orderBean.isEmptyOrder()) {

                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_empty_service));
                        return;

                    } else if (orderBean.getTotalCharge() < Session.getMinimumOrder()) {

                        String minimumOrder = CommonUtil.formatCurrency(Session.getMinimumOrder());
                        NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_minimum_order, minimumOrder));
                        return;
                    }
                }

                if (startOrderStep < 3) {
                    startOrderStep++;
                }

                setProgressBarOnTop(startOrderStep);
            }
        };
    }

    private View.OnClickListener getConfirmButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDlg(CONFIRM_SUBMIT_ORDER, getString(R.string.confirm_submit_order));
            }
        };
    }

    private void setProgressBarOnTop(int step) {
        switch (step) {
            case 0:
                setProgressWithAnimation(0, 33);
                addFragment(mNewOrderSchedulePickupFragment, "mNewOrderSchedulePickupFragment");
                setTitleOnNavigationBar(step);
                mConfirmButton.setVisibility(View.INVISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                break;

            case 1:
                setProgressWithAnimation(33, 66);
                replaceFragment(mNewOrderSelectServiceFragment, "mNewOrderSelectServiceFragment");
                setTitleOnNavigationBar(step);
                mConfirmButton.setVisibility(View.INVISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                break;

            case 2:
                setProgressWithAnimation(66, 99);
                replaceFragment(mNewOrderConfirmFragment, "mNewOrderConfirmFragment");
                setTitleOnNavigationBar(step);
                mNextButton.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setReverseProgressBarOnTop(int step) {
        switch (step) {
            case 0:
                setProgressWithAnimation(66, 33);
                replaceFragment(mNewOrderSchedulePickupFragment, "mNewOrderSchedulePickupFragment");
                setTitleOnNavigationBar(step);
                mConfirmButton.setVisibility(View.INVISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                break;

            case 1:
                setProgressWithAnimation(99, 66);
                replaceFragment(mNewOrderSelectServiceFragment, "mNewOrderSelectServiceFragment");
                setTitleOnNavigationBar(step);
                mConfirmButton.setVisibility(View.INVISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_SUBMIT_ORDER) {

            showProgressDlg(getString(R.string.message_async_processing_order));

            OrderBean orderBean = Session.getOrder();
            orderBean.setCustomer(Session.getCustomer());
            orderBean.setOrderDate(new Date());
            orderBean.setStatus(Constant.ORDER_STATUS_NEW_ORDER);

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
            httpAsyncManager.submitOrder(orderBean);

        } else if (confirmId == CONFIRM_CANCEL_ORDER) {

            finish();
        }
    }

    private void setTitleOnNavigationBar(int step) {
        View view = getSupportActionBar().getCustomView();
        mTitleLabel = (AppCompatTextView) view.findViewById(R.id.label_title);
        switch (step) {
            case 0:
                mTitleLabel.setText("SCHEDULE PICK UP");
                break;

            case 1:
                mTitleLabel.setText("SELECT SERVICES");
                break;

            case 2:
                mTitleLabel.setText("ORDER SUMMARY");
                break;
        }
        mTitleLabel.startAnimation(setAnimationOnNavigationTitle());
    }

    private Drawable changeBackArrowColor() {
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.icon_back_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    private void setProgressWithAnimation(int value1, int value2) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mNewOrderProgressBar, "progress", value1, value2);
        progressAnimator.setDuration(250);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    private Animation setAnimationOnNavigationTitle() {
        Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(500);
        in.setInterpolator(new AccelerateDecelerateInterpolator());

        return in;
    }

    @Override
    protected void onKeyboardVisible() {
        mBottomPanel.setVisibility(View.GONE);
    }

    @Override
    protected void onKeyboardGone() {
        mBottomPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeliveryDateUpdated() {

        mNewOrderSelectServiceFragment.refreshDeliveryDate();
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        mNewOrderSchedulePickupFragment.refreshCollectionDateTimes();
    };

    public void onAsyncGetOutstandingOrders(List<OrderBean> orderBeans) {

        hideProgressDlgNow();

        Session.setOutstandingOrders(orderBeans);

        Intent orderCompleteIntent = new Intent(this, NewOrderCompleteActivity.class);
        startActivity(orderCompleteIntent);
        finish();
    }

    @Override
    public void onChangePlace(PlaceBean placeBean) {

        if (mServiceAreaBean == null || placeBean.getServiceArea() == null ||
                placeBean.getServiceArea().getId().longValue() != mServiceAreaBean.getId().longValue()) {

            mServiceAreaBean = placeBean.getServiceArea();
            refreshSchedule();
        }
    }
}