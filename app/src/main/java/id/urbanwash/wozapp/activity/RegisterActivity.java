package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import id.urbanwash.wozapp.Installation;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.VerifyMobileListener;
import id.urbanwash.wozapp.fragment.RegisterStep1Fragment;
import id.urbanwash.wozapp.fragment.RegisterStep3Fragment;
import id.urbanwash.wozapp.fragment.RegisterStep2Fragment;

import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.util.NotificationUtil;

public class RegisterActivity extends BaseActivity implements VerifyMobileListener {

    private int mRegisterStep = 0;

    private LinearLayout mStepsPanel;

    private AppCompatTextView mStep1Label;
    private AppCompatTextView mStep2Label;
    private AppCompatTextView mStep3Label;

    private AppCompatTextView mNextStepButton;
    private LinearLayout mBottomPanel;

    private AppCompatActivity mAppCompatActivity;

    private RegisterStep1Fragment mRegisterStep1Fragment;
    private RegisterStep2Fragment mRegisterStep2Fragment;
    private RegisterStep3Fragment mRegisterStep3Fragment;

    private static final String REGISTER_STEP_1_FRAGMENT = "REGISTER_STEP_1_FRAGMENT";
    private static final String REGISTER_STEP_2_FRAGMENT = "REGISTER_STEP_2_FRAGMENT";
    private static final String REGISTER_STEP_3_FRAGMENT = "REGISTER_STEP_3_FRAGMENT";

    private static final int CONFIRM_CANCEL_REGISTRATION = 1;

    private static final int ACK_PLACE_OUT_OF_SERVICE_AREA = 1;

    private boolean mIsMobileVerified = false;
    private boolean mIsWaitForSms = false;

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mRegisterStep1Fragment = new RegisterStep1Fragment(this);
        mRegisterStep2Fragment = new RegisterStep2Fragment(this);
        mRegisterStep3Fragment = new RegisterStep3Fragment(this);

        mStepsPanel = (LinearLayout) findViewById(R.id.panel_steps);
        mStep1Label = (AppCompatTextView) findViewById(R.id.label_step_1);
        mStep2Label = (AppCompatTextView) findViewById(R.id.label_step_2);
        mStep3Label = (AppCompatTextView) findViewById(R.id.label_step_3);
        mNextStepButton = (AppCompatTextView) findViewById(R.id.button_next);

        mBottomPanel = (LinearLayout) findViewById(R.id.panel_bottom);

        setActiveStepTitle(mRegisterStep);
        setActiveStepView(mRegisterStep);
        setNavigationBarTitle("CREATE ACCOUNT", mRegisterStep);

