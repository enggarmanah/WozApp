package id.urbanwash.wozapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReportProspectivePlaceActivity extends BaseActivity implements OnMapReadyCallback {

    private final static String TAG = ReportProspectivePlaceActivity.class.getSimpleName();

    private SupportMapFragment supportMapFragment;

    private GoogleMap mMap;

    AppCompatActivity mAppCompatActivity;

    List<ReportBean> mReports;
    Map<ReportBean, Integer> mRadiusMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_prospective_place);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackCloseColor());

        setNavigationBarTitle(getString(R.string.title_prospective_place));

        try {
            initializeMap();
        } catch (Exception e) {
            Log.d(ReportProspectivePlaceActivity.class.getSimpleName(), "Failed loading map " + e.getMessage());
        }

        mReports = new ArrayList<ReportBean>();
    }

    public void onStart() {

        super.onStart();

        if (mReports.isEmpty()) {
            getProspectivePlaceReport();
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

        mMap.setOnMapClickListener(getMapOnClickListener());

        refreshMarkers();
    }

    private void getProspectivePlaceReport() {

        showProgressDlg(getString(R.string.message_async_loading));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.getProspectivePlaceReport();
    }

    @Override
    public void onAsyncGetReports(List<ReportBean> reports) {

        hideProgressDlgNow();

        mReports = reports;

        setLoading(false);

        refreshMarkers();
    }

    private void refreshMarkers() {

        if (mMap != null && mReports != null) {

            mRadiusMap = new HashMap<ReportBean, Integer>();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            float minValue = 0f;
            float maxValue = 0f;

            for (ReportBean reportBean : mReports) {

                minValue = minValue > reportBean.getValue() ? reportBean.getValue() : minValue;
                maxValue = maxValue < reportBean.getValue() ? reportBean.getValue() : maxValue;
            }

            float scale = (maxValue - minValue) / 5;

            for (ReportBean reportBean : mReports) {

                LatLng latLng = new LatLng(reportBean.getLat(), reportBean.getLng());

                int radius = ((int) ((reportBean.getValue() - minValue) / scale) + 1) * 25;

                mRadiusMap.put(reportBean, radius);

                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(radius)
                        .strokeColor(getResources().getColor(R.color.colorMapBorder))
                        .fillColor(getResources().getColor(R.color.colorMapCenter)));

                builder.include(latLng);
            }

            if (!mReports.isEmpty()) {

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
        }
    }

    private GoogleMap.OnMapClickListener getMapOnClickListener() {

        return new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if (mReports != null) {

                    for (ReportBean reportBean : mReports) {

                        double distance = CommonUtil.calculationByDistance(reportBean.getLat(), reportBean.getLng(), latLng.latitude, latLng.longitude);

                        int radius = mRadiusMap.get(reportBean);

                        if (distance <= radius) {

                            CommonUtil.setContext(mAppCompatActivity);
                            NotificationUtil.showInfoMessage(mAppCompatActivity, getString(R.string.map_info_prospective_place, CommonUtil.formatNumber(reportBean.getValue()), CommonUtil.getAddress(latLng)));
                            return;
                        }
                    }
                }
            }
        };
    }
}
