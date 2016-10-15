package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;

import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.MainAdapter;

import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.view.CustomViewPager;
import id.urbanwash.wozapp.widget.DepthPageTransformer;

public class MainActivity extends BaseActivity implements MainListener {

    private TabLayout mMainTab;
    private ViewPager mMainPager;
    private MainAdapter mMainAdapter;

    private String mStatus;
    private PlaceBean mPlaceBean;

    public static final int ACTIVITY_MANAGE_ORDER = 1;
    public static final int ACTIVITY_UPDATE_PROFILE = 2;

    private static final int CONFIRM_DELETE_PLACE = 1;
    private static final int CONFIRM_LOGOUT = 2;

    private boolean mIsInitialize = false;
    private boolean mIsShowOrderDetail = false;

    private String mOrderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainTab = (TabLayout) findViewById(R.id.tab_main);
        mMainPager = (CustomViewPager) findViewById(R.id.pager_main);
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), this, this);
        mMainPager.setAdapter(mMainAdapter);
        mMainPager.setPageTransformer(true, new DepthPageTransformer());
        mMainTab.setupWithViewPager(mMainPager);

        for (int i = 0; i < mMainTab.getTabCount(); i++) {
            setIconTabLayoutBasedOnIndeks(i);
        }

        mMainTab.setOnTabSelectedListener(new SetActiveTabIcon());
    }

    @Override
    public void onStart() {

        super.onStart();

        if (!mIsInitialize && !mIsShowOrderDetail) {

            if (Session.isCustomer()) {
                refreshCustomerOrders();

            } else {
                refreshOrderSummary();
            }

            mIsInitialize = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        mOrderNo = intent.getStringExtra(Constant.DATA_ORDER);

        if (!CommonUtil.isEmpty(mOrderNo)) {

            showOrderDetail();
        }
    }

    private void setIconTabLayoutBasedOnIndeks(int indeks) {

        TabLayout.Tab tab = mMainTab.getTabAt(indeks);

        if (tab != null) {
            tab.setCustomView(mMainAdapter.getTabView(indeks));
            ((AppCompatImageView) tab.getCustomView().findViewById(R.id.image_tab)).setColorFilter(ContextCompat.getColor(this, R.color.colorDarkGrey), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void showOrderDetail() {

        mIsShowOrderDetail = true;

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
        httpAsyncManager.searchOrders(mOrderNo, 1);
    }

    public void refreshCustomerOrders() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
        httpAsyncManager.refreshCustomerData(Session.getCustomer());
    }

    @Override
    public void refreshOrderSummary() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);

        if (Session.isAdminUser()) {
            httpAsyncManager.refreshAdminData(Session.getEmployee());
        } else {
            httpAsyncManager.refreshTransporterData(Session.getEmployee());
        }
    }

    @Override
    public void refreshPendingCreditCount() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
        httpAsyncManager.refreshPendingCreditCount();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void ShowProfileFromMainView() {
        mMainPager.setCurrentItem(5, true);
    }

    class SetActiveTabIcon implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mMainPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orders) {

        hideProgressDlgNow();

        if (mIsShowOrderDetail) {

            mIsShowOrderDetail = false;

            if (orders.size() == 0) {
                return;
            }

            OrderBean orderBean = orders.get(0);

            Session.setOrder(orderBean);
            Session.setPic(null);

            if (Session.isCustomer()) {

                Intent intent = new Intent(this, TrackingOrderActivity.class);
                startActivity(intent);

            } else {

                Intent intent = new Intent(this, ProcessOrderActivity.class);
                startActivity(intent);
            }

        } else {

            Session.setOrders(orders);

            Intent intent = new Intent(this, ManageOrderActivity.class);
            intent.putExtra(Constant.DATA_ORDER_STATUS, mStatus);
            startActivityForResult(intent, ACTIVITY_MANAGE_ORDER);
        }
    }

    @Override
    public void onAsyncGetOutstandingOrders(List<OrderBean> orders) {

        hideProgressDlgNow();

        Session.setOutstandingOrders(orders);
        mMainAdapter.refreshOutstandingOrders();
    }

    @Override
    public void onAsyncGetCompletedOrders(List<OrderBean> orders) {

        hideProgressDlgNow();

        Session.setCompletedOrders(orders);
        mMainAdapter.refreshCompletedOrders();
    }

    @Override
    public void onAsyncGetCredits(List<CreditBean> credits) {

        hideProgressDlgNow();

        Session.setCredits(credits);
        mMainAdapter.refreshContent();
    }

    @Override
    public void onAsyncGetPlaces(List<PlaceBean> places) {

        hideProgressDlgNow();

        Session.setPlaces(places);
        mMainAdapter.refreshPlaces();
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        if (Session.isCustomer()) {
            mMainAdapter.refreshOutstandingOrders();

        } else if (Session.isAdminUser() || Session.isTransporterUser()) {
            mMainAdapter.refreshOrderSummary();
            mMainAdapter.refreshContent();
        }
    }

    @Override
    public void onProcessOrder(String status) {

        showProgressDlg(getString(R.string.message_async_loading));

        EmployeeBean employeeBean = null;

        if (Session.isTransporterUser()) {
            employeeBean = Session.getEmployee();
        }

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
        httpAsyncManager.getOrdersByStatus(employeeBean, status, 1);

        mStatus = status;
    }

    @Override
    public void onSearchOrder() {

        Intent intent = new Intent(getApplicationContext(), SearchOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefreshOrders() {

        if (Session.isCustomer()) {

            showProgressDlg(getString(R.string.message_async_loading));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.refreshCustomerData(Session.getCustomer());
        }
    }

    @Override
    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_DELETE_PLACE) {

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.deletePlace(mPlaceBean);

        } else if (confirmId == CONFIRM_LOGOUT) {

            showProgressDlg(getString(R.string.message_async_sign_out));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);

            if (Session.isCustomer()) {
                httpAsyncManager.logout(Session.getCustomer());

            } else {
                httpAsyncManager.logout(Session.getEmployee());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == ACTIVITY_MANAGE_ORDER) {

                if (Session.isAdminUser()) {

                    showProgressDlg(getString(R.string.message_async_loading));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
                    httpAsyncManager.refreshAdminData(Session.getEmployee());

                } else if (Session.isAdminUser()) {

                    showProgressDlg(getString(R.string.message_async_loading));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
                    httpAsyncManager.refreshTransporterData(Session.getEmployee());
                }

            } else if (requestCode == ACTIVITY_UPDATE_PROFILE) {

                mMainAdapter.refreshProfile();
                mMainAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onUpdateProfile() {

        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivityForResult(intent, ACTIVITY_UPDATE_PROFILE);
    }

    @Override
    public void onDeletePlace(PlaceBean placeBean) {

        mPlaceBean = placeBean;

        showConfirmDlg(CONFIRM_DELETE_PLACE, getString(R.string.confirm_delete_place));
    }

    @Override
    public void onLogout() {

        showConfirmDlg(CONFIRM_LOGOUT, getString(R.string.confirm_logout));
    }

    @Override
    public void onAsyncLogout() {

        hideProgressDlgNow();

        Session.clear();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }
}
