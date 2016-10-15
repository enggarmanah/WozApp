package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.listener.VerifyMobileListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/28/16.
 */
@SuppressLint("ValidFragment")
public class RegisterStep2Fragment extends BaseFragment {

    private View rootView;

    private AppCompatActivity mAppCompatActivity;
    private VerifyMobileListener mListener;

    private AppCompatTextView mInfoLabel;

    private AppCompatEditText mVerificationCodeText;

    private LinearLayout mVerificationCodePanel;

    private AppCompatTextView mVerificationCodeText1;
    private AppCompatTextView mVerificationCodeText2;
    private AppCompatTextView mVerificationCodeText3;
    private AppCompatTextView mVerificationCodeText4;

    private AppCompatTextView mResendSmsButton;
    private AppCompatTextView mChangeMobileButton;

    private boolean mIsRequestVerificationCode = false;

    public RegisterStep2Fragment() {
    }

    public RegisterStep2Fragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mListener = (VerifyMobileListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_register_step_2, container, false);

        mInfoLabel = (AppCompatTextView) rootView.findViewById(R.id.label_info);

        mVerificationCodeText = (AppCompatEditText) rootView.findViewById(R.id.text_verification_code);

        mVerificationCodePanel = (LinearLayout) rootView.findViewById(R.id.panel_verification_code);

        mVerificationCodeText1 = (AppCompatTextView) rootView.findViewById(R.id.text_verification_code_1);
        mVerificationCodeText2 = (AppCompatTextView) rootView.findViewById(R.id.text_verification_code_2);
        mVerificationCodeText3 = (AppCompatTextView) rootView.findViewById(R.id.text_verification_code_3);
        mVerificationCodeText4 = (AppCompatTextView) rootView.findViewById(R.id.text_verification_code_4);

        mResendSmsButton = (AppCompatTextView) rootView.findViewById(R.id.button_resend_sms);
        mChangeMobileButton = (AppCompatTextView) rootView.findViewById(R.id.button_change_mobile);

        mVerificationCodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                mVerificationCodeText1.setText(Constant.EMPTY_STRING);
                mVerificationCodeText2.setText(Constant.EMPTY_STRING);
                mVerificationCodeText3.setText(Constant.EMPTY_STRING);
                mVerificationCodeText4.setText(Constant.EMPTY_STRING);

                if (!CommonUtil.isEmpty(input)) {

                    if (input.length() == 1) {
                        mVerificationCodeText1.setText(input.substring(0,1));

                    } else if (input.length() == 2) {
                        mVerificationCodeText1.setText(input.substring(0,1));
                        mVerificationCodeText2.setText(input.substring(1,2));

                    } else if (input.length() == 3) {
                        mVerificationCodeText1.setText(input.substring(0,1));
                        mVerificationCodeText2.setText(input.substring(1,2));
                        mVerificationCodeText3.setText(input.substring(2,3));

                    } else if (input.length() == 4) {
                        mVerificationCodeText1.setText(input.substring(0,1));
                        mVerificationCodeText2.setText(input.substring(1,2));
                        mVerificationCodeText3.setText(input.substring(2,3));
                        mVerificationCodeText4.setText(input.substring(3,4));

                        hideSoftKeyboard(mVerificationCodeText);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        View.OnClickListener verificationCodeOnClickListener = getVerificationCodeOnClickListener();
        mVerificationCodeText1.setOnClickListener(verificationCodeOnClickListener);
        mVerificationCodeText2.setOnClickListener(verificationCodeOnClickListener);
        mVerificationCodeText3.setOnClickListener(verificationCodeOnClickListener);
        mVerificationCodeText4.setOnClickListener(verificationCodeOnClickListener);

        mResendSmsButton.setOnClickListener(getResendSmsButtonOnClickListener());
        mChangeMobileButton.setOnClickListener(getChangeMobileButtonOnClickListener());

        if (mIsRequestVerificationCode) {
            mInfoLabel.setText(getString(R.string.verification_code_request));
            mVerificationCodeText1.setText(Constant.EMPTY_STRING);
        } else {
            mInfoLabel.setText(getString(R.string.verification_code_complete));
        }

        return rootView;
    }

    public void setRequestVerificationCode(boolean isRequestVerificationCode) {

        mIsRequestVerificationCode = isRequestVerificationCode;
    }

    public boolean isValidated() {

        boolean isValid = true;

        String verificationCode = mVerificationCodeText1.getText().toString() +
                mVerificationCodeText2.getText().toString() +
                mVerificationCodeText3.getText().toString() +
                mVerificationCodeText4.getText().toString();

        if (CommonUtil.isEmpty(verificationCode) || verificationCode.length() < 4) {

            isValid = false;
            mVerificationCodeText1.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_verification_code_required));

        } else {

            if (Session.getCustomer() != null) {

                CustomerBean customerBean = Session.getCustomer();

                if (!customerBean.getVerificationCode().equals(verificationCode)) {

                    isValid = false;
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_verification_code_invalid));
                }

            } else {
                EmployeeBean employeeBean = Session.getEmployee();

                if (!employeeBean.getVerificationCode().equals(verificationCode)) {

                    isValid = false;
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_verification_code_invalid));
                }
            }
        }

        return isValid;
    }

    private View.OnClickListener getResendSmsButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResendSms();
            }
        };
    }

    private View.OnClickListener getChangeMobileButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeMobileNumber();
            }
        };
    }

    private View.OnClickListener getVerificationCodeOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVerificationCodeText.requestFocus();
                showSoftKeyboard(mVerificationCodeText);
            }
        };
    }
}