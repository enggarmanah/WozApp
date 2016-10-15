package id.urbanwash.wozapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

public class PasswordResetActivity extends BaseActivity {

    private AppCompatActivity mAppCompatActivity;

    private AppCompatEditText mEmailText;
    private AppCompatTextView mSubmitButton;

    private static final int ACK_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_reset_password));

        mEmailText = (AppCompatEditText) findViewById(R.id.text_email);

        mSubmitButton = (AppCompatTextView) findViewById(R.id.button_submit);
        mSubmitButton.setOnClickListener(getResetPasswordButtonOnClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();

        mEmailText.requestFocus();
        hideSoftKeyboard(mEmailText);
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

        String email = mEmailText.getText().toString();

        if (CommonUtil.isEmpty(email)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_email_required));
            isValidated = false;
        }

        return isValidated;
    }

    private void resetPassword() {

        String email = mEmailText.getText().toString();

        showProgressDlg(getString(R.string.message_async_processing));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.resetPassword(email);
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        showAcknowlegementDlg(ACK_SUCCESS, getString(R.string.ack_reset_password_success));
    }

    @Override
    public void onAcknowledge(int id) {

        if (id == ACK_SUCCESS) {

            finish();
        }
    }
}