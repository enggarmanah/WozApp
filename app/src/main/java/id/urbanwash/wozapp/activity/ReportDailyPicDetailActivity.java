package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.ReportDailyPicDetailAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

public class ReportDailyPicDetailActivity extends BaseActivity {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mReportView;

    private AppCompatTextView mWalletText;
    private AppCompatTextView mCashText;
    private AppCompatTextView mTotalText;

    private ReportDailyPicDetailAdapter mReportDailyPicDetailAdapter;

    private AppCompatActivity mAppCompatActivity;

    List<ReportBean> mReports;

    EmployeeBean mPic;
    Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_daily_pic_detail);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mDate = (Date) getIntent().getSerializableExtra(Constant.DATA_DATE);
        mPic = (EmployeeBean) getIntent().getSerializableExtra(Constant.DATA_EMPLOYEE);

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(mPic.getName() + ", " + CommonUtil.formatDate(mDate));

        mWalletText = (AppCompatTextView) findViewById(R.id.label_wallet);
        mCashText = (AppCompatTextView) findViewById(R.id.label_cash);
        mTotalText = (AppCompatTextView) findViewById(R.id.label_total);

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mReportView = (RecyclerView) findViewById(R.id.view_report);

        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mReportView.setHasFixedSize(true);
        mReportView.setLayoutManager(mLayoutManager);
        mReportView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mReportView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mReports = new ArrayList<ReportBean>();

        mReportDailyPicDetailAdapter = new ReportDailyPicDetailAdapter(mAppCompatActivity, mReports);
        mReportView.setAdapter(mReportDailyPicDetailAdapter);
    }

    public void onStart() {

        super.onStart();

        if (mReports.isEmpty()) {
            getRecords();
        }
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
    public void onAsyncGetReports(List<ReportBean> reports) {

        hideProgressDlgNow();

        mReports.clear();
        mReports.addAll(reports);
        mReportDailyPicDetailAdapter.notifyDataSetChanged();

        setLoading(false);

        float wallet = 0f;
        float cash = 0f;
        float total = 0f;

        for (ReportBean reportBean : reports) {

            if (Constant.PAYMENT_TYPE_WALLET.equals(reportBean.getPaymentType())) {
                wallet += reportBean.getValue();

            } else if (Constant.PAYMENT_TYPE_CASH.equals(reportBean.getPaymentType())) {
                cash += reportBean.getValue();
            }

            total += reportBean.getValue();
        }

        mWalletText.setText(CommonUtil.formatCurrency(wallet));
        mCashText.setText(CommonUtil.formatCurrency(cash));
        mTotalText.setText(CommonUtil.formatCurrency(total));
    }

    private SwipeRefreshLayout.OnRefreshListener getViewOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
                getRecords();
            }
        };
    }

    public void getRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getDailyReportPicDetail(mPic, mDate);
    }
}
