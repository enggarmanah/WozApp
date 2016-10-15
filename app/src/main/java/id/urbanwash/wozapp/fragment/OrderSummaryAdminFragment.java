package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/18/16.
 */
@SuppressLint("ValidFragment")
public class OrderSummaryAdminFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayout mAlertPanel;
    private FrameLayout mAlertBorder;

    private RelativeLayout mCriticalPanel;
    private RelativeLayout mWarningPanel;

    private AppCompatTextView mNewOrderCountLabel;
    private AppCompatTextView mWarningCountLabel;
    private AppCompatTextView mCriticalCountLabel;
    private AppCompatTextView mAssignedForCollectionCountLabel;
    private AppCompatTextView mCollectionInProgressCountLabel;
    private AppCompatTextView mCollectedCountLabel;
    private AppCompatTextView mCleaningCountLabel;
    private AppCompatTextView mCleanedCountLabel;
    private AppCompatTextView mAssignedForDeliveryCountLabel;
    private AppCompatTextView mDeliveryInProgressCountLabel;
    private AppCompatTextView mCompletedCountLabel;

    private AppCompatTextView mSearchButton;
    private AppCompatTextView mWarningButton;
    private AppCompatTextView mCriticalButton;
    private AppCompatTextView mNewOrderButton;
    private AppCompatTextView mAssignedForCollectionButton;
    private AppCompatTextView mCollectionInProgressButton;
    private AppCompatTextView mCollectedButton;
    private AppCompatTextView mCleaningButton;
    private AppCompatTextView mCleanedButton;
    private AppCompatTextView mAssignedForDeliveryButton;
    private AppCompatTextView mDeliveryInProgressButton;
    private AppCompatTextView mCompletedButton;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public OrderSummaryAdminFragment() {}

    public OrderSummaryAdminFragment(AppCompatActivity appCompatActivity) {

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

        mRootView = inflater.inflate(R.layout.content_order_summary_admin, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.layout_swipe_refresh);

        mAlertPanel = (LinearLayout) mRootView.findViewById(R.id.panel_alert);
        mAlertBorder = (FrameLayout) mRootView.findViewById(R.id.border_alert);

        mCriticalPanel = (RelativeLayout) mRootView.findViewById(R.id.panel_critical);
        mWarningPanel = (RelativeLayout) mRootView.findViewById(R.id.panel_warning);

        mNewOrderCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_new_order);
        mWarningCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_warning);
        mCriticalCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_critical);
        mAssignedForCollectionCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_assigned_for_collection);
        mCollectionInProgressCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_collection_in_progress);
        mCollectedCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_collected);
        mCleaningCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_cleaning);
        mCleanedCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_cleaned);
        mAssignedForDeliveryCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_assigned_for_delivery);
        mDeliveryInProgressCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_delivery);
        mCompletedCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_completed);

        mSearchButton = (AppCompatTextView) mRootView.findViewById(R.id.button_search);
        mWarningButton = (AppCompatTextView) mRootView.findViewById(R.id.button_warning);
        mCriticalButton = (AppCompatTextView) mRootView.findViewById(R.id.button_critical);
        mNewOrderButton = (AppCompatTextView)  mRootView.findViewById(R.id.button_new_order);
        mAssignedForCollectionButton = (AppCompatTextView) mRootView.findViewById(R.id.button_assigned_for_collection);
        mCollectionInProgressButton = (AppCompatTextView) mRootView.findViewById(R.id.button_collection_in_progress);
        mCollectedButton = (AppCompatTextView) mRootView.findViewById(R.id.button_collected);
        mCleaningButton = (AppCompatTextView) mRootView.findViewById(R.id.button_cleaning);
        mCleanedButton = (AppCompatTextView) mRootView.findViewById(R.id.button_cleaned);
        mAssignedForDeliveryButton = (AppCompatTextView) mRootView.findViewById(R.id.button_assigned_for_delivery);
        mDeliveryInProgressButton = (AppCompatTextView) mRootView.findViewById(R.id.button_delivery);
        mCompletedButton = (AppCompatTextView) mRootView.findViewById(R.id.button_completed);

        mSwipeRefreshLayout.setOnRefreshListener(getMainPanelOnRefreshListener());
        mSearchButton.setOnClickListener(getSearchButtonOnClickListener());
        mWarningButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_WARNING));
        mCriticalButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_CRITICAL));
        mNewOrderButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_NEW_ORDER));
        mAssignedForCollectionButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION));
        mCollectionInProgressButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS));
        mCollectedButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COLLECTED));
        mCleaningButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_CLEANING_IN_PROGRESS));
        mCleanedButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_CLEANED));
        mAssignedForDeliveryButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY));
        mDeliveryInProgressButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS));
        mCompletedButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COMPLETED));

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        updateContent();

        return mRootView;
    }

    private void updateContent() {

        Map<String, Long> orderSummary = Session.getOrderSummary();

        Long newOrderCount = orderSummary.get(Constant.ORDER_STATUS_NEW_ORDER) != null ? orderSummary.get(Constant.ORDER_STATUS_NEW_ORDER) : 0;
        Long warningCount = orderSummary.get(Constant.ORDER_STATUS_WARNING) != null ? orderSummary.get(Constant.ORDER_STATUS_WARNING) : 0;
        Long criticalCount = orderSummary.get(Constant.ORDER_STATUS_CRITICAL) != null ? orderSummary.get(Constant.ORDER_STATUS_CRITICAL) : 0;
        Long assignedForCollectionCount = orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION) != null ? orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION) : 0;
        Long collectionInProgressCount = orderSummary.get(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS) != null ? orderSummary.get(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS) : 0;
        Long collectedCount = orderSummary.get(Constant.ORDER_STATUS_COLLECTED) != null ? orderSummary.get(Constant.ORDER_STATUS_COLLECTED) : 0;
        Long cleaningCount = orderSummary.get(Constant.ORDER_STATUS_CLEANING_IN_PROGRESS) != null ? orderSummary.get(Constant.ORDER_STATUS_CLEANING_IN_PROGRESS) : 0;
        Long cleanedCount = orderSummary.get(Constant.ORDER_STATUS_CLEANED) != null ? orderSummary.get(Constant.ORDER_STATUS_CLEANED) : 0;
        Long assignedForDeliveryCount = orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY) != null ? orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY) : 0;
        Long deliveryInProgressCount = orderSummary.get(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS) != null ? orderSummary.get(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS) : 0;
        Long completedCount = orderSummary.get(Constant.ORDER_STATUS_COMPLETED) != null ? orderSummary.get(Constant.ORDER_STATUS_COMPLETED) : 0;

        mNewOrderCountLabel.setText(CommonUtil.formatNumber(newOrderCount));
        mWarningCountLabel.setText(CommonUtil.formatNumber(warningCount));
        mCriticalCountLabel.setText(CommonUtil.formatNumber(criticalCount));
        mAssignedForCollectionCountLabel.setText(CommonUtil.formatNumber(assignedForCollectionCount));
        mCollectionInProgressCountLabel.setText(CommonUtil.formatNumber(collectionInProgressCount));
        mCollectedCountLabel.setText(CommonUtil.formatNumber(collectedCount));
        mCleaningCountLabel.setText(CommonUtil.formatNumber(cleaningCount));
        mCleanedCountLabel.setText(CommonUtil.formatNumber(cleanedCount));
        mAssignedForDeliveryCountLabel.setText(CommonUtil.formatNumber(assignedForDeliveryCount));
        mDeliveryInProgressCountLabel.setText(CommonUtil.formatNumber(deliveryInProgressCount));
        mCompletedCountLabel.setText(CommonUtil.formatNumber(completedCount));

        mCriticalPanel.setVisibility(View.GONE);

        if (criticalCount != 0) {
            mCriticalPanel.setVisibility(View.VISIBLE);
        }

        mWarningPanel.setVisibility(View.GONE);

        if (warningCount != 0) {
            mWarningPanel.setVisibility(View.VISIBLE);
        }

        mAlertPanel.setVisibility(View.GONE);
        mAlertBorder.setVisibility(View.GONE);

        if (criticalCount != 0 || warningCount != 0) {
            mAlertPanel.setVisibility(View.VISIBLE);
            mAlertBorder.setVisibility(View.VISIBLE);
        }
    }

    public void refreshSummary() {

        updateContent();
    }

    private View.OnClickListener getProcessOrderButtonOnClickListener(final String status) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMainListener.onProcessOrder(status);
            }
        };
    }

    private View.OnClickListener getSearchButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMainListener.onSearchOrder();
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener getMainPanelOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
                mMainListener.refreshOrderSummary();
            }
        };
    }
}