        mNextStepButton.setOnClickListener(getNextStepButtonOnClickListener());
    }

    @Override
    public void onBackPressed() {

        backToPrevStep();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            backToPrevStep();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 110) {
            setResult(RESULT_OK, data);
        }
    }

    private View.OnClickListener getNextStepButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRegisterStep == 0) {

                    if (mRegisterStep1Fragment.isValidated()) {
                        CustomerBean customerBean = mRegisterStep1Fragment.getCustomer();
                        Session.setCustomer(customerBean);
                        verifyEmail();
                        return;
                    } else {
                        return;
                    }

                } else if (mRegisterStep == 1) {

                    if (!mRegisterStep2Fragment.isAdded()) {
                        return;
                    }

                    if (!mIsMobileVerified && !mRegisterStep2Fragment.isValidated()) {
                        return;

                    } else {
                        mIsMobileVerified = true;
                        mIsWaitForSms = false;
                        mRegisterStep2Fragment.setRequestVerificationCode(false);
                    }

                } else if (mRegisterStep == 2) {

                    if (mRegisterStep3Fragment.isValidated()) {

                        showProgressDlg(getString(R.string.message_async_register));

                        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                        httpAsyncManager.registerCustomer(Session.getCustomer(), mRegisterStep3Fragment.getPlace());
                    }

                    return;
                }

                mRegisterStep++;

                setActiveStepTitle(mRegisterStep);
                setActiveStepView(mRegisterStep);
            }
        };
    }

    private void setActiveStepTitle(int step) {
        switch (step) {
            case 0:
                mStep1Label.setTextColor(ContextCompat.getColor(this, R.color.colorCustomGreen));
                mStep2Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                mStep3Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                break;

            case 1:
                mStep1Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                mStep2Label.setTextColor(ContextCompat.getColor(this, R.color.colorCustomGreen));
                mStep3Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                break;

            case 2:
                mStep1Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                mStep2Label.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
                mStep3Label.setTextColor(ContextCompat.getColor(this, R.color.colorCustomGreen));
                break;
        }
    }

    private void setActiveStepView(int step) {
        switch (step) {
            case 0:
                setNavigationBarTitle(getString(R.string.title_register_step_1), step);
                mStepsPanel.setVisibility(View.VISIBLE);
                replaceFragment(mRegisterStep1Fragment, REGISTER_STEP_1_FRAGMENT);
                mNextStepButton.setText(getString(R.string.button_next));
                break;

            case 1:
                if (!mIsMobileVerified) {
                    if (!mIsWaitForSms) {
                        sendSmsforVerification();
                    } else {
                        showVerifyMobileFragment();
                    }
                } else {
                    showVerifyMobileFragment();
                }
                break;

            case 2:
                setNavigationBarTitle(getString(R.string.title_register_step_3), step);
                mStepsPanel.setVisibility(View.GONE);
                replaceFragment(mRegisterStep3Fragment, REGISTER_STEP_3_FRAGMENT);
                mNextStepButton.setText(getString(R.string.button_confirm));
                mNextStepButton.startAnimation(setAnimationOnNext());
                break;
        }
    }

    private void backToPrevStep() {

        if (mRegisterStep > 0) {
            mRegisterStep--;

        } else {

            showConfirmDlg(CONFIRM_CANCEL_REGISTRATION, getString(R.string.confirm_cancel_registration));
        }

        setActiveStepTitle(mRegisterStep);
        setActiveStepView(mRegisterStep);
    }

    private Animation setAnimationOnNext() {

        Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(500);
        in.setInterpolator(new AccelerateDecelerateInterpolator());

        return in;
    }

    private void setNavigationBarTitle(String text, int registerStep) {

        ((AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title)).setText(text);

        if (registerStep == 2) {
            ((AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title)).startAnimation(setAnimationOnNext());
        }
    }

    @Override
    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_CANCEL_REGISTRATION) {

            finish();
        }
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        Installation.saveCustomerProfile(this, Session.getCustomer());

        if (Session.getPlaces().size() > 0) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            showAcknowlegementDlg(ACK_PLACE_OUT_OF_SERVICE_AREA, getString(R.string.alert_register_out_of_service_area_select_other));
        }
    }

    @Override
    public void onAcknowledge(int id) {

        if (id == ACK_PLACE_OUT_OF_SERVICE_AREA) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showVerifyMobileFragment() {

        setNavigationBarTitle(getString(R.string.title_register_step_2), 1);
        mStepsPanel.setVisibility(View.VISIBLE);
        replaceFragment(mRegisterStep2Fragment, REGISTER_STEP_2_FRAGMENT);
        mNextStepButton.setText(getString(R.string.button_next));
    }

    @Override
    public void onAsyncCheckEmail(CustomerBean customerBean) {

        hideProgressDlgNow();

        if (customerBean != null) {

            NotificationUtil.showErrorMessage(this, getString(R.string.error_register_customer_exist));
            return;
        }

        mRegisterStep++;

        setActiveStepTitle(mRegisterStep);
        setActiveStepView(mRegisterStep);
    }

    @Override
    public void onAsyncVerifyMobile(CustomerBean customerBean) {

        hideProgressDlgNow();

        Session.setCustomer(customerBean);

        showVerifyMobileFragment();

        NotificationUtil.showInfoMessage(this, getString(R.string.message_please_wait_sms));
    }

    @Override
    protected void onKeyboardVisible() {
        mBottomPanel.setVisibility(View.GONE);
    }

    @Override
    protected void onKeyboardGone() {
        mBottomPanel.setVisibility(View.VISIBLE);
    }

    private void verifyEmail() {

        showProgressDlg(getString(R.string.message_async_processing));

        CustomerBean customerBean = Session.getCustomer();

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
        httpAsyncManager.checkCustomerEmail(customerBean);
    }

    private void sendSmsforVerification() {

        mRegisterStep2Fragment.setRequestVerificationCode(true);

        if (!mIsWaitForSms) {

            mIsWaitForSms = true;

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsWaitForSms = false;
                }
            }, 5 * 60 * 1000);

            CustomerBean customerBean = Session.getCustomer();
            showProgressDlg(getString(R.string.message_async_sending_sms));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.verifyMobile(customerBean);

        } else {

            NotificationUtil.showAlertMessage(this, getString(R.string.message_please_wait_sms_first));
        }
    }

    @Override
    public void onResendSms() {

        if (!mIsMobileVerified) {
            sendSmsforVerification();

        } else {
            NotificationUtil.showAlertMessage(this, getString(R.string.verification_code_complete));
        }
    }

    @Override
    public void onChangeMobileNumber() {

        mIsMobileVerified = false;
        mIsWaitForSms = false;

        mRegisterStep = 0;
        setActiveStepTitle(mRegisterStep);
        setActiveStepView(mRegisterStep);

        mRegisterStep1Fragment.requestFocusOnMobile();
    }
}