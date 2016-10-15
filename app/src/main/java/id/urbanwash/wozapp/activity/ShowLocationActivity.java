package id.urbanwash.wozapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final static String TAG = ShowLocationActivity.class.getSimpleName();

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    private SupportMapFragment supportMapFragment;

    private GoogleMap mMap;
    PlaceBean mPlaceBean;

    AppCompatImageView mGpsButton;

    private boolean mIsPlaceSelected;
    AppCompatActivity mActivity;

    OrderBean mOrderBean;

    LatLng mOrderLatLng;
    LatLng mTransporterLatLng;

    AppCompatTextView mPlaceNameLabel;
    AppCompatTextView mPlaceAddressLabel;
    AppCompatTextView mPlaceInfoLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        mActivity = this;

        mOrderBean = Session.getOrder();
        mOrderLatLng = new LatLng(mOrderBean.getLat(), mOrderBean.getLng());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar_show_location);
        getSupportActionBar().setHomeAsUpIndicator(changeBackCloseColor());

        mPlaceNameLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_place_name);
        mPlaceAddressLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_place_address);
        mPlaceInfoLabel = (AppCompatTextView) findViewById(R.id.label_place_info);

        if (!CommonUtil.isEmpty(mOrderBean.getPlaceName())) {
            mPlaceNameLabel.setText(mOrderBean.getPlaceName());
            mPlaceInfoLabel.setText(mOrderBean.getPlaceAddress());
            mPlaceAddressLabel.setVisibility(View.GONE);
        } else {
            mPlaceAddressLabel.setText(mOrderBean.getPlaceAddress());
            mPlaceNameLabel.setVisibility(View.GONE);
            mPlaceInfoLabel.setVisibility(View.GONE);
        }

        Intent intent = getIntent();

        if (intent != null) {

            mPlaceBean = (PlaceBean) getIntent().getSerializableExtra(Constant.DATA_ADDRESS);

            if (mPlaceBean != null && mPlaceBean.getName() != null) {
                mIsPlaceSelected = true;
            }
        }

        mGpsButton = (AppCompatImageView) findViewById(R.id.button_gps);

        try {
            initializeMap();
        } catch (Exception e) {
            Log.d(ShowLocationActivity.class.getSimpleName(), "Failed loading map " + e.getMessage());
        }

        mGpsButton.setOnClickListener(getGpsButtonOnClickListener());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            showCloseAlertDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeMap() {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_address);
        supportMapFragment.getMapAsync(this);
    }

    private Drawable changeBackCloseColor() {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_cancel)).getBitmap();
        Drawable upArrow = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 36, 36, true));
        upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    private void showCloseAlertDialog() {

        try {
            mLocationManager.removeUpdates(mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = this;

        try {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                mTransporterLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        refreshMap();
    }

    private View.OnClickListener getGpsButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mTransporterLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                    refreshMap();

                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void refreshMap() {

        if (mMap == null) {
            return;
        }

        List<Marker> markers = new ArrayList<Marker>();

        mMap.clear();

        Bitmap bitmap = CommonUtil.createBitmap(mActivity, R.drawable.icon_pin_green, R.dimen.map_marker_width, R.dimen.map_marker_height);

        Marker mOrderMarker = mMap.addMarker(
                new MarkerOptions().position(mOrderLatLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        markers.add(mOrderMarker);

        bitmap = CommonUtil.createBitmap(mActivity, R.drawable.icon_transporter_gps, R.dimen.map_marker_height, R.dimen.map_marker_height);

        if (mTransporterLatLng != null) {

            Marker transporterMarker = mMap.addMarker(new MarkerOptions().position(mTransporterLatLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            markers.add(transporterMarker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : markers) {
            builder.include(m.getPosition());
        }

        final int padding = 200; // offset from edges of the map in pixels
        final LatLngBounds bounds = builder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                float zoom = mMap.getCameraPosition().zoom;

                if (zoom > Constant.MAP_MIN_ZOOM) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(Constant.MAP_MIN_ZOOM));
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        mTransporterLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        refreshMap();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
