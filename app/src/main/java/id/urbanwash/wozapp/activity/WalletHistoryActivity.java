package id.urbanwash.wozapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import id.urbanwash.wozapp.adapter.WalletHistoryAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class WalletHistoryActivity extends BaseActivity {

    private AppCompatTextView mTitleLabel;

    private RecyclerView mWalletHistoryView;
    private WalletHistoryAdapter mWalletHistoryAdapter;

    private AppCompatActivity mAppCompatActivity;

    List<CreditBean> mCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallet_history);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_wallet_history));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mWalletHistoryView = (RecyclerView) findViewById(R.id.view_wallet_history);

        mWalletHistoryView.setHasFixedSize(true);
        mWalletHistoryView.setLayoutManager(mLayoutManager);
        mWalletHistoryView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mWalletHistoryView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mCredits = new ArrayList<CreditBean>();

        mWalletHistoryAdapter = new WalletHistoryAdapter(mAppCompatActivity, mCredits);
        mWalletHistoryView.setAdapter(mWalletHistoryAdapter);

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getCredits(Session.getCustomer(), mPage);
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

    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getCredits(Session.getCustomer(), mPage);
    }

    @Override
    public void onAsyncGetCredits(List<CreditBean> credits) {

        hideProgressDlgNow();

        if (mPage == 1) {
            mCredits.clear();
        }

        if (credits.size() > 0) {

            mCredits.addAll(credits);
            mWalletHistoryAdapter.notifyDataSetChanged();

            if (credits.size() < Constant.MAX_FETCH) {
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
}