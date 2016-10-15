package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.TopUpActivity;
import id.urbanwash.wozapp.activity.WalletHistoryActivity;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/30/16.
 */
@SuppressLint("ValidFragment")
public class WalletFragment extends Fragment {

    private View rootView;

    private AppCompatTextView mTotalCreditsLabel;
    private AppCompatImageView mWalletButton;
    private AppCompatTextView mTopUpButton;
    private AppCompatTextView mWalletHistoryButton;

    private AppCompatEditText mVoucherText;

    private AppCompatActivity mAppCompatActivity;

    public WalletFragment() {}

    public WalletFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_wallet, container, false);
        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        mTotalCreditsLabel = (AppCompatTextView) rootView.findViewById(R.id.label_total_credits);
        mWalletButton = (AppCompatImageView) rootView.findViewById(R.id.button_wallet);
        mTopUpButton = (AppCompatTextView) rootView.findViewById(R.id.button_top_up);
        mWalletHistoryButton = (AppCompatTextView) rootView.findViewById(R.id.button_wallet_history);
        mVoucherText = (AppCompatEditText) rootView.findViewById(R.id.text_voucher);

        mTopUpButton.setOnClickListener(getTopUpButtonOnClickListener());
        mWalletButton.setOnClickListener(getTopUpButtonOnClickListener());
        mWalletHistoryButton.setOnClickListener(getWalletHistoryOnClickListener());

        mTotalCreditsLabel.setText(CommonUtil.formatCurrency(Session.getCustomer().getTotalCredits()));

        return rootView;
    }

    private View.OnClickListener getTopUpButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, TopUpActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener getWalletHistoryOnClickListener() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, WalletHistoryActivity.class);
                startActivity(intent);
            }
        };
    }
}
