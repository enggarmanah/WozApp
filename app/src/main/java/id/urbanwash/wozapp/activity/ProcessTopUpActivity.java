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
import id.urbanwash.wozapp.adapter.ProcessTopUpAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ProcessTopUpListener;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class ProcessTopUpActivity extends BaseActivity implements ProcessTopUpListener {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mProcessTopUpView;

    private ProcessTopUpAdapter mProcessTopUpAdapter;

    private AppCompatActivity mAppCompatActivity;

    List<CreditBean> mCredits;
    private CreditBean mCreditBean;

    private static final int CONFIRM_APPROVE_TOP_UP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process_top_up);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_process_top_up));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mProcessTopUpView = (RecyclerView) findViewById(R.id.view_process_top_up);

        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mProcessTopUpView.setHasFixedSize(true);
        mProcessTopUpView.setLayoutManager(mLayoutManager);
        mProcessTopUpView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mProcessTopUpView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mCredits = Session.getCredits();

        if (mCredits == null) {
            mCredits = new ArrayList<CreditBean>();
        }

        mProcessTopUpAdapter = new ProcessTopUpAdapter(mAppCompatActivity, this, mCredits);
        mProcessTopUpView.setAdapter(mProcessTopUpAdapter);
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
    public void onProcessTopUp(CreditBean creditBean) {

        mCreditBean = creditBean;
        showConfirmDlg(CONFIRM_APPROVE_TOP_UP, getString(R.string.confirm_approve_credit));
    }

    @Override
    public void onAsyncGetCredits(List<CreditBean> credits) {

        hideProgressDlgNow();
        Session.setCredits(credits);

        mCredits.clear();
        mCredits.addAll(Session.getCredits());

        mProcessTopUpAdapter.notifyDataSetChanged();
    }

    @Override
    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getPendingCredits(mPage);
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orderBeans) {

        if (mPage == 1) {
            mCredits.clear();
        }

        if (orderBeans.size() > 0) {

            hideProgressDlgNow();
            mCredits.addAll(mCredits);
            mProcessTopUpAdapter.notifyDataSetChanged();

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

    @Override
    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_APPROVE_TOP_UP) {

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.approveCredit(mCreditBean);
        }
    }
}
