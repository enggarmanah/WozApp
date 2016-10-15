package id.urbanwash.wozapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.ManageOrderAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ProcessOrderListener;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class ManageOrderActivity extends BaseActivity implements ProcessOrderListener {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mManageOrderView;

    private ManageOrderAdapter mManageOrderAdapter;

    private AppCompatActivity mAppCompatActivity;

    String mStatus;
    List<OrderBean> mOrders;

    private static int PROCESS_ORDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_order);
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
        mTitleLabel.setText(CommonUtil.getProcessOrderTitle(mStatus));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mManageOrderView = (RecyclerView) findViewById(R.id.view_process_order);

        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mManageOrderView.setHasFixedSize(true);
        mManageOrderView.setLayoutManager(mLayoutManager);
        mManageOrderView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mManageOrderView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mOrders = Session.getOrders();

        if (mOrders == null) {
            mOrders = new ArrayList<OrderBean>();
        }

        if (mOrders.size() < Constant.MAX_FETCH) {
            setReachLastPage(true);
        } else {
            setReachLastPage(false);
        }

        boolean isStatusRequired = Constant.ORDER_STATUS_WARNING.equals(mStatus) || Constant.ORDER_STATUS_CRITICAL.equals(mStatus);

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
                    mTitleLabel.setText(CommonUtil.getProcessOrderTitle(status));
                }

                mOrders.clear();
                mOrders.addAll(Session.getOrders());

                if (mOrders.size() < Constant.MAX_FETCH) {
                    setReachLastPage(true);
                } else {
                    setReachLastPage(false);
                }

                mManageOrderAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        EmployeeBean employeeBean = null;

        if (Session.isTransporterUser()) {
            employeeBean = Session.getEmployee();
        }

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getOrdersByStatus(employeeBean, mStatus, mPage);
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orderBeans) {

        if (mPage == 1) {
            mOrders.clear();
        }

        if (orderBeans.size() > 0) {

            hideProgressDlgNow();
            mOrders.addAll(orderBeans);
            mManageOrderAdapter.notifyDataSetChanged();

            if (orderBeans.size() < Constant.MAX_FETCH) {
                setReachLastPage(true);
            }

        } else {

            setReachLastPage(true);
            hideProgressDlgNow();

            //NotificationUtil.showInfoMessage(mAppCompatActivity, getString(R.string.alert_no_more_data_to_be_fetched));
        }

        setLoading(false);
    }

    @Override
    public void onDeliveryDateUpdated() {}

    private SwipeRefreshLayout.OnRefreshListener getViewOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);

                mPage = 1;
                setReachLastPage(false);
                getNextPageRecords();
            }
        };
    }
}
