package id.urbanwash.wozapp.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 4/11/16.
 */
@SuppressLint("ValidFragment")
public class NotificationDialog extends AppCompatDialogFragment {

    AppCompatImageView mInfoImage;
    AppCompatTextView mTitleLabel;
    AppCompatTextView mInfoLabel;
    
    private AppCompatActivity mAppCompatActivity;
    private View mRootView;

    private String mNotificationType;

    private String mInfoStr;

    public NotificationDialog() {
    }

    public NotificationDialog(AppCompatActivity appCompatActivity, String notificationType) {

        mAppCompatActivity = appCompatActivity;
        mNotificationType = notificationType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = (int) (Utility.DisplayUtilty.getDisplayMetricFromWindow(mAppCompatActivity).widthPixels * 0.95);
        windowParams.gravity = Gravity.CENTER;
        window.setAttributes(windowParams);

        mInfoLabel.setText(mInfoStr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.dialog_notification, container, false);

        mInfoImage = (AppCompatImageView) mRootView.findViewById(R.id.image_notification_type);
        mTitleLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_title);
        mInfoLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_message);

        if (Constant.NOTIFICATION_TYPE_INFO.equals(mNotificationType)) {
            mInfoImage.setImageResource(R.drawable.icon_dlg_info);
            mTitleLabel.setText(R.string.notification_type_info);
            mTitleLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorNotificationInfo));

        } else if (Constant.NOTIFICATION_TYPE_ALERT.equals(mNotificationType)) {
            mInfoImage.setImageResource(R.drawable.icon_dlg_alert);
            mTitleLabel.setText(R.string.notification_type_alert);
            mTitleLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorNotificationAlert));

        } else if (Constant.NOTIFICATION_TYPE_ERROR.equals(mNotificationType)) {
            mInfoImage.setImageResource(R.drawable.icon_dlg_error);
            mTitleLabel.setText(R.string.notification_type_error);
            mTitleLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorNotificationError));
        }

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return mRootView;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dialog.dismiss();
    }

    public void setInfo(String info) {

        mInfoStr = info;
    }
}
