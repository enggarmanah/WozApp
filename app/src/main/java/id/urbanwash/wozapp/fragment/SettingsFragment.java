package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.activity.PasswordChangeActivity;
import id.urbanwash.wozapp.adapter.SettingAdapter;
import id.urbanwash.wozapp.listener.SettingListener;
import id.urbanwash.wozapp.listener.SettingsListener;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/28/16.
 */
@SuppressLint("ValidFragment")
public class SettingsFragment extends Fragment implements SettingListener {
    
    private View rootView;
    private AppCompatActivity mAppCompatActivity;
    
    private RecyclerView mSettingsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SettingAdapter mSettingsAdapter;
    private SettingsListener mSettingsListener;

    public SettingsFragment() {}

    public SettingsFragment(AppCompatActivity appCompatActivity, SettingsListener listener) {
        mAppCompatActivity = appCompatActivity;
        mSettingsListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_setting, container, false);

        mSettingsRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_setting);
        mLinearLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mSettingsRecyclerView.setHasFixedSize(true);
        mSettingsRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSettingsAdapter = new SettingAdapter(mAppCompatActivity, mAppCompatActivity.getResources().getStringArray(R.array.setting_content_items), this);
        mSettingsRecyclerView.setAdapter(mSettingsAdapter);

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        return rootView;
    }

    @Override
    public void LogoutFromAccount() {

        mSettingsListener.onLogoutSelected();
    }

    @Override
    public void ShowProfileFromSetting() {

        mSettingsListener.onProfileSelected();
    }

    @Override
    public void ShowFAQViewFromSetting() {
    }

    @Override
    public void ShowAbousUsFromSetting() {
    }

    @Override
    public void ShowPrivacyPolicyFromSetting() {
    }

    @Override
    public void ShowTermsOfServiceFromSetting() {
    }

    @Override
    public void ShowPromoFromSetting() {
    }

    @Override
    public void ShowChangePasswordFromSetting() {

        Intent intent = new Intent(mAppCompatActivity, PasswordChangeActivity.class);
        startActivity(intent);
    }
}
