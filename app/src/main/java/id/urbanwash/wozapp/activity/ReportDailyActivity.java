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
import id.urbanwash.wozapp.adapter.ReportDailyAdapter;
import id.urbanwash.wozapp.adapter.ReportDailyPicDetailAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ReportDailyListener;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

public class ReportDailyActivity extends BaseActivity implements ReportDailyListener {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mReportView;

    private AppCompatTextView mTotalText;

    private ReportDailyAdapter mReportDailyAdapter;

    private AppCompatActivity mAppCompatActivity;

    List<ReportBean> mReports;

    Date mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_daily);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mMonth = (Date) getIntent().getSerializableExtra(Constant.DATA_DATE);

        if (mMonth == null) {
            mMonth = new Date();
        }

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(CommonUtil.formatMonthYear(mMonth));

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

        mReportDailyAdapter = new ReportDailyAdapter(mAppCompatActivity, this, mReports);
        mReportView.setAdapter(mReportDailyAdapter);
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
    public void onDateSelected(Date date) {

        if (Session.isAdminUser()) {
            Intent intent = new Intent(mAppCompatActivity, ReportDailyPicActivity.class);
            intent.putExtra(Constant.DATA_DATE, date);
            startActivity(intent);

        } else if (Session.isTransporterUser()) {
            Intent intent = new Intent(mAppCompatActivity, ReportDailyPicDetailActivity.class);
            intent.putExtra(Constant.DATA_DATE, date);
            intent.putExtra(Constant.DATA_EMPLOYEE, Session.getEmployee());
            startActivity(intent);
        }
    }

    @Override
    public void onAsyncGetReports(List<ReportBean> reports) {

        hideProgressDlgNow();

        mReports.clear();
        mReports.addAll(reports);
        mReportDailyAdapter.notifyDataSetChanged();

        setLoading(false);

        float total = 0f;

        for (ReportBean reportBean : reports) {
            total += reportBean.getValue();
        }

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

        if (Session.isTransporterUser()) {
            httpAsyncManager.getDailyReport(mMonth, Session.getEmployee());
        } else {
            httpAsyncManager.getDailyReport(mMonth);
        }
    }
}
