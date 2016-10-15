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
import id.urbanwash.wozapp.activity.ReportTransporterOrderPlaceActivity;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.util.Utility;

@SuppressLint("ValidFragment")
public class TransporterFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mMainListener;

    private AppCompatTextView mDailyReportButton;
    private AppCompatTextView mDailyCollectionReportButton;
    private AppCompatTextView mDailyDeliveryReportButton;
    private AppCompatTextView mAssignedOrderButton;

    public TransporterFragment() {}

    public TransporterFragment(AppCompatActivity appCompatActivity) {

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

        mRootView = inflater.inflate(R.layout.content_transporter, container, false);

        mDailyReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_report);
        mDailyCollectionReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_collection_report);
        mDailyDeliveryReportButton = (AppCompatTextView) mRootView.findViewById(R.id.button_daily_delivery_report);
        mAssignedOrderButton = (AppCompatTextView) mRootView.findViewById(R.id.button_assigned_order);

        mDailyReportButton.setOnClickListener(getDailyReportButtonOnClickListener());
        mDailyCollectionReportButton.setOnClickListener(getDailyCollectionReportButtonOnClickListener());
        mDailyDeliveryReportButton.setOnClickListener(getDailyDeliveryReportButtonOnClickListener());
        mAssignedOrderButton.setOnClickListener(getAssignedOrderButtonOnClickListener());

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return mRootView;
    }

    private View.OnClickListener getDailyCollectionReportButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportDailyCollectionActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }

    private View.OnClickListener getDailyDeliveryReportButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportDailyDeliveryActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
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

    private View.OnClickListener getAssignedOrderButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, ReportTransporterOrderPlaceActivity.class);
                mAppCompatActivity.startActivity(intent);
            }
        };
    }
}
