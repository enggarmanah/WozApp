package id.urbanwash.wozapp.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.ContactDialogListener;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.util.ImageManager;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class ContactDialog extends AppCompatDialogFragment {

    private AppCompatActivity mAppCompatActivity;
    private View rootView;

    private CircleImageView mContactImage;
    private AppCompatTextView mNameLabel;

    private AppCompatButton mContactButton;
    private AppCompatButton mCancelButton;

    private ContactDialogListener mContactDialogListener;

    private String mName;
    private String mMobile;
    private String mType;

    private ImageBean mImage;

    public ContactDialog() {}

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mContactDialogListener = (ContactDialogListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = (int) (Utility.DisplayUtilty.getDisplayMetricFromWindow(mAppCompatActivity).widthPixels * 0.95);
        windowParams.height = ActionBar.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = Gravity.CENTER;
        window.setAttributes(windowParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.dialog_contact, container, false);

        mContactImage = (CircleImageView) rootView.findViewById(R.id.image_contact);
        mNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_name);

        mContactButton = (AppCompatButton) rootView.findViewById(R.id.btn_contact);
        mCancelButton = (AppCompatButton) rootView.findViewById(R.id.btn_cancel);

        refreshInfo();

        mContactButton.setOnClickListener(getOkButtonOnClickListener());
        mCancelButton.setOnClickListener(getCancelButtonOnClickListener());

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return rootView;
    }

    private void refreshInfo() {

        if (mName != null) {

            mNameLabel.setText(mName);

            ImageManager imageManager = new ImageManager(mAppCompatActivity);
            imageManager.setImage(mContactImage, mImage);

        } else {

            mNameLabel.setText(getString(R.string.app_name));
            mContactImage.setImageResource(R.drawable.icon_profile);
        }

        if (Constant.CONTACT_CALL.equals(mType)) {
            mContactButton.setText(getString(R.string.button_call));

        } else if (Constant.CONTACT_SMS.equals(mType)) {
            mContactButton.setText(getString(R.string.button_sms));
        }
    }

    public void setContact(String name, ImageBean image, String mobile, String type) {

        mName = name;
        mImage = image;
        mMobile = mobile;
        mType = type;

        mMobile = mMobile == null ? Constant.URBANWASH_CONTACT : mMobile;

        if (isAdded()) {
            refreshInfo();
        }
    }

    private View.OnClickListener getOkButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.CONTACT_CALL.equals(mType)) {
                    mContactDialogListener.onCall(mMobile);

                } else if (Constant.CONTACT_SMS.equals(mType)) {
                    mContactDialogListener.onSms(mMobile);
                }

                dismiss();
            }
        };
    }

    private View.OnClickListener getCancelButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        };
    }
}
