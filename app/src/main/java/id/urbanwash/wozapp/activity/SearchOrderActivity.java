package id.urbanwash.wozapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.ManageOrderAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ProcessOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class SearchOrderActivity extends BaseActivity implements ProcessOrderListener {

    private AppCompatTextView mTitleLabel;

    private RecyclerView mManageOrderView;
    private ManageOrderAdapter mManageOrderAdapter;

    private AppCompatActivity mAppCompatActivity;
    private AppCompatEditText mSearchText;
    private AppCompatImageView mSearchButton;

    String mStatus;
    List<OrderBean> mOrders;

    private static int PROCESS_ORDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_order);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        Intent intent = getIntent();
        mStatus = intent.getStringExtra(Constant.DATA_ORDER_STATUS);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_search_order));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSearchText = (AppCompatEditText) findViewById(R.id.text_search);
        mSearchButton = (AppCompatImageView) findViewById(R.id.button_search);
        mManageOrderView = (RecyclerView) findViewById(R.id.view_process_order);

        mSearchButton.setOnClickListener(getSearchButtonOnClickListener());

        mManageOrderView.setHasFixedSize(true);
        mManageOrderView.setLayoutManager(mLayoutManager);
        mManageOrderView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mManageOrderView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mOrders = new ArrayList<OrderBean>();

        boolean isStatusRequired = true;

        mManageOrderAdapter = new ManageOrderAdapter(mAppCompatActivity, mOrders, isStatusRequired);
        mManageOrderView.setAdapter(mManageOrderAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onOrderSelected(OrderBean orderBean) {

        Session.setOrder(orderBean);
        Session.setPic(null);

        Intent intent = new Intent(this, ProcessOrderActivity.class);
        startActivityForResult(intent, PROCESS_ORDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == PROCESS_ORDER) {

                String status = data.getStringExtra(Constant.INTENT_DATA_STATUS);

                if (!CommonUtil.isEmpty(status)) {
                    mTitleLabel.setText(CommonUtil.getProcessOrderTitle(mStatus));
                }

                mOrders.clear();
                mOrders.addAll(Session.getOrders());

                mManageOrderAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.searchOrders(mSearchText.getText().toString().trim(), mPage);
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orders) {

        hideProgressDlgNow();

        if (mPage == 1) {
            mOrders.clear();
        }

        if (orders.size() > 0) {

            mOrders.addAll(orders);
            mManageOrderAdapter.notifyDataSetChanged();

            if (orders.size() < Constant.MAX_FETCH) {
                setReachLastPage(true);
            }

        } else {

            setReachLastPage(true);

            if (mPage == 1) {
                NotificationUtil.showInfoMessage(mAppCompatActivity, getString(R.string.alert_no_matching_record_found));
            } else {
                //NotificationUtil.showInfoMessage(mAppCompatActivity, getString(R.string.alert_no_more_data_to_be_fetched));
            }
        }

        setLoading(false);
    }

    public View.OnClickListener getSearchButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPage = 1;
                setReachLastPage(false);

                showProgressDlg(getString(R.string.message_async_searching));

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                httpAsyncManager.searchOrders(mSearchText.getText().toString().trim(), mPage);
            }
        };
    }

    @Override
    public void onDeliveryDateUpdated() {}
}