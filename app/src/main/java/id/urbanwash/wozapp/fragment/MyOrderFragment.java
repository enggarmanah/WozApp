package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.activity.NewOrderActivity;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/18/16.
 */
@SuppressLint("ValidFragment")
public class MyOrderFragment extends Fragment {
    
    private View rootView;
    
    private AppCompatActivity mAppCompatActivity;
    private AppCompatTextView mNewOrderButton;
    private FragmentTabHost mFragmentTabHost;

    private MyOrderOutstandingFragment mMyOrderOutstandingFragment;
    private MyOrderCompletedFragment mMyOrderCompletedFragment;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public MyOrderFragment() {}

    public MyOrderFragment(AppCompatActivity aca) {

        mAppCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_my_order, container, false);

        mFragmentTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mNewOrderButton = (AppCompatTextView) rootView.findViewById(R.id.button_new_order);

        mFragmentTabHost.setup(mAppCompatActivity, getChildFragmentManager(), R.id.realtabcontent);

        mFragmentTabHost.addTab(
                setIndicator(mAppCompatActivity, mFragmentTabHost.newTabSpec(getString(R.string.my_order_in_progress)), R.drawable.custom_tab_widget_backgroung),
                MyOrderOutstandingFragment.class, null);
        mFragmentTabHost.addTab(
                setIndicator(mAppCompatActivity, mFragmentTabHost.newTabSpec(getString(R.string.my_order_completed)), R.drawable.custom_tab_widget_backgroung),
                MyOrderCompletedFragment.class, null);

        mNewOrderButton.setOnClickListener(new GoToStartOrder());

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment f : fragments) {

            if (f instanceof MyOrderOutstandingFragment) {
                mMyOrderOutstandingFragment = (MyOrderOutstandingFragment) f;

            } else if (f instanceof MyOrderCompletedFragment) {
                mMyOrderCompletedFragment = (MyOrderCompletedFragment) f;
            }
        }
    }

    public TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec, int resid) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(ctx).inflate(R.layout.custom_tab_widget, null);
        v.setBackgroundResource(resid);
        TextView text = (TextView) v.findViewById(R.id.label_title);
        text.setText(spec.getTag());
        return spec.setIndicator(v);
    }

    class GoToStartOrder implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Session.setOrder(new OrderBean());

            Intent startOrderIntent = new Intent(mAppCompatActivity, NewOrderActivity.class);
            startActivity(startOrderIntent);
        }
    }

    public void refreshOutstandingOrders() {

        if (mMyOrderOutstandingFragment != null) {
            mMyOrderOutstandingFragment.refreshOutstandingOrders();
        }
    }

    public void refreshCompletedOrders() {

        if (mMyOrderCompletedFragment != null) {
            mMyOrderCompletedFragment.refreshCompletedOrders();
        }
    }
}
