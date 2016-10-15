package id.urbanwash.wozapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.Date;
import java.util.List;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.SelectServiceListener;
import id.urbanwash.wozapp.fragment.NewOrderSelectServiceFragment;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class UpdateOrderActivity extends BaseActivity implements SelectServiceListener {

    private AppCompatImageView mNextButton;

    private AppCompatActivity mAppCompatActivity;

    private NewOrderSelectServiceFragment mNewOrderSelectServiceFragment;

    private static final String PLACE_FRAGMENT = "PLACE_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);

        mAppCompatActivity = this;

        Intent intent = getIntent();
        boolean isRepeatOrder = intent.getBooleanExtra(Constant.DATA_REPEAT_ORDER, false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mNewOrderSelectServiceFragment = new NewOrderSelectServiceFragment(mAppCompatActivity);

        replaceFragment(mNewOrderSelectServiceFragment, PLACE_FRAGMENT);

        mNextButton = (AppCompatImageView) findViewById(R.id.button_next);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_update_order));

        mNextButton.setOnClickListener(getOkButtonOnClickListener());
    }

    private View.OnClickListener getOkButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        };
    }

    @Override
    public void onGetDeliveryDates() {

        OrderBean orderBean = Session.getOrder();

        showProgressDlg(getString(R.string.message_async_processing));

        Date date = new Date(orderBean.getDeliveryDate().getTime());
        date = CommonUtil.getDateWithoutTime(date);

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.initDeliveryTimes(date, orderBean.getServiceArea());
    }

    @Override
    public void onAsyncGetDeliveryTimes(Map<Date, List<Long>> deliveryDateTimes) {

        hideProgressDlgNow();
        mNewOrderSelectServiceFragment.onReceiveDeliveryDateTimes();
    }
}