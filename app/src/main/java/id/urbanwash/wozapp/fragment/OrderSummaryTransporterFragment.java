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
public class OrderSummaryTransporterFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AppCompatTextView mAssignedForCollectionCountLabel;
    private AppCompatTextView mCollectionInProgressCountLabel;
    private AppCompatTextView mCollectedCountLabel;
    private AppCompatTextView mAssignedForDeliveryCountLabel;
    private AppCompatTextView mDeliveryInProgressCountLabel;
    private AppCompatTextView mCompletedCountLabel;

    private AppCompatTextView mAssignedForCollectionButton;
    private AppCompatTextView mCollectionInProgressButton;
    private AppCompatTextView mCollectedButton;
    private AppCompatTextView mAssignedForDeliveryButton;
    private AppCompatTextView mDeliveryInProgressButton;
    private AppCompatTextView mCompletedButton;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public OrderSummaryTransporterFragment() {}

    public OrderSummaryTransporterFragment(AppCompatActivity appCompatActivity) {

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

        mRootView = inflater.inflate(R.layout.content_order_summary_transporter, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.layout_swipe_refresh);

        mAssignedForCollectionCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_assigned_for_collection);
        mCollectionInProgressCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_collection_in_progress);
        mCollectedCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_collected);
        mAssignedForDeliveryCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_assigned_for_delivery);
        mDeliveryInProgressCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_delivery);
        mCompletedCountLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_count_completed);

        mAssignedForCollectionButton = (AppCompatTextView) mRootView.findViewById(R.id.button_assigned_for_collection);
        mCollectionInProgressButton = (AppCompatTextView) mRootView.findViewById(R.id.button_collection_in_progress);
        mCollectedButton = (AppCompatTextView) mRootView.findViewById(R.id.button_collected);
        mAssignedForDeliveryButton = (AppCompatTextView) mRootView.findViewById(R.id.button_assigned_for_delivery);
        mDeliveryInProgressButton = (AppCompatTextView) mRootView.findViewById(R.id.button_delivery);
        mCompletedButton = (AppCompatTextView) mRootView.findViewById(R.id.button_completed);

        mSwipeRefreshLayout.setOnRefreshListener(getMainPanelOnRefreshListener());
        mAssignedForCollectionButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION));
        mCollectionInProgressButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS));
        mCollectedButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COLLECTED));
        mAssignedForDeliveryButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY));
        mDeliveryInProgressButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS));
        mCompletedButton.setOnClickListener(getProcessOrderButtonOnClickListener(Constant.ORDER_STATUS_COMPLETED));

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        updateContent();

        return mRootView;
    }

    private void updateContent() {

        Map<String, Long> orderSummary = Session.getOrderSummary();

        Long assignedForCollectionCount = orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION) != null ? orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION) : 0;
        Long collectionInProgressCount = orderSummary.get(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS) != null ? orderSummary.get(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS) : 0;
        Long collectedCount = orderSummary.get(Constant.ORDER_STATUS_COLLECTED) != null ? orderSummary.get(Constant.ORDER_STATUS_COLLECTED) : 0;
        Long assignedForDeliveryCount = orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY) != null ? orderSummary.get(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY) : 0;
        Long deliveryInProgressCount = orderSummary.get(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS) != null ? orderSummary.get(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS) : 0;
        Long completedCount = orderSummary.get(Constant.ORDER_STATUS_COMPLETED) != null ? orderSummary.get(Constant.ORDER_STATUS_COMPLETED) : 0;

        mAssignedForCollectionCountLabel.setText(CommonUtil.formatNumber(assignedForCollectionCount));
        mCollectionInProgressCountLabel.setText(CommonUtil.formatNumber(collectionInProgressCount));
        mCollectedCountLabel.setText(CommonUtil.formatNumber(collectedCount));
        mAssignedForDeliveryCountLabel.setText(CommonUtil.formatNumber(assignedForDeliveryCount));
        mDeliveryInProgressCountLabel.setText(CommonUtil.formatNumber(deliveryInProgressCount));
        mCompletedCountLabel.setText(CommonUtil.formatNumber(completedCount));
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
