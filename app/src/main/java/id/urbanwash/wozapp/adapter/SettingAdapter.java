package id.urbanwash.wozapp.adapter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.SettingListener;

/**
 * Created by apridosandyasa on 3/28/16.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    private AppCompatActivity appCompatActivity;
    private String[] settingItems;
    private SettingListener settingListener;

    public SettingAdapter(AppCompatActivity aca, String[] objects, SettingListener listener) {
        appCompatActivity = aca;
        settingItems = objects;
        settingListener = listener;
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_setting, parent, false);
        SettingViewHolder svh = new SettingViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {

        holder.optionLabel.setText(settingItems[position]);

        if (position < 7) {

            holder.optionLabel.setTextColor(Color.parseColor("#444749"));

            switch (position) {
                case 0:
                    holder.optionPanel.setOnClickListener(new ShowProfile());
                    break;

                case 1:
                    holder.optionPanel.setOnClickListener(new ShowChangePassword());
                    break;

                case 2:
                    holder.optionPanel.setOnClickListener(new ShowPromo());
                    break;

                case 3:
                    holder.optionPanel.setOnClickListener(new ShowTermsOfService());
                    break;

                case 4:
                    holder.optionPanel.setOnClickListener(new ShowPrivacyPolicy());
                    break;

                case 5:
                    holder.optionPanel.setOnClickListener(new ShowAboutUs());
                    break;

                case 6:
                    holder.optionPanel.setOnClickListener(new ShowFAQ());
                    break;
            }
        }else {
            holder.optionLabel.setTextColor(Color.parseColor("#03a994"));
            holder.optionPanel.setOnClickListener(new LogOut());
        }
    }

    @Override
    public int getItemCount() {
        return settingItems.length;
    }

    class LogOut implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.LogoutFromAccount();
        }
    }

    class ShowProfile implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowProfileFromSetting();
        }
    }

    class ShowChangePassword implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowChangePasswordFromSetting();
        }
    }

    class ShowPromo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowPromoFromSetting();
        }
    }

    class ShowTermsOfService implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowTermsOfServiceFromSetting();
        }
    }

    class ShowPrivacyPolicy implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowPrivacyPolicyFromSetting();
        }
    }

    class ShowAboutUs implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowAbousUsFromSetting();
        }
    }

    class ShowFAQ implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            settingListener.ShowFAQViewFromSetting();
        }
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        
        private RelativeLayout optionPanel;
        private AppCompatTextView optionLabel;
        private AppCompatImageView optionImage;

        SettingViewHolder(View view) {
            super(view);
            optionPanel = (RelativeLayout) view.findViewById(R.id.panel_option);
            optionLabel = (AppCompatTextView) view.findViewById(R.id.label_option);
            optionImage = (AppCompatImageView) view.findViewById(R.id.image_arrow);
        }
    }
}
