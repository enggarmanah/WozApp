package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.CustomSpinnerAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class NewCustomerActivity extends BaseActivity {

    private AppCompatSpinner mTitleSpinner;
    private AppCompatEditText mNameText;
    private AppCompatEditText mEmailText;
    private AppCompatEditText mMobileText;

    private AppCompatTextView mSaveButton;

    private AppCompatActivity mAppCompatActivity;

    private CustomerBean mCustomerBean;

    private String mNameStr;
    private String mTitleStr;
    private String mEmailStr;
    private String mMobileStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mTitleSpinner = (AppCompatSpinner) findViewById(R.id.spinner_salutation);
        mNameText = (AppCompatEditText) findViewById(R.id.text_name);
        mEmailText = (AppCompatEditText) findViewById(R.id.text_email);
        mMobileText = (AppCompatEditText) findViewById(R.id.text_mobile);

        mSaveButton = (AppCompatTextView) findViewById(R.id.button_save);

        CustomSpinnerAdapter salutationAdapter = new CustomSpinnerAdapter(mAppCompatActivity,
                mAppCompatActivity.getResources().getStringArray(R.array.title_name));
        mTitleSpinner.setAdapter(salutationAdapter);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_new_customer));

        mSaveButton.setOnClickListener(getSaveButtonOnClickListener());
    }

    private void saveInput() {

        mNameStr = mNameText.getText().toString();
        mEmailStr = mEmailText.getText().toString();
        mTitleStr = (String) mTitleSpinner.getSelectedItem();
        mMobileStr = mMobileText.getText().toString();
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
        }

        return isValid;
    }

    private View.OnClickListener getSaveButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidated()) {

                    mCustomerBean = new CustomerBean();

                    mCustomerBean.setTitle(mTitleStr);
                    mCustomerBean.setName(mNameStr);
                    mCustomerBean.setEmail(mEmailStr);
                    mCustomerBean.setMobile(mMobileStr);

                    showProgressDlg(getString(R.string.message_async_saving));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.registerCustomer(mCustomerBean, null);
                }
            }
        };
    }

    @Override
    public void onAsyncCompleted() {

        Intent intent = new Intent(this, ShowCustomerActivity.class);
        startActivity(intent);

        finish();
    }
}