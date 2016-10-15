package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

public class PasswordChangeActivity extends BaseActivity {

    private AppCompatActivity mAppCompatActivity;

    private AppCompatEditText mOldPasswordText;
    private AppCompatEditText mNewPasswordText;
    private AppCompatEditText mNewPasswordRetypeText;

    private AppCompatTextView mSubmitButton;

    private static final int ACK_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_change_password));

        mOldPasswordText = (AppCompatEditText) findViewById(R.id.text_old_password);
        mNewPasswordText = (AppCompatEditText) findViewById(R.id.text_new_password);
        mNewPasswordRetypeText = (AppCompatEditText) findViewById(R.id.text_new_password_retype);

        mSubmitButton = (AppCompatTextView) findViewById(R.id.button_submit);
        mSubmitButton.setOnClickListener(getResetPasswordButtonOnClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();

        mOldPasswordText.requestFocus();
        hideSoftKeyboard(mOldPasswordText);
        hideSoftKeyboard(mNewPasswordText);
    }

    private View.OnClickListener getResetPasswordButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    resetPassword();
                }
            }
        };
    }

    private boolean validate() {

        boolean isValidated = true;

        String oldPassword = mOldPasswordText.getText().toString();
        String newPassword = mNewPasswordText.getText().toString();
        String newPasswordRetype = mNewPasswordRetypeText.getText().toString();

        if (CommonUtil.isEmpty(oldPassword)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_password_required));
            isValidated = false;

        } else if (CommonUtil.isEmpty(newPassword)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_new_password_required));
            isValidated = false;

        } else if (CommonUtil.isEmpty(newPasswordRetype)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_new_password_retype_required));
            isValidated = false;

        } else if (!newPassword.equals(newPasswordRetype)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_change_password_retype_does_not_match));
            isValidated = false;
        }

        return isValidated;
    }

    private void resetPassword() {

        String email = null;

        if (Session.isAdminUser() || Session.isTransporterUser()) {
            email = Session.getEmployee().getEmail();

        } else if (Session.isCustomer()) {
            email = Session.getCustomer().getEmail();
        }

        String password = mOldPasswordText.getText().toString();
        String newPassword = mNewPasswordText.getText().toString();

        showProgressDlg(getString(R.string.message_async_processing));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.changePassword(email, password, newPassword);
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        showAcknowlegementDlg(ACK_SUCCESS, getString(R.string.ack_change_password_success));
    }

    @Override
    public void onAcknowledge(int id) {

        if (id == ACK_SUCCESS) {

            boolean isChangePassword = false;

            if (Session.isAdminUser() || Session.isTransporterUser()) {
                isChangePassword = Constant.STATUS_YES.equals(Session.getEmployee().getChangePassword());

            } else if (Session.isCustomer()) {
                isChangePassword = Constant.STATUS_YES.equals(Session.getCustomer().getChangePassword());
            }

            if (isChangePassword) {

                Intent intent = new Intent(mAppCompatActivity, MainActivity.class);
                startActivity(intent);
            }

            finish();
        }
    }
}