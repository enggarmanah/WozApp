package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.CustomSpinnerAdapter;
import id.urbanwash.wozapp.listener.UpdateProfileListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.ImageManager;
import id.urbanwash.wozapp.util.NotificationUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/28/16.
 */
@SuppressLint("ValidFragment")
public class UpdateProfileFragment extends Fragment {

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    private UpdateProfileListener mListener;

    private CircleImageView mProfileImage;
    private AppCompatSpinner mTitleSpinner;
    private AppCompatEditText mNameText;
    private AppCompatEditText mEmailText;
    private AppCompatEditText mMobileText;

    private byte[] mImageBytes;

    private String mNameStr;
    private String mTitleStr;
    private String mEmailStr;
    private String mMobileStr;

    public UpdateProfileFragment() {
    }

    public UpdateProfileFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mListener = (UpdateProfileListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.content_update_profile, container, false);

        mProfileImage = (CircleImageView) mRootView.findViewById(R.id.image_profile);

        mTitleSpinner = (AppCompatSpinner) mRootView.findViewById(R.id.spinner_salutation);
        mNameText = (AppCompatEditText) mRootView.findViewById(R.id.text_name);
        mEmailText = (AppCompatEditText) mRootView.findViewById(R.id.text_email);
        mMobileText = (AppCompatEditText) mRootView.findViewById(R.id.text_mobile);

        CustomSpinnerAdapter salutationAdapter = new CustomSpinnerAdapter(mAppCompatActivity,
                mAppCompatActivity.getResources().getStringArray(R.array.title_name));
        mTitleSpinner.setAdapter(salutationAdapter);

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        initView();

        return mRootView;
    }


    private void initView() {

        if (Session.getCustomer() != null) {

            CustomerBean customerBean = Session.getCustomer();

            int titleIndex = 0;

            if (Constant.TITLE_MR.equals(customerBean.getTitle())) {
                titleIndex = 0;

            } else if (Constant.TITLE_MRS.equals(customerBean.getTitle())) {
                titleIndex = 1;

            } else if (Constant.TITLE_MS.equals(customerBean.getTitle())) {
                titleIndex = 2;
            }

            mTitleSpinner.setSelection(titleIndex);

            mNameText.setText(customerBean.getName());
            mEmailText.setText(customerBean.getEmail());
            mMobileText.setText(customerBean.getMobile());

            ImageManager imageManager = new ImageManager(mAppCompatActivity);
            imageManager.setImage(mProfileImage, customerBean.getImage());

            mProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSelectProfileImage();
                }
            });

        } else {

            EmployeeBean employeeBean = Session.getEmployee();

            mNameText.setText(employeeBean.getName());
            mEmailText.setText(employeeBean.getEmail());
            mMobileText.setText(employeeBean.getMobile());

            ImageManager imageManager = new ImageManager(mAppCompatActivity);
            imageManager.setImage(mProfileImage, employeeBean.getImage());

            mProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSelectProfileImage();
                }
            });
        }
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

    public CustomerBean getCustomer() {

        CustomerBean customerBean = new CustomerBean();
        customerBean.setName(mNameStr);
        customerBean.setEmail(mEmailStr);
        customerBean.setTitle(mTitleStr);
        customerBean.setMobile(mMobileStr);

        if (mImageBytes != null) {

            ImageBean imageBean = Session.getCustomer().getImage();

            if (imageBean == null) {
                imageBean = new ImageBean();
            }

            imageBean.setBytes(mImageBytes);
            customerBean.setImage(imageBean);
        }

        return customerBean;
    }

    public EmployeeBean getEmployee() {

        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setName(mNameStr);
        employeeBean.setEmail(mEmailStr);
        employeeBean.setMobile(mMobileStr);

        if (mImageBytes != null) {

            ImageBean imageBean = Session.getEmployee().getImage();

            if (imageBean == null) {
                imageBean = new ImageBean();
            }

            imageBean.setBytes(mImageBytes);
            employeeBean.setImage(imageBean);
        }

        return employeeBean;
    }

    public void updateProfileImage(byte[] bytes) {

        mImageBytes = bytes;

        Bitmap bMap = CommonUtil.getBitmap(bytes);
        mProfileImage.setImageBitmap(bMap);
    }
}
