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
import id.urbanwash.wozapp.adapter.SearchCustomerAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.SearchCustomerListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class SearchCustomerActivity extends BaseActivity implements SearchCustomerListener {

    private AppCompatTextView mTitleLabel;

    private RecyclerView mSearchCustomerView;
    private SearchCustomerAdapter mSearchCustomerAdapter;

    private AppCompatActivity mAppCompatActivity;
    private AppCompatEditText mSearchText;
    private AppCompatImageView mSearchButton;

    private AppCompatTextView mNewCustomerButton;

    String mStatus;
    List<CustomerBean> mCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_customer);
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
        mTitleLabel.setText(getString(R.string.title_search_customer));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSearchText = (AppCompatEditText) findViewById(R.id.text_search);
        mSearchButton = (AppCompatImageView) findViewById(R.id.button_search);
        mSearchCustomerView = (RecyclerView) findViewById(R.id.view_search_customer);

        mNewCustomerButton = (AppCompatTextView) findViewById(R.id.button_new_customer);

        mSearchButton.setOnClickListener(getSearchButtonOnClickListener());
        mNewCustomerButton.setOnClickListener(getNewCustomerButtonOnClickListener());

        mSearchCustomerView.setHasFixedSize(true);
        mSearchCustomerView.setLayoutManager(mLayoutManager);
        mSearchCustomerView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mSearchCustomerView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mCustomers = new ArrayList<CustomerBean>();

        mSearchCustomerAdapter = new SearchCustomerAdapter(mAppCompatActivity, mCustomers, this);
        mSearchCustomerView.setAdapter(mSearchCustomerAdapter);
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
    public void onCustomerSelected(CustomerBean customerBean) {

        Session.setCustomer(customerBean);

        Intent intent = new Intent(this, ShowCustomerActivity.class);
        startActivity(intent);
    }

    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.searchCustomers(mSearchText.getText().toString().trim(), mPage);
    }

    @Override
    public void onAsyncGetCustomers(List<CustomerBean> customers) {

        hideProgressDlgNow();

        if (mPage == 1) {
            mCustomers.clear();
        }

        if (customers.size() > 0) {

            mCustomers.addAll(customers);
            mSearchCustomerAdapter.notifyDataSetChanged();

            if (customers.size() < Constant.MAX_FETCH) {
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

                String search = mSearchText.getText().toString();

                if (CommonUtil.isEmpty(search)) {

                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_search_is_empty));
                    return;
                }

                mPage = 1;
                setReachLastPage(false);

                showProgressDlg(getString(R.string.message_async_searching));

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                httpAsyncManager.searchCustomers(search.trim(), mPage);
            }
        };
    }

    public View.OnClickListener getNewCustomerButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, NewCustomerActivity.class);
                startActivity(intent);
            }
        };
    }
}