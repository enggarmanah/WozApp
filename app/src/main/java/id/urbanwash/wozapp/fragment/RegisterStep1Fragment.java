package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.adapter.CustomSpinnerAdapter;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/28/16.
 */
@SuppressLint("ValidFragment")
public class RegisterStep1Fragment extends Fragment {

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;

    private AppCompatSpinner mTitleSpinner;
    private AppCompatEditText mNameText;
    private AppCompatEditText mEmailText;
    private AppCompatEditText mMobileText;
    private AppCompatEditText mPasswordText;
    private AppCompatEditText mPasswordRetypeText;

    private String mNameStr;
    private String mTitleStr;
    private String mEmailStr;
    private String mMobileStr;
    private String mPasswordStr;
    private String mPasswordRetypeStr;

    public RegisterStep1Fragment() {}

    public RegisterStep1Fragment(AppCompatActivity aca) {
        mAppCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.content_register_step_1, container, false);

        mTitleSpinner = (AppCompatSpinner) mRootView.findViewById(R.id.spinner_salutation);
        mNameText = (AppCompatEditText) mRootView.findViewById(R.id.text_name);
        mEmailText = (AppCompatEditText) mRootView.findViewById(R.id.text_email);
        mMobileText = (AppCompatEditText) mRootView.findViewById(R.id.text_mobile);
        mPasswordText = (AppCompatEditText) mRootView.findViewById(R.id.text_password);
        mPasswordRetypeText = (AppCompatEditText) mRootView.findViewById(R.id.text_password_retype);

        CustomSpinnerAdapter salutationAdapter = new CustomSpinnerAdapter(mAppCompatActivity,
                mAppCompatActivity.getResources().getStringArray(R.array.title_name));
        mTitleSpinner.setAdapter(salutationAdapter);

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return mRootView;
    }

    private void saveInput() {

        mNameStr = mNameText.getText().toString();
        mEmailStr = mEmailText.getText().toString();
        mTitleStr = (String) mTitleSpinner.getSelectedItem();
        mMobileStr = mMobileText.getText().toString();
        mPasswordStr = mPasswordText.getText().toString();
        mPasswordRetypeStr = mPasswordRetypeText.getText().toString();
    }

    public boolean isValidated() {

        saveInput();

        boolean isValid = true;

        if (CommonUtil.isEmpty(mNameStr)) {

            isValid = false;
            mNameText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_name_required));

        } else if (CommonUtil.isEmpty(mEmailStr)) {

            isValid = false;
            mEmailText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_email_required));

        } else if (CommonUtil.isEmpty(mMobileStr)) {

            isValid = false;
            mMobileText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_mobile_required));

        } else if (CommonUtil.isEmpty(mPasswordStr)) {

            isValid = false;
            mPasswordText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_password_required));

        } else if (CommonUtil.isEmpty(mPasswordRetypeStr)) {

            isValid = false;
            mPasswordRetypeText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_password_retype_required));

        } else if (!mPasswordStr.equals(mPasswordRetypeStr)) {

            isValid = false;
            mPasswordRetypeText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_password_retype_does_not_match));
        }

        return isValid;
    }

    public CustomerBean getCustomer() {

        CustomerBean customerBean = new CustomerBean();
        customerBean.setName(mNameStr);
        customerBean.setEmail(mEmailStr);
        customerBean.setTitle(mTitleStr);
        customerBean.setMobile(mMobileStr);
        customerBean.setPassword(mPasswordStr);

        return customerBean;
    }

    public void requestFocusOnMobile() {

        mMobileText.requestFocus();
    }
}
