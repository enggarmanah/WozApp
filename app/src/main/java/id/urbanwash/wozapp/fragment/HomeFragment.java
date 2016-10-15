package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Calendar;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.NewOrderActivity;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/18/16.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment{

    private View mRootView;

    private AppCompatActivity mAppCompatActivity;
    
    private RelativeLayout mMainPanel;
    private AppCompatImageView mNewOrderButton;

    private AppCompatTextView mGreetingsLabel;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public HomeFragment() {

    }

    public HomeFragment(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.content_start_order, container, false);

        mGreetingsLabel = (AppCompatTextView) mRootView.findViewById(R.id.label_title);

        mNewOrderButton = (AppCompatImageView) mRootView.findViewById(R.id.button_new_order);
        mMainPanel = (RelativeLayout) mRootView.findViewById(R.id.panel_main);

        mNewOrderButton.setOnClickListener(getNewOrderButtonOnClickListener());
        mMainPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        mRootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        initView();

        return mRootView;
    }

    private void initView() {

        String greetings = null;
        Calendar cal = CommonUtil.getCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour < 12) {
            greetings = getString(R.string.greeting_morning);
        } else if (hour < 18) {
            greetings = getString(R.string.greeting_afternoon);
        } else {
            greetings = getString(R.string.greeting_evening);
        }

        String user = null;

        if (Session.getCustomer() != null) {
            user = Session.getCustomer().getName().trim();
        } else {
            user = Session.getEmployee().getName().trim();
        }

        int i = user.indexOf(' ');

        if (i > -1) {
            user = user.substring(0, i);
        }

        mGreetingsLabel.setText(greetings + Constant.SPACE_STRING + user + "!");
    }

    private View.OnClickListener getNewOrderButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Session.setOrder(new OrderBean());

                Intent startOrderIntent = new Intent(mAppCompatActivity, NewOrderActivity.class);
                mAppCompatActivity.startActivity(startOrderIntent);
            }
        };
    }
}
