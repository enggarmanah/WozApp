package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import id.urbanwash.wozapp.adapter.ManagePromoAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.ManagePromoListener;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

public class ManagePromoActivity extends BaseActivity implements ManagePromoListener {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mProcessTopUpView;

    private ManagePromoAdapter mManagePromoAdapter;

    private AppCompatActivity mAppCompatActivity;

    private AppCompatTextView mNewPromoButton;

    List<PromoBean> mPromos;
    private PromoBean mPromoBean;

    private static final int NEW_PROMO = 1;
    private static final int UPDATE_PROMO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_promo);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_manage_promo));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mProcessTopUpView = (RecyclerView) findViewById(R.id.view_manage_promo);

        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mNewPromoButton = (AppCompatTextView) findViewById(R.id.button_new_promo);
        mNewPromoButton.setOnClickListener(getNewPromoOnClickListener());

        mProcessTopUpView.setHasFixedSize(true);
        mProcessTopUpView.setLayoutManager(mLayoutManager);
        mProcessTopUpView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mProcessTopUpView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mPromos = Session.getPromos();

        if (mPromos == null) {
            mPromos = new ArrayList<PromoBean>();
        }

        mManagePromoAdapter = new ManagePromoAdapter(mAppCompatActivity, this, mPromos);
        mProcessTopUpView.setAdapter(mManagePromoAdapter);
    }

    @Override
    public void onStart() {

        super.onStart();

        mPage = 1;
        getNextPageRecords();
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
    public void onUpdatePromo(PromoBean promoBean) {

        mPromoBean = promoBean;

        Intent intent = new Intent(mAppCompatActivity, PromoDetailActivity.class);
        intent.putExtra(Constant.DATA_PROMO, mPromoBean);
        startActivityForResult(intent, UPDATE_PROMO);
    }


    @Override
    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getPromos(mPage);
    }

    @Override
    public void onAsyncGetPromos(List<PromoBean> promos) {

        if (mPage == 1) {
            mPromos.clear();
        }

        if (promos.size() > 0) {

            hideProgressDlgNow();
            mPromos.addAll(promos);
            mManagePromoAdapter.notifyDataSetChanged();

            if (promos.size() < Constant.MAX_FETCH) {
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

    private View.OnClickListener getNewPromoOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startOrderIntent = new Intent(mAppCompatActivity, PromoDetailActivity.class);
                startActivityForResult(startOrderIntent, NEW_PROMO);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == NEW_PROMO || requestCode == UPDATE_PROMO) {

                mPage = 1;

                List<PromoBean> promos = Session.getPromos();

                mPromos.clear();
                mPromos.addAll(promos);
                mManagePromoAdapter.notifyDataSetChanged();
            }
        }
    }
}
