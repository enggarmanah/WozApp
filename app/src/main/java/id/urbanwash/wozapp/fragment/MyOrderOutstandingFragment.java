package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.TrackingOrderActivity;
import id.urbanwash.wozapp.adapter.OrderAdapter;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.listener.MyOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.widget.RecycleDividerItemDecoration;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class MyOrderOutstandingFragment extends Fragment implements MyOrderListener {
    
    private View rootView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mMyOrderView;

    private LinearLayoutManager mMyOrderLayoutManager;
    private OrderAdapter mMyOrderAdapter;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    List<OrderBean> mOrders;

    public MyOrderOutstandingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_my_order_content, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.layout_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(getViewOnRefreshListener());

        mMyOrderLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mMyOrderView = (RecyclerView) rootView.findViewById(R.id.view_my_order);
        mMyOrderView.setHasFixedSize(true);
        mMyOrderView.setLayoutManager(mMyOrderLayoutManager);
        mMyOrderView.addItemDecoration(new RecycleDividerItemDecoration(getActivity()));

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        mOrders = Session.getOutstandingOrders();

        if (mOrders == null) {
            mOrders = new ArrayList<OrderBean>();
        }

        mMyOrderAdapter = new OrderAdapter(mAppCompatActivity, 0, this, mOrders);
        mMyOrderView.setAdapter(mMyOrderAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AppCompatActivity){
            mAppCompatActivity = (AppCompatActivity) context;
            mMainListener = (MainListener) mAppCompatActivity;
        }
    }

    @Override
    public void onShowOrderDetail(OrderBean orderBean) {

        Session.setOrder(orderBean);

        Intent trackingOrderIntent = new Intent(getActivity(), TrackingOrderActivity.class);
        getActivity().startActivity(trackingOrderIntent);

        //PushNotificationUtil.setContext(getContext());
        //PushNotificationUtil.createNotification(orderBean.getOrderNo(), orderBean.getStatus(), orderBean.getCollectionDate(), orderBean.getPlaceName(), orderBean.getPlaceAddress());
    }

    public void refreshOutstandingOrders() {

        mOrders.clear();
        mOrders.addAll(Session.getOutstandingOrders());

        mMyOrderAdapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private SwipeRefreshLayout.OnRefreshListener getViewOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
                mMainListener.onRefreshOrders();
            }
        };
    }
}
