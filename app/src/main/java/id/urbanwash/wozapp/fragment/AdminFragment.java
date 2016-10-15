package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.ManageMessageActivity;
import id.urbanwash.wozapp.activity.ManagePromoActivity;
import id.urbanwash.wozapp.activity.ProcessTopUpActivity;
import id.urbanwash.wozapp.activity.SearchCustomerActivity;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

@SuppressLint("ValidFragment")
public class AdminFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AppCompatTextView mProcessTopUpCountLabel;

    private AppCompatTextView mProcessTopUpButton;
    private AppCompatTextView mPromoButton;
    private AppCompatTextView mMessageButton;
    private AppCompatTextView mSearchCustomerButton;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public AdminFragment() {}

    public AdminFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mMainListener = (MainListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.content_admin, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.layout_swipe_refresh);

        mProcessTopUpCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_process_top_up);

        mProcessTopUpButton = (AppCompatTextView) mRootView.findViewById(R.id.button_process_top_up);
        mPromoButton = (AppCompatTextView) mRootView.findViewById(R.id.button_promo);
        mMessageButton = (AppCompatTextView) mRootView.findViewById(R.id.button_message);
        mSearchCustomerButton = (AppCompatTextView) mRootView.findViewById(R.id.button_search_customer);

        mSwipeRefreshLayout.setOnRefreshListener(getMainPanelOnRefreshListener());
        mProcessTopUpButton.setOnClickListener(getProcessTopUpButtonOnClickListener());
        mPromoButton.setOnClickListener(getPromoButtonOnClickListener());
        mMessageButton.setOnClickListener(getMessageButtonOnClickListener());
        mSearchCustomerButton.setOnClickListener(getSearchCustomerButtonOnClickListener());

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        updateContent();

        return mRootView;
    }

    private void updateContent() {

        Integer pendingCreditCount = Session.getPendingCreditCount();

        mProcessTopUpCountLabel.setText(CommonUtil.formatNumber(pendingCreditCount));
    }

    public void refreshContent() {

        updateContent();
    }

    private View.OnClickListener getProcessTopUpButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ProcessTopUpActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getPromoButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ManagePromoActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getMessageButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ManageMessageActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getSearchCustomerButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, SearchCustomerActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getMainPanelOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
                mMainListener.refreshPendingCreditCount();
            }
        };
    }
}
