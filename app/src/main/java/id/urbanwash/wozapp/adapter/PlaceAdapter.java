package id.urbanwash.wozapp.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.SelectPlaceListener;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 4/24/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.SelectPlaceViewHolder> {
    
    private AppCompatActivity mAppCompatActivity;
    private List<PlaceBean> mPlaces;
    private SelectPlaceListener mListener;

    public PlaceAdapter(AppCompatActivity appCompatActivity, List<PlaceBean> places, SelectPlaceListener listener) {
        
        mAppCompatActivity = appCompatActivity;
        mPlaces = places;
        mListener = listener;
    }

    @Override
    public SelectPlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
        SelectPlaceViewHolder selectPlaceViewHolder = new SelectPlaceViewHolder(view);
        return selectPlaceViewHolder;
    }

    @Override
    public void onBindViewHolder(SelectPlaceViewHolder holder, int position) {

        PlaceBean placeBean = mPlaces.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(placeBean));

        Drawable drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        if (Constant.PLACE_TYPE_HOME.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        } else if (Constant.PLACE_TYPE_APARTMENT.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_apartment);

        } else if (Constant.PLACE_TYPE_OFFICE.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_office);
        }

        holder.placeImage.setImageDrawable(drawable);

        if (!CommonUtil.isEmpty(placeBean.getName())) {
            holder.nameLabel.setText(placeBean.getName());
            holder.nameLabel.setVisibility(View.VISIBLE);
        } else {
            holder.nameLabel.setVisibility(View.GONE);
        }

        holder.addressLabel.setText(placeBean.getAddress());
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public static class SelectPlaceViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;
        
        private AppCompatImageView placeImage;
        private AppCompatTextView nameLabel;
        private AppCompatTextView addressLabel;

        SelectPlaceViewHolder(View view) {
            
            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            placeImage = (AppCompatImageView) view.findViewById(R.id.image_place);
            nameLabel = (AppCompatTextView) view.findViewById(R.id.label_place_name);
            addressLabel = (AppCompatTextView) view.findViewById(R.id.label_place_address);
        }
    }

    private View.OnClickListener getMainPanelOnClickListener(final PlaceBean placeBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPlaceSelected(placeBean);
            }
        };
    }
}
