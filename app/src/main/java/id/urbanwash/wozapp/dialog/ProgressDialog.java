package id.urbanwash.wozapp.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

public class ProgressDialog extends DialogFragment {
	
	private static final String PROGRESS_PERCENTAGE = "PROGRESS_PERCENTAGE";
	private static final String PROGRESS_MESSAGE = "PROGRESS_MESSAGE";

    AppCompatImageView mProgress1Image;
    AppCompatImageView mProgress2Image;
    AppCompatImageView mProgress3Image;
	
	AppCompatTextView mMessageLabel;

    Timer mTimer;
	
	String mMessage = Constant.EMPTY_STRING;
	int mProgress = 2;

    private AppCompatActivity mAppCompatActivity;
	
	public ProgressDialog() {}

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //set background not dimmed
		setStyle(STYLE_NO_TITLE, 0);
		
		if (savedInstanceState != null) {
			
			mProgress = savedInstanceState.getInt(PROGRESS_PERCENTAGE);
			mMessage = savedInstanceState.getString(PROGRESS_MESSAGE);
		}
		
		setCancelable(false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		
		outState.putInt(PROGRESS_PERCENTAGE, mProgress);
		outState.putString(PROGRESS_MESSAGE, mMessage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_progress_dlg, container, false);

        view.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        mProgress1Image = (AppCompatImageView) view.findViewById(R.id.image_progress_1);
        mProgress2Image = (AppCompatImageView) view.findViewById(R.id.image_progress_2);
        mProgress3Image = (AppCompatImageView) view.findViewById(R.id.image_progress_3);

        mMessageLabel = (AppCompatTextView) view.findViewById(R.id.label_message);

        mProgress = 2;

        return view;
	}
	
	@Override
	public void onStart() {

		super.onStart();

        if (!CommonUtil.isEmpty(mMessage)) {
            mMessageLabel.setText(mMessage);
        }

		if (getDialog() != null) {
			
			getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	
					if ((keyCode == KeyEvent.KEYCODE_BACK)) {
						
						return true;
			
					} else {
						return false;
					}
				}
			});
		}

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                mAppCompatActivity.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {

                        initProgressImage();

                        if (mProgress == 0) {
                            mProgress1Image.setImageResource(R.drawable.icon_next);
                        } else if (mProgress == 1) {
                            mProgress2Image.setImageResource(R.drawable.icon_next);
                        } else if (mProgress == 2) {
                            mProgress3Image.setImageResource(R.drawable.icon_next);
                        }
                    }
                });

                mProgress++;

                if (mProgress > 2) {
                    mProgress = 0;
                }

                System.out.println("Progress : " + mProgress);

                if (isDetached()) {

                    mTimer.cancel();
                    mTimer.purge();
                }

            }
        }, 0, 500);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.width = (int) (Utility.DisplayUtilty.getDisplayMetricFromWindow(mAppCompatActivity).widthPixels * 0.95);
        //windowParams.height = ActionBar.LayoutParams.WRAP_CONTENT;
        windowParams.gravity = Gravity.CENTER;
        window.setAttributes(windowParams);
    }
	
	public void setMessage(String message) {
		
		mMessage = message;
		
		if (mMessageLabel == null) {
			return;
		}
		
		mMessageLabel.setText(message);
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		
	    if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }

        super.onCancel(dialog);
	}

    private void initProgressImage() {

        mProgress1Image.setImageResource(R.drawable.icon_next_grey);
        mProgress2Image.setImageResource(R.drawable.icon_next_grey);
        mProgress3Image.setImageResource(R.drawable.icon_next_grey);
    }

    @Override
    public void dismissAllowingStateLoss() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }

        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}