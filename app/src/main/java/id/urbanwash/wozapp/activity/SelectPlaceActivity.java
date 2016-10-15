package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.PlaceAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.SelectPlaceListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class SelectPlaceActivity extends BaseActivity implements SelectPlaceListener {

    private AppCompatActivity mAppCompatActivity;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mPlaceView;
    private LinearLayoutManager mPlaceLayoutManager;
    private PlaceAdapter mPlaceAdapter;

    private AppCompatTextView mNewPlaceButton;

    private List<PlaceBean> mPlaceBeans;

    private static final int NEW_PLACE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_select_address));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);

        mPlaceView = (RecyclerView) findViewById(R.id.view_select_place);
        mPlaceLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mPlaceView.setHasFixedSize(true);
        mPlaceView.setLayoutManager(mPlaceLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(getMainPanelOnRefreshListener());

        mNewPlaceButton = (AppCompatTextView) findViewById(R.id.button_new_place);
        mNewPlaceButton.setOnClickListener(getNewPlaceButtonOnClickListener());

        mPlaceBeans = Session.getPlaces();

        if (mPlaceBeans == null) {
            mPlaceBeans = new ArrayList<PlaceBean>();
        }

        mPlaceAdapter = new PlaceAdapter(mAppCompatActivity, mPlaceBeans, this);
        mPlaceView.setAdapter(mPlaceAdapter);
    }

    private View.OnClickListener getNewPlaceButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startOrderIntent = new Intent(mAppCompatActivity, NewPlaceActivity.class);
                mAppCompatActivity.startActivityForResult(startOrderIntent, NEW_PLACE);
            }
        };
    }

    @Override
    public void onPlaceSelected(PlaceBean placeBean) {

        Session.setPlace(placeBean);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == NEW_PLACE) {

                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private SwipeRefreshLayout.OnRefreshListener getMainPanelOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);

                showProgressDlg(getString(R.string.message_async_loading));

                CustomerBean customerBean = Session.getCustomer();

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                httpAsyncManager.getPlaces(customerBean);
            }
        };
    }

    @Override
    public void onAsyncGetPlaces(List<PlaceBean> places) {

        hideProgressDlgNow();

        mPlaceBeans.clear();
        mPlaceBeans.addAll(places);

        mPlaceAdapter.notifyDataSetChanged();
    }
}