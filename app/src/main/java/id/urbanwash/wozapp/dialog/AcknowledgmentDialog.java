package id.urbanwash.wozapp.dialog;

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

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.AcknowledgementDialogListener;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 4/11/16.
 */
public class AcknowledgmentDialog extends AppCompatDialogFragment {

    private AppCompatActivity mAppCompatActivity;
    private View rootView;

    private AppCompatTextView mMessageLabel;

    private AppCompatButton mOkButton;

    private AcknowledgementDialogListener mAcknowlegementDialogListener;

    private int mAckId;
    private String mMessage;

    public AcknowledgmentDialog() {}

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mAcknowlegementDialogListener = (AcknowledgementDialogListener) mAppCompatActivity;
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

        rootView = inflater.inflate(R.layout.dialog_acknowledgment, container, false);

        mMessageLabel = (AppCompatTextView) rootView.findViewById(R.id.label_message);

        mOkButton = (AppCompatButton) rootView.findViewById(R.id.btn_ok);

        refreshMessage();

        mOkButton.setOnClickListener(getOkButtonOnClickListener());

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return rootView;
    }

    private void refreshMessage() {

        mMessageLabel.setText(mMessage);
    }

    public void setAcknowledgement(int ackId, String message) {

        mAckId = ackId;
        mMessage = message;

        if (isAdded()) {
            refreshMessage();
        }
    }

    private View.OnClickListener getOkButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAcknowlegementDialogListener.onAcknowledge(mAckId);
                dismiss();
            }
        };
    }
}
