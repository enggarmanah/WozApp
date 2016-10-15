package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.activity.SelectLocationActivity;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/28/16.
 */
@SuppressLint("ValidFragment")
public class RegisterStep3Fragment extends BaseFragment implements OnMapReadyCallback {

    private View rootView;

    private AppCompatActivity mAppCompatActivity;
    private AppCompatEditText mPlaceNameText;
    private AppCompatEditText mPlaceAddressText;

    private AppCompatTextView mPlaceAddressOkButton;

    private AppCompatImageView mHomeButton;
    private AppCompatImageView mApartmentButton;
    private AppCompatImageView mOfficeButton;

    private AppCompatTextView mHomeLabel;
    private AppCompatTextView mApartmentLabel;
    private AppCompatTextView mOfficeLabel;

    private GoogleMap mMap;
    private MapFragment mMapFragment;

    PlaceBean mPlaceBean;

    Double mLat;
    Double mLng;

    String mPlaceTypeStr;

    private static final int GET_LOCATION = 1;

    public RegisterStep3Fragment() {
    }

    public RegisterStep3Fragment(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_register_step_3, container, false);

        mPlaceNameText = (AppCompatEditText) rootView.findViewById(R.id.text_place);
        mPlaceAddressText = (AppCompatEditText) rootView.findViewById(R.id.text_address);

        mPlaceAddressOkButton = (AppCompatTextView) rootView.findViewById(R.id.button_address_ok);

        mHomeButton = (AppCompatImageView) rootView.findViewById(R.id.button_home);
        mApartmentButton = (AppCompatImageView) rootView.findViewById(R.id.button_apartment);
        mOfficeButton = (AppCompatImageView) rootView.findViewById(R.id.button_office);

        mHomeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_home);
        mApartmentLabel = (AppCompatTextView) rootView.findViewById(R.id.label_apartment);
        mOfficeLabel = (AppCompatTextView) rootView.findViewById(R.id.label_office);

        mPlaceAddressText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideSoftKeyboard(mPlaceAddressText);
                }

                return false;
            }
        });

        mPlaceAddressText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    mPlaceAddressOkButton.setVisibility(View.VISIBLE);
                }
            }
        });

        mPlaceAddressOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlaceAddressOkButton.setVisibility(View.GONE);
                hideSoftKeyboard(mPlaceAddressText);
            }
        });

        mHomeButton.setOnClickListener(getHomeButtonOnClickListener());
        mApartmentButton.setOnClickListener(getApartmentButtonOnClickListener());
        mOfficeButton.setOnClickListener(getOfficeButtonOnClickListener());

        try {
            initializeMap();
        } catch (Exception e) {
            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.error_failed_to_load_map));
        }

        mPlaceNameText.setOnClickListener(new ShowAddressView());

        showSelectedPlaceType();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mHomeButton.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_LOCATION) {

            if (resultCode == Activity.RESULT_OK) {

                mPlaceBean = (PlaceBean) data.getSerializableExtra(Constant.INTENT_DATA_RESULT);

                //mPlaceNameText.setText(mPlaceBean.getName());
                //mPlaceAddressText.setText(mPlaceBean.getAddress());

                String placeInfo = !CommonUtil.isEmpty(mPlaceBean.getName()) ? mPlaceBean.getName() + ", " + mPlaceBean.getAddress() : mPlaceBean.getAddress();
                mPlaceNameText.setText(placeInfo);

                mLat = mPlaceBean.getLat();
                mLng = mPlaceBean.getLng();

                LatLng latLng = new LatLng(mLat, mLng);

                mMap.clear();

                Bitmap bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_pin_green, R.dimen.map_marker_width, R.dimen.map_marker_height);

                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            }
        }
    }

    private void initializeMap() {

        mMapFragment = (MapFragment) getFragmentManager().findFragmentByTag("mapFragment");

        if (mMapFragment == null) {

            mMapFragment = new MapFragment();
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.panel_map, mMapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        mMapFragment.getMapAsync(this);
    }

    class ShowAddressView implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            selectLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng monas = new LatLng(-6.1754256408522386, 106.82713463902473);

        Bitmap bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_pin_green, R.dimen.map_marker_width, R.dimen.map_marker_height);

        mMap.addMarker(new MarkerOptions().position(monas).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monas, 10f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                selectLocation();
            }
        });
    }

    private void selectLocation() {

        Intent intent = new Intent(mAppCompatActivity, SelectLocationActivity.class);
        intent.putExtra(Constant.DATA_ADDRESS, mPlaceBean);

        startActivityForResult(intent, GET_LOCATION);
    }

    public PlaceBean getPlace() {

        String addrText = mPlaceAddressText.getText().toString();

        if (!CommonUtil.isEmpty(addrText)) {
            mPlaceBean.setAddress(mPlaceBean.getAddress() + ", " + addrText);
        }

        mPlaceBean.setType(mPlaceTypeStr);
        return mPlaceBean;
    }

    public boolean isValidated() {

        boolean isValidated = true;

        if (mPlaceBean == null) {

            isValidated = false;
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_address_required));

        } else if (mPlaceTypeStr == null) {

            isValidated = false;
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_place_type_required));
        }

        return isValidated;
    }

    private void resetPlaceTypeButton() {

        mHomeButton.setImageResource(R.drawable.icon_home_grey);
        mApartmentButton.setImageResource(R.drawable.icon_apartment_grey);
        mOfficeButton.setImageResource(R.drawable.icon_office_grey);

        mHomeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mApartmentLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));
        mOfficeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorGreyText));

        hideSoftKeyboard(mPlaceAddressText);
    }

    private void selectPlaceTypeHome() {

        resetPlaceTypeButton();
        mHomeButton.setImageResource(R.drawable.icon_home);
        mHomeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
        mPlaceTypeStr = Constant.PLACE_TYPE_HOME;
    }

    private void selectPlaceTypeApartment() {

        resetPlaceTypeButton();
        mApartmentButton.setImageResource(R.drawable.icon_apartment);
        mApartmentLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
        mPlaceTypeStr = Constant.PLACE_TYPE_APARTMENT;
    }

    private void selectPlaceTypeOffice() {

        resetPlaceTypeButton();
        mOfficeButton.setImageResource(R.drawable.icon_office);
        mOfficeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkLightGrey));
        mPlaceTypeStr = Constant.PLACE_TYPE_OFFICE;
    }

    private void showSelectedPlaceType() {

        if (Constant.PLACE_TYPE_HOME.equals(mPlaceTypeStr)) {
            selectPlaceTypeHome();

        } else if (Constant.PLACE_TYPE_APARTMENT.equals(mPlaceTypeStr)) {
            selectPlaceTypeApartment();

        } else if (Constant.PLACE_TYPE_OFFICE.equals(mPlaceTypeStr)) {
            selectPlaceTypeOffice();
        }
    }

    public View.OnClickListener getHomeButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPlaceTypeHome();
            }
        };
    }

    public View.OnClickListener getApartmentButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPlaceTypeApartment();
            }
        };
    }

    public View.OnClickListener getOfficeButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPlaceTypeOffice();
            }
        };
    }
}
