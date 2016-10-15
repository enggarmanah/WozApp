package id.urbanwash.wozapp.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.SearchCustomerListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.util.ImageManager;

/**
 * Created by apridosandyasa on 4/24/16.
 */
public class SearchCustomerAdapter extends RecyclerView.Adapter<SearchCustomerAdapter.CustomerViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private List<CustomerBean> mCustomers;
    private SearchCustomerListener mListener;

    public SearchCustomerAdapter(AppCompatActivity appCompatActivity, List<CustomerBean> customers, SearchCustomerListener listener) {
        
        mAppCompatActivity = appCompatActivity;
        mCustomers = customers;
        mListener = listener;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        CustomerViewHolder customerViewHolder = new CustomerViewHolder(view);
        return customerViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        CustomerBean picBean = mCustomers.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(picBean));

        Drawable drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_user);

        holder.imageProfile.setImageDrawable(drawable);

        holder.nameLabel.setText(picBean.getName());
        holder.mobileLabel.setText(picBean.getMobile());

        ImageManager imageManager = new ImageManager(mAppCompatActivity);
        imageManager.setImage(holder.imageProfile, picBean.getImage());
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mainPanel;

        private CircleImageView imageProfile;
        private AppCompatTextView nameLabel;
        private AppCompatTextView mobileLabel;

        CustomerViewHolder(View view) {
            
            super(view);

            mainPanel = (LinearLayout) view.findViewById(R.id.panel_main);

            imageProfile = (CircleImageView) view.findViewById(R.id.image_profile);
            nameLabel = (AppCompatTextView) view.findViewById(R.id.label_name);
            mobileLabel = (AppCompatTextView) view.findViewById(R.id.label_mobile);
        }
    }

    private View.OnClickListener getMainPanelOnClickListener(final CustomerBean customerBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onCustomerSelected(customerBean);
            }
        };
    }
}