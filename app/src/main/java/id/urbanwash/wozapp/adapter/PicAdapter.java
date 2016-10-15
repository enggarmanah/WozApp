package id.urbanwash.wozapp.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.SelectPicListener;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.ImageManager;

/**
 * Created by apridosandyasa on 4/24/16.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.SelectPicViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private List<EmployeeBean> mPics;
    private SelectPicListener mListener;

    public PicAdapter(AppCompatActivity appCompatActivity, List<EmployeeBean> pics, SelectPicListener listener) {
        
        mAppCompatActivity = appCompatActivity;
        mPics = pics;
        mListener = listener;
    }

    @Override
    public SelectPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pic, parent, false);
        SelectPicViewHolder selectPicViewHolder = new SelectPicViewHolder(view);
        return selectPicViewHolder;
    }

    @Override
    public void onBindViewHolder(SelectPicViewHolder holder, int position) {

        EmployeeBean picBean = mPics.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(picBean));

        Drawable drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_user);

        holder.picImage.setImageDrawable(drawable);
        holder.nameLabel.setText(picBean.getName());
        holder.assignedOrderCountLabel.setText(CommonUtil.formatNumber(picBean.getAssignedOrderCount()));

        ImageManager imageManager = new ImageManager(mAppCompatActivity);
        imageManager.setImage(holder.picImage, picBean.getImage());
    }

    @Override
    public int getItemCount() {
        return mPics.size();
    }

    public static class SelectPicViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;
        
        private CircleImageView picImage;
        private AppCompatTextView nameLabel;
        private AppCompatTextView assignedOrderCountLabel;

        SelectPicViewHolder(View view) {
            
            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            picImage = (CircleImageView) view.findViewById(R.id.image_pic);
            nameLabel = (AppCompatTextView) view.findViewById(R.id.label_place_address);
            assignedOrderCountLabel = (AppCompatTextView) view.findViewById(R.id.label_assigned_order_count);
        }
    }

    private View.OnClickListener getMainPanelOnClickListener(final EmployeeBean picBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPicSelected(picBean);
            }
        };
    }
}