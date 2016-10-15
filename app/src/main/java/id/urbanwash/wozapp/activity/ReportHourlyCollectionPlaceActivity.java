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
import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.CustomInfoWindowAdapter;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CommonUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReportHourlyCollectionPlaceActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    private final static String TAG = ReportHourlyCollectionPlaceActivity.class.getSimpleName();

    private SupportMapFragment supportMapFragment;

    private GoogleMap mMap;

    private AppCompatImageView gpsButton;

    AppCompatActivity mAppCompatActivity;

    Date mCollectionDate;
    List<OrderBean> mOrders;

    LatLng mTransporterLatLng;
    LatLngBounds mBounds;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    public static final int ACTIVITY_PROCESS_ORDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_order_place);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackCloseColor());

        setNavigationBarTitle(getString(R.string.title_order_place));

        gpsButton = (AppCompatImageView) findViewById(R.id.button_gps);
        gpsButton.setOnClickListener(getGpsButtonOnClickListener());

        mCollectionDate = (Date) getIntent().getSerializableExtra(Constant.DATA_DATE);

        try {
            initializeMap();
        } catch (Exception e) {
            Log.d(ReportHourlyCollectionPlaceActivity.class.getSimpleName(), "Failed loading map " + e.getMessage());
        }

        mOrders = new ArrayList<OrderBean>();
    }

    public void onStart() {

        super.onStart();

        if (mOrders.isEmpty()) {
            getCollectionOrders();
        }
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

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == ACTIVITY_PROCESS_ORDER) {

                getCollectionOrders();
            }
        }
    }

    private void initializeMap() {

        this.supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_address);
        this.supportMapFragment.getMapAsync(this);
    }

    private Drawable changeBackCloseColor() {

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_cancel)).getBitmap();
        Drawable upArrow = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 36, 36, true));
        upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    private void setNavigationBarTitle(String text) {

        ((AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title)).setText(text);
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

        refreshMarkers();
    }

    private void getCollectionOrders() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);

        if (Session.isTransporterUser()) {
            httpAsyncManager.getCollectionOrders(Session.getEmployee(), mCollectionDate);
        } else {
            httpAsyncManager.getCollectionOrders(mCollectionDate);
        }
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orders) {

        hideProgressDlgNow();

        mOrders = orders;

        setLoading(false);

        refreshMarkers();
    }

    private void refreshMarkers() {

        if (mMap != null && mOrders != null) {

            mMap.clear();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(mAppCompatActivity);

            for (OrderBean orderBean : mOrders) {

                LatLng latLng = new LatLng(orderBean.getLat(), orderBean.getLng());

                Bitmap bitmap = null;

                if (Constant.ORDER_STATUS_NEW_ORDER.equals(orderBean.getStatus())) {

                    bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_pin_red, R.dimen.map_marker_width, R.dimen.map_marker_height);
                    customInfoWindowAdapter.putMapping(latLng, Constant.INFO_WINDOW_RED);

                } else if (Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(orderBean.getStatus()) ||
                        Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(orderBean.getStatus())) {

                    bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_pin_green, R.dimen.map_marker_width, R.dimen.map_marker_height);
                    customInfoWindowAdapter.putMapping(latLng, Constant.INFO_WINDOW_GREEN);

                } else {
                    bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_pin_blue, R.dimen.map_marker_width, R.dimen.map_marker_height);
                    customInfoWindowAdapter.putMapping(latLng, Constant.INFO_WINDOW_BLUE);
                }

                Marker marker = mMap.addMarker(
                        new MarkerOptions().position(latLng)
                                .title("# " + orderBean.getOrderNo())
                                .snippet(orderBean.getPlaceInfo())
                                .infoWindowAnchor(0, 0)
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                builder.include(latLng);
            }

            mMap.setInfoWindowAdapter(customInfoWindowAdapter);
            mMap.setOnInfoWindowClickListener(getOnInfoWindowClickListener());

            Bitmap bitmap = CommonUtil.createBitmap(mAppCompatActivity, R.drawable.icon_transporter_gps, R.dimen.map_marker_height, R.dimen.map_marker_height);

            if (mTransporterLatLng != null) {

                mMap.addMarker(new MarkerOptions().position(mTransporterLatLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                builder.include(mTransporterLatLng);
            }

            if (!mOrders.isEmpty()) {

                mBounds = builder.build();

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        setCameraOnCenter();
                    }
                });
            }
        }
    }

    private void setCameraOnCenter() {

        if (mMap != null && mBounds != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, Constant.MAP_PADDING));

            float zoom = mMap.getCameraPosition().zoom;

            if (zoom > Constant.MAP_MIN_ZOOM) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(Constant.MAP_MIN_ZOOM));
            }
        }
    }

    private GoogleMap.OnInfoWindowClickListener getOnInfoWindowClickListener() {

        return new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                try {
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

                OrderBean orderBean = getOrder(marker);
                Session.setOrder(orderBean);

                Intent intent = new Intent(mAppCompatActivity, ProcessOrderActivity.class);
                startActivityForResult(intent, ACTIVITY_PROCESS_ORDER);
            }
        };
    }

    private OrderBean getOrder(Marker marker) {

        for (OrderBean orderBean : mOrders) {

            LatLng latLng = marker.getPosition();

            if (orderBean.getLat() == latLng.latitude && orderBean.getLng() == latLng.longitude) {

                return orderBean;
            }
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        mTransporterLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        refreshMarkers();
    }

    private View.OnClickListener getGpsButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshMarkers();
            }
        };
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
