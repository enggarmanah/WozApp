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

import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 3/29/16.
 */
public class ProfilePlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;

    List<PlaceBean> mPlaceBeans;

    private ProfilePlaceAdapterListener mListener;

    public interface ProfilePlaceAdapterListener {

        void onDeletePlace(PlaceBean placeBean);
    }

    public ProfilePlaceAdapter(AppCompatActivity appCompatActivity, List<PlaceBean> placeBeans, ProfilePlaceAdapterListener listener) {

        mAppCompatActivity = appCompatActivity;
        mPlaceBeans = placeBeans;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_place, parent, false);
        ProfilePlaceHolder profilePlaceHolder = new ProfilePlaceHolder(view);
        return profilePlaceHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProfilePlaceHolder profilePlaceHolder = (ProfilePlaceHolder) holder;
        setupProfilePlaceHolder(profilePlaceHolder, position);
    }

    @Override
    public int getItemCount() {

        return mPlaceBeans != null ? mPlaceBeans.size() : 0;
    }

    private void setupProfilePlaceHolder(ProfilePlaceHolder holder, int position) {

        final PlaceBean placeBean = mPlaceBeans.get(position);

        Drawable drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        if (Constant.PLACE_TYPE_HOME.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_home);

        } else if (Constant.PLACE_TYPE_APARTMENT.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_apartment);

        } else if (Constant.PLACE_TYPE_OFFICE.equals(placeBean.getType())) {
            drawable = ContextCompat.getDrawable(mAppCompatActivity, R.drawable.icon_office);
        }

        if (!CommonUtil.isEmpty(placeBean.getName())) {
            holder.placeNameLabel.setVisibility(View.VISIBLE);
            holder.placeNameLabel.setText(placeBean.getName());
        } else {
            holder.placeNameLabel.setVisibility(View.GONE);
        }

        holder.placeTypeImage.setImageDrawable(drawable);
        holder.placeInfoLabel.setText(placeBean.getAddress());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onDeletePlace(placeBean);
            }
        });
    }

    static class ProfilePlaceHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView placeTypeImage;

        private AppCompatTextView placeNameLabel;
        private AppCompatTextView placeInfoLabel;

        //private AppCompatImageView editButton;
        private AppCompatImageView deleteButton;

        ProfilePlaceHolder(View view) {
            super(view);

            placeTypeImage = (AppCompatImageView) view.findViewById(R.id.image_place_type);
            placeNameLabel = (AppCompatTextView) view.findViewById(R.id.label_place_name);
            placeInfoLabel = (AppCompatTextView) view.findViewById(R.id.label_place_address);
            //editButton = (AppCompatImageView) view.findViewById(R.id.button_edit);
            deleteButton = (AppCompatImageView) view.findViewById(R.id.button_delete);
        }
    }
}
