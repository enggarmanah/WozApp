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
import java.util.HashMap;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.ReportDailyAdapter;
import id.urbanwash.wozapp.adapter.ReportDailyCollectionAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ReportDailyListener;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

public class ReportDailyCollectionActivity extends BaseActivity implements ReportDailyListener {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mReportView;

    private AppCompatTextView mTotalText;

    private ReportDailyCollectionAdapter mReportDailyCollectionAdapter;

    private AppCompatActivity mAppCompatActivity;

    List<ReportBean> mReports;

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

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_order_collection));

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

        mReportDailyCollectionAdapter = new ReportDailyCollectionAdapter(mAppCompatActivity, this, mReports);
        mReportView.setAdapter(mReportDailyCollectionAdapter);
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

        Intent intent = new Intent(mAppCompatActivity, ReportHourlyCollectionActivity.class);
        intent.putExtra(Constant.DATA_DATE, date);
        startActivity(intent);
    }

    @Override
    public void onAsyncGetReports(List<ReportBean> reports) {

        hideProgressDlgNow();

        mReports.clear();

        mReports.addAll(reports);

        mReportDailyCollectionAdapter.notifyDataSetChanged();

        setLoading(false);

        float total = 0f;

        for (ReportBean reportBean : reports) {
            total += reportBean.getValue();
        }

        mTotalText.setText(CommonUtil.formatNumber(total));
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
            httpAsyncManager.getDailyCollectionReport(Session.getEmployee());
        } else {
            httpAsyncManager.getDailyCollectionReport();
        }
    }
}
