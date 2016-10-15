package id.urbanwash.wozapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.fragment.TransporterFragment;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.listener.SettingsListener;
import id.urbanwash.wozapp.fragment.AdminFragment;
import id.urbanwash.wozapp.fragment.OrderSummaryAdminFragment;
import id.urbanwash.wozapp.fragment.OrderSummaryTransporterFragment;
import id.urbanwash.wozapp.fragment.ReportFragment;
import id.urbanwash.wozapp.fragment.HomeFragment;
import id.urbanwash.wozapp.fragment.MyOrderFragment;
import id.urbanwash.wozapp.fragment.ProfileFragment;
import id.urbanwash.wozapp.fragment.WalletFragment;
import id.urbanwash.wozapp.fragment.PriceFragment;
import id.urbanwash.wozapp.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.util.ImageManager;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class MainAdapter extends FragmentStatePagerAdapter implements SettingsListener {

    private AppCompatActivity mAppCompatActivity;

    private List<Fragment> mFragmentList;

    private HomeFragment mHomeFragment;
    private MyOrderFragment mMyOrderFragment;
    private PriceFragment mPriceFragment;
    private WalletFragment mWalletFragment;
    private AdminFragment mAdminFragment;
    private TransporterFragment mTransporterFragment;
    private ReportFragment mReportFragment;
    private SettingsFragment mSettingsFragment;
    private ProfileFragment mProfileFragment;
    private OrderSummaryAdminFragment mOrderSummaryAdminFragment;
    private OrderSummaryTransporterFragment mOrderSummaryTransporterFragment;

    private MainListener mainListener;

    CircleImageView profileImage;

    private int[] customerIcons = {
            R.drawable.icon_add_plus,
            R.drawable.icon_order,
            R.drawable.icon_pricing,
            R.drawable.icon_wallet,
            R.drawable.icon_setting,
            R.drawable.icon_user};

    private int[] adminIcons = {
            R.drawable.icon_order,
            R.drawable.icon_laundry,
            R.drawable.icon_chart,
            R.drawable.icon_pricing,
            R.drawable.icon_setting,
            R.drawable.icon_user};

    private int[] transporterIcons = {
            R.drawable.icon_order,
            R.drawable.icon_laundry,
            R.drawable.icon_pricing,
            R.drawable.icon_setting,
            R.drawable.icon_user};

    public MainAdapter(FragmentManager fm, AppCompatActivity aca, MainListener listener) {

        super(fm);

        mAppCompatActivity = aca;
        mainListener = listener;
        initFragmentListData(mAppCompatActivity);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public View getTabView(int position) {

        View view = LayoutInflater.from(mAppCompatActivity).inflate(R.layout.custom_tab_layout, null);
        AppCompatImageView tabImage = (AppCompatImageView) view.findViewById(R.id.image_tab);

        profileImage = (CircleImageView) view.findViewById(R.id.image_profile);
        profileImage.setImageResource(R.drawable.icon_user);

        int maxTabIndex = mFragmentList.size()-1;

        CustomerBean customerBean = Session.getCustomer();
        EmployeeBean employeeBean = Session.getEmployee();

        if (Session.isCustomer()) {
            tabImage.setImageResource(customerIcons[position]);

        } else if (Session.isAdminUser()) {
            tabImage.setImageResource(adminIcons[position]);

        } else if (Session.isTransporterUser()) {
            tabImage.setImageResource(transporterIcons[position]);
        }

        if (position < maxTabIndex) {
            profileImage.setVisibility(View.GONE);
            tabImage.setVisibility(View.VISIBLE);
            tabImage.setAlpha(0.8f);

        } else {

            tabImage.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            tabImage.setAlpha(1.0f);

            if (Session.isCustomer()) {

                ImageBean imageBean = customerBean.getImage();

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(profileImage, imageBean);

            } else {

                ImageBean imageBean = employeeBean.getImage();

                ImageManager imageManager = new ImageManager(mAppCompatActivity);
                imageManager.setImage(profileImage, imageBean);
            }
        }

        return view;
    }

    private void refreshProfileImage() {

        CustomerBean customerBean = Session.getCustomer();
        EmployeeBean employeeBean = Session.getEmployee();

        ImageBean imageBean = null;

        if (customerBean != null) {
            imageBean = customerBean.getImage();
        } else {
            imageBean = employeeBean.getImage();
        }

        ImageManager imageManager = new ImageManager(mAppCompatActivity);
        imageManager.setImage(profileImage, imageBean);
    }

    private void initFragmentListData(AppCompatActivity appCompatActivity) {

        mFragmentList = new ArrayList<>();

        if (Session.isCustomer()) {

            mHomeFragment = new HomeFragment(appCompatActivity);
            mMyOrderFragment = new MyOrderFragment(appCompatActivity);
            mPriceFragment = new PriceFragment(appCompatActivity);
            mWalletFragment = new WalletFragment(appCompatActivity);
            mSettingsFragment = new SettingsFragment(appCompatActivity, this);
            mProfileFragment = new ProfileFragment(appCompatActivity);

            mFragmentList.add(mHomeFragment);
            mFragmentList.add(mMyOrderFragment);
            mFragmentList.add(mPriceFragment);
            mFragmentList.add(mWalletFragment);
            mFragmentList.add(mSettingsFragment);
            mFragmentList.add(mProfileFragment);

        } else {

            mReportFragment = new ReportFragment(appCompatActivity);
            mPriceFragment = new PriceFragment(appCompatActivity);
            mSettingsFragment = new SettingsFragment(appCompatActivity, this);
            mProfileFragment = new ProfileFragment(appCompatActivity);

            if (Session.isAdminUser()) {

                mOrderSummaryAdminFragment = new OrderSummaryAdminFragment(appCompatActivity);
                mAdminFragment = new AdminFragment(appCompatActivity);

                mFragmentList.add(mOrderSummaryAdminFragment);
                mFragmentList.add(mAdminFragment);
                mFragmentList.add(mReportFragment);

            } else {
                mOrderSummaryTransporterFragment = new OrderSummaryTransporterFragment(appCompatActivity);
                mTransporterFragment = new TransporterFragment(appCompatActivity);

                mFragmentList.add(mOrderSummaryTransporterFragment);
                mFragmentList.add(mTransporterFragment);
            }

            mFragmentList.add(mPriceFragment);
            mFragmentList.add(mSettingsFragment);
            mFragmentList.add(mProfileFragment);
        }
    }

    @Override
    public void onLogoutSelected() {

        mainListener.onLogout();
    }

    @Override
    public void onProfileSelected() {
        mainListener.ShowProfileFromMainView();
    }

    public void refreshOutstandingOrders() {

        mMyOrderFragment.refreshOutstandingOrders();
    }

    public void refreshCompletedOrders() {

        mMyOrderFragment.refreshCompletedOrders();
    }

    public void refreshProfile() {

        mProfileFragment.refreshProfile();
        refreshProfileImage();
    }

    public void refreshPlaces() {

        mProfileFragment.refreshPlaces();
    }

    public void refreshOrderSummary() {

        if (mOrderSummaryAdminFragment != null) {
            mOrderSummaryAdminFragment.refreshSummary();
        } else {
            mOrderSummaryTransporterFragment.refreshSummary();
        }
    }

    public void refreshContent() {

        if (Session.isAdminUser()) {
            mAdminFragment.refreshContent();
        }
    }
}