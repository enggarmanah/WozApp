package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.activity.ReportDailyActivity;
import id.urbanwash.wozapp.activity.ReportDailyCollectionActivity;
import id.urbanwash.wozapp.activity.ReportDailyDeliveryActivity;
import id.urbanwash.wozapp.activity.ReportMonthlyActivity;
import id.urbanwash.wozapp.activity.ReportOrderPlaceActivity;
import id.urbanwash.wozapp.activity.ReportProspectivePlaceActivity;
import id.urbanwash.wozapp.activity.ReportYearlyActivity;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.util.Utility;

@SuppressLint("ValidFragment")
public class ReportFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    private AppCompatTextView mDailyReportButton;
    private AppCompatTextView mMonthlyReportButton;
    private AppCompatTextView mYearlyReportButton;
    private AppCompatTextView mDailyCollectionReportButton;
    private AppCompatTextView mDailyDeliveryReportButton;
    private AppCompatTextView mOrderPlaceReportButton;
    private AppCompatTextView mProspectivePlaceReportButton;

    public ReportFragment() {}

    public ReportFragment(AppCompatActivity appCompatActivity) {

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

        mRootView = inflater.inflate(R.layout.content_report, container, false);

        mDailyReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_report);
        mMonthlyReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_monthly_report);
        mYearlyReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_yearly_report);
        mDailyCollectionReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_collection_report);
        mDailyDeliveryReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_delivery_report);
        mOrderPlaceReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_order_place_report);
        mProspectivePlaceReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_prospective_place_report);

        mDailyReportButton.setOnClickListener(getDailyReportButtonOnClickListener());
        mMonthlyReportButton.setOnClickListener(getMonthlyReportButtonOnClickListener());
        mYearlyReportButton.setOnClickListener(getYearlyReportButtonOnClickListener());
        mDailyCollectionReportButton.setOnClickListener(getDailyCollectionButtonOnClickListener());
        mDailyDeliveryReportButton.setOnClickListener(getDailyDeliveryButtonOnClickListener());
        mOrderPlaceReportButton.setOnClickListener(getOrderPlaceButtonOnClickListener());
        mProspectivePlaceReportButton.setOnClickListener(getProspectivePlaceButtonOnClickListener());

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return mRootView;
    }

    private View.OnClickListener getDailyReportButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportDailyActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getMonthlyReportButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportMonthlyActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getYearlyReportButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportYearlyActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getDailyCollectionButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportDailyCollectionActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getDailyDeliveryButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportDailyDeliveryActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getOrderPlaceButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportOrderPlaceActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getProspectivePlaceButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportProspectivePlaceActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }
}
