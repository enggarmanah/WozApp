package id.urbanwash.wozapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.Installation;
import id.urbanwash.wozapp.R;

import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/6/16.
 */
public class LoginActivity extends BaseActivity {

    AppCompatEditText mUserNameText;
    AppCompatEditText mPasswordText;
    AppCompatButton mLoginButton;
    AppCompatButton mRegisterButton;
    AppCompatTextView mResetPasswordButton;

    AppCompatActivity mAppCompatActivity;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppCompatActivity = this;

        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_TOPIC_PROMO);
        Session.setDeviceId(FirebaseInstanceId.getInstance().getToken());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mUserNameText = (AppCompatEditText) findViewById(R.id.text_user_name);
        mPasswordText = (AppCompatEditText) findViewById(R.id.text_password);
        mLoginButton = (AppCompatButton) findViewById(R.id.button_login);
        mRegisterButton = (AppCompatButton) findViewById(R.id.button_register);
        mResetPasswordButton = (AppCompatTextView) findViewById(R.id.button_reset_password);

        mLoginButton.setOnClickListener(getLoginBtnOnClickListener());
        mRegisterButton.setOnClickListener(getRegisterBtnOnClickListener());

        mResetPasswordButton.setOnClickListener(getResetPasswordOnClickListener());

        if (Session.isCustomer() || Session.isAdminUser() || Session.isTransporterUser()) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
        };
    }

    private View.OnClickListener getLoginBtnOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserNameText.getText().toString();
                String password = mPasswordText.getText().toString();

                if (CommonUtil.isEmpty(userName)) {

                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_username_required));
                    return;

                } else if (CommonUtil.isEmpty(password)) {
                    NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_password_required));
                    return;
                }

                showProgressDlg(getString(R.string.message_async_authenticate));

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                httpAsyncManager.authenticate(userName, password);
            }
        };
    }

    private View.OnClickListener getRegisterBtnOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        };
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        if (Session.isCustomer()) {
            Installation.saveCustomerProfile(this, Session.getCustomer());
        } else {
            Installation.saveEmployeeProfile(this, Session.getEmployee());
        }

        boolean isChangePassword = false;

        if (Session.isAdminUser() || Session.isTransporterUser()) {
            isChangePassword = Constant.STATUS_YES.equals(Session.getEmployee().getChangePassword());

        } else if (Session.isCustomer()) {
            isChangePassword = Constant.STATUS_YES.equals(Session.getCustomer().getChangePassword());
        }

        if (isChangePassword) {
            Intent intent = new Intent(getApplicationContext(), PasswordChangeActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        finish();
    }

    private View.OnClickListener getResetPasswordOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mAppCompatActivity, PasswordResetActivity.class);
                startActivity(intent);
            }
        };
    }
}
