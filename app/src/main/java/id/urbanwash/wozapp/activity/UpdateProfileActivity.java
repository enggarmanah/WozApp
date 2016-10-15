package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import id.urbanwash.wozapp.Installation;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.listener.UpdateProfileListener;
import id.urbanwash.wozapp.listener.VerifyMobileListener;
import id.urbanwash.wozapp.fragment.RegisterStep2Fragment;
import id.urbanwash.wozapp.fragment.UpdateProfileFragment;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.ImageManager;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class UpdateProfileActivity extends BaseActivity implements UpdateProfileListener, VerifyMobileListener {

    private AppCompatImageView mNextButton;

    private AppCompatActivity mAppCompatActivity;

    private UpdateProfileFragment mUpdateProfileFragment;
    private RegisterStep2Fragment mRegisterStep2Fragment;

    Bitmap mProfileBitmap;

    private static final String PLACE_FRAGMENT = "PLACE_FRAGMENT";

    private static final int CONFIRM_SUBMIT = 1;
    private static final int CONFIRM_CANCEL = 2;

    private static final int ACK_UPDATE_SUCCESS = 1;

    public static final int ACTIVITY_GALLERY_INTENT_CALLED = 3;
    public static final int ACTIVITY_GALLERY_KITKAT_INTENT_CALLED = 4;

    private boolean mIsMobileVerified = false;
    private boolean mIsWaitForSms = false;

    private Timer mTimer;

    private int mStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mUpdateProfileFragment = new UpdateProfileFragment(mAppCompatActivity);
        mRegisterStep2Fragment = new RegisterStep2Fragment(mAppCompatActivity);

        replaceFragment(mUpdateProfileFragment, PLACE_FRAGMENT);

        mNextButton = (AppCompatImageView) findViewById(R.id.button_next);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_update_profile));

        mNextButton.setOnClickListener(getNextButtonOnClickListener());
    }

    private View.OnClickListener getNextButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mStep == 1) {

                    if (mUpdateProfileFragment.isValidated()) {

                        showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_update_profile));
                    }
                } else if (mStep == 2) {

                    if (!mIsMobileVerified && !mRegisterStep2Fragment.isValidated()) {
                        return;

                    } else {

                        mIsMobileVerified = true;
                        mIsWaitForSms = false;
                        mRegisterStep2Fragment.setRequestVerificationCode(false);

                        showProgressDlg(getString(R.string.message_async_processing));

                        if (Session.isCustomer()) {

                            CustomerBean customerBean = mUpdateProfileFragment.getCustomer();
                            CustomerBean curCustomerBean = Session.getCustomer();

                            curCustomerBean.setTitle(customerBean.getTitle());
                            curCustomerBean.setName(customerBean.getName());
                            curCustomerBean.setEmail(customerBean.getEmail());
                            curCustomerBean.setMobile(customerBean.getMobile());

                            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                            httpAsyncManager.updateCustomer(curCustomerBean);

                        } else {

                            EmployeeBean employeeBean = mUpdateProfileFragment.getEmployee();
                            EmployeeBean curEmployeeBean = Session.getEmployee();

                            curEmployeeBean.setName(employeeBean.getName());
                            curEmployeeBean.setEmail(employeeBean.getEmail());
                            curEmployeeBean.setMobile(employeeBean.getMobile());

                            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                            httpAsyncManager.updateEmployee(curEmployeeBean);
                        }
                    }
                }
            }
        };
    }

    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_SUBMIT) {

            if (Session.getCustomer() != null) {

                CustomerBean customerBean = mUpdateProfileFragment.getCustomer();
                CustomerBean curCustomerBean = Session.getCustomer();

                if (curCustomerBean.getMobile().equals(customerBean.getMobile())) {

                    curCustomerBean.setTitle(customerBean.getTitle());
                    curCustomerBean.setName(customerBean.getName());
                    curCustomerBean.setEmail(customerBean.getEmail());
                    curCustomerBean.setMobile(customerBean.getMobile());

                    if (customerBean.getImage() != null && customerBean.getImage().getBytes() != null) {

                        if (curCustomerBean.getImage() == null) {
                            curCustomerBean.setImage(customerBean.getImage());
                        } else {
                            curCustomerBean.getImage().setBytes(customerBean.getImage().getBytes());
                        }
                    }

                    showProgressDlg(getString(R.string.message_async_processing));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.updateCustomer(curCustomerBean);

                } else {

                    // send SMS
                    showProgressDlg(getString(R.string.message_async_sending_sms));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.verifyMobile(customerBean);
                }

            } else {

                EmployeeBean employeeBean = mUpdateProfileFragment.getEmployee();
                EmployeeBean curEmployeeBean = Session.getEmployee();

                if (curEmployeeBean.getMobile().equals(employeeBean.getMobile())) {

                    curEmployeeBean.setName(employeeBean.getName());
                    curEmployeeBean.setEmail(employeeBean.getEmail());
                    curEmployeeBean.setMobile(employeeBean.getMobile());

                    if (employeeBean.getImage() != null && employeeBean.getImage().getBytes() != null) {

                        if (curEmployeeBean.getImage() == null) {
                            curEmployeeBean.setImage(employeeBean.getImage());
                        } else {
                            curEmployeeBean.getImage().setBytes(employeeBean.getImage().getBytes());
                        }
                    }

                    showProgressDlg(getString(R.string.message_async_processing));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.updateEmployee(curEmployeeBean);

                } else {

                    // send SMS
                    showProgressDlg(getString(R.string.message_async_sending_sms));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.verifyMobile(employeeBean);
                }
            }
        } else if (confirmId == CONFIRM_CANCEL) {

            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                goToPrevSection();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        goToPrevSection();
    }

    private void goToPrevSection() {

        if (mStep == 1) {

            showConfirmDlg(CONFIRM_CANCEL, getString(R.string.confirm_cancel_update_profile));

        } else if (mStep == 2) {
            replaceFragment(mUpdateProfileFragment, PLACE_FRAGMENT);
            mStep--;
        }

    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlgNow();

        ImageManager imageManager = new ImageManager(mAppCompatActivity);

        if (Session.isCustomer()) {

            ImageBean imageBean = Session.getCustomer().getImage();

            if (imageBean != null) {

                ImageBean imgBean = new ImageBean();
                imgBean.setBean(imageBean);
                imgBean.setBytes(imageBean.getBytes());

                imageManager.saveImage(imgBean);

                //set image null thus it won't be part of request data
                imageBean.setBytes(null);
            }

            Installation.saveCustomerProfile(this, Session.getCustomer());

        } else {

            imageManager.saveImage(Session.getEmployee().getImage());

            ImageBean imageBean = Session.getEmployee().getImage();

            if (imageBean != null) {

                ImageBean imgBean = new ImageBean();
                imgBean.setBean(imageBean);
                imgBean.setBytes(imageBean.getBytes());

                imageManager.saveImage(imgBean);

                //set image null thus it won't be part of request data
                imageBean.setBytes(null);
            }

            Installation.saveEmployeeProfile(this, Session.getEmployee());
        }

        showAcknowlegementDlg(ACK_UPDATE_SUCCESS, getString(R.string.ack_profile_update_success));
    }

    @Override
    public void onAcknowledge(int id) {

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onSelectProfileImage() {

        if (Build.VERSION.SDK_INT < 19) {

            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), ACTIVITY_GALLERY_INTENT_CALLED);

        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, ACTIVITY_GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (null == data) return;

            Uri originalUri = null;

            if (requestCode == ACTIVITY_GALLERY_INTENT_CALLED) {
                originalUri = data.getData();

            } else if (requestCode == ACTIVITY_GALLERY_KITKAT_INTENT_CALLED) {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                //getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
            }

            try {

                byte[] byteArray = null;

                mProfileBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(originalUri));

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mProfileBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                mProfileBitmap = CommonUtil.getBitmap(byteArray);

                mUpdateProfileFragment.updateProfileImage(byteArray);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
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
        mIsWaitForSms = true;

        goToPrevSection();
    }

    @Override
    public void onAsyncVerifyMobile(CustomerBean customerBean) {

        hideProgressDlgNow();

        Session.getCustomer().setVerificationCode(customerBean.getVerificationCode());

        mRegisterStep2Fragment.setRequestVerificationCode(true);
        replaceFragment(mRegisterStep2Fragment, PLACE_FRAGMENT);
        mStep++;

        NotificationUtil.showInfoMessage(this, getString(R.string.message_please_wait_sms));
    }

    @Override
    public void onAsyncVerifyMobile(EmployeeBean employeeBean) {

        hideProgressDlgNow();

        Session.getEmployee().setVerificationCode(employeeBean.getVerificationCode());

        mRegisterStep2Fragment.setRequestVerificationCode(true);
        replaceFragment(mRegisterStep2Fragment, PLACE_FRAGMENT);
        mStep++;

        NotificationUtil.showInfoMessage(this, getString(R.string.message_please_wait_sms));
    }
}