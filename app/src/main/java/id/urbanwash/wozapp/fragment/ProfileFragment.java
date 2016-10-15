package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.ProfilePlaceAdapter;
import id.urbanwash.wozapp.listener.MainListener;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.ImageManager;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/29/16.
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements ProfilePlaceAdapter.ProfilePlaceAdapterListener {

    private View rootView;

    private CircleImageView mProfileImage;
    private AppCompatTextView mNameLabel;
    private AppCompatTextView mEmailLabel;
    private AppCompatTextView mMobileLabel;

    private AppCompatTextView mPlaceLabel;

    private RecyclerView mPlaceView;
    private LinearLayoutManager mPlaceLayoutManager;
    private ProfilePlaceAdapter mProfilePlaceAdapter;

    private LinearLayout mEditButton;

    private AppCompatActivity mAppCompatActivity;
    private MainListener mListener;

    private List<PlaceBean> mPlaces;

    public ProfileFragment() {
    }

    public ProfileFragment(AppCompatActivity appCompatActivity) {

        mAppCompatActivity = appCompatActivity;
        mListener = (MainListener) mAppCompatActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_profile, container, false);

        mProfileImage = (CircleImageView) rootView.findViewById(R.id.image_profile);
        mNameLabel = (AppCompatTextView) rootView.findViewById(R.id.label_name);
        mEmailLabel = (AppCompatTextView) rootView.findViewById(R.id.label_email);
        mMobileLabel = (AppCompatTextView) rootView.findViewById(R.id.label_mobile);
        mPlaceLabel = (AppCompatTextView) rootView.findViewById(R.id.label_place_address);

        mEditButton = (LinearLayout) rootView.findViewById(R.id.button_edit);
        mEditButton.setOnClickListener(getEditButtonOnClickListener());

        mPlaceView = (RecyclerView) rootView.findViewById(R.id.view_place);
        mPlaceLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mPlaceView.setHasFixedSize(true);
        mPlaceView.setLayoutManager(mPlaceLayoutManager);

        mPlaces = new ArrayList<PlaceBean>();
        mPlaces.addAll(Session.getPlaces());

        mProfilePlaceAdapter = new ProfilePlaceAdapter(mAppCompatActivity, mPlaces, this);
        mPlaceView.setAdapter(mProfilePlaceAdapter);

        rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(mAppCompatActivity));

        refreshProfile();

        return rootView;
    }

    public void refreshProfile() {

        if (Session.getCustomer() != null) {

            CustomerBean customerBean = Session.getCustomer();

            ImageManager imageManager = new ImageManager(mAppCompatActivity);
            imageManager.setImage(mProfileImage, customerBean.getImage());

            mNameLabel.setText(customerBean.getName());
            mEmailLabel.setText(customerBean.getEmail());
            mMobileLabel.setText(customerBean.getMobile());
            mPlaceLabel.setVisibility(View.VISIBLE);

        } else {

            EmployeeBean employeeBean = Session.getEmployee();

            ImageManager imageManager = new ImageManager(mAppCompatActivity);
            imageManager.setImage(mProfileImage, employeeBean.getImage());

            mNameLabel.setText(employeeBean.getName());
            mEmailLabel.setText(employeeBean.getEmail());
            mMobileLabel.setText(employeeBean.getMobile());
            mPlaceLabel.setVisibility(View.GONE);
            mPlaceView.setVisibility(View.GONE);
        }
    }

    public void refreshPlaces() {

        mPlaces.clear();
        mPlaces.addAll(Session.getPlaces());
        mProfilePlaceAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener getEditButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onUpdateProfile();
            }
        };
    }

    @Override
    public void onDeletePlace(PlaceBean placeBean) {

        mListener.onDeletePlace(placeBean);
    }
}
