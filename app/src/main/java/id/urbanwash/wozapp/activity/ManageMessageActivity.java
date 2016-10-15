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
import id.urbanwash.wozapp.adapter.ManageMessageAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.MessageBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

public class ManageMessageActivity extends BaseActivity {

    private AppCompatTextView mTitleLabel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mManageMessageView;

    private ManageMessageAdapter mManageMessageAdapter;

    private AppCompatActivity mAppCompatActivity;

    private AppCompatTextView mNewMessageButton;

    List<MessageBean> mMessages;
    private MessageBean mMessageBean;

    private static final int NEW_MESSAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_message);
        CommonUtil.setContext(this);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        mTitleLabel.setText(getString(R.string.title_manage_message));

        mLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mManageMessageView = (RecyclerView) findViewById(R.id.view_manage_message);

        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mNewMessageButton = (AppCompatTextView) findViewById(R.id.button_new_message);
        mNewMessageButton.setOnClickListener(getNewMessageOnClickListener());

        mManageMessageView.setHasFixedSize(true);
        mManageMessageView.setLayoutManager(mLayoutManager);
        mManageMessageView.addItemDecoration(new RecycleDividerItemDecoration(this));
        mManageMessageView.addOnScrollListener(getRecyclerViewOnScrollListener());

        mMessages = Session.getMessages();

        if (mMessages == null) {
            mMessages = new ArrayList<MessageBean>();
        }

        mManageMessageAdapter = new ManageMessageAdapter(mAppCompatActivity, mMessages);
        mManageMessageView.setAdapter(mManageMessageAdapter);
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
    protected void getNextPageRecords() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getMessages(mPage);
    }

    @Override
    public void onAsyncGetMessages(List<MessageBean> messages) {

        if (mPage == 1) {
            mMessages.clear();
        }

        if (messages.size() > 0) {

            hideProgressDlgNow();
            mMessages.addAll(messages);
            mManageMessageAdapter.notifyDataSetChanged();

            if (messages.size() < Constant.MAX_FETCH) {
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

    private View.OnClickListener getNewMessageOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startOrderIntent = new Intent(mAppCompatActivity, MessageDetailActivity.class);
                startActivityForResult(startOrderIntent, NEW_MESSAGE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == NEW_MESSAGE) {

                mPage = 1;

                List<MessageBean> messages = Session.getMessages();

                mMessages.clear();
                mMessages.addAll(messages);
                mManageMessageAdapter.notifyDataSetChanged();
            }
        }
    }
}
