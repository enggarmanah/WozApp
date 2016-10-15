package id.urbanwash.wozapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.adapter.CustomInfoWindowAdapter;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.CommonUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final static String TAG = SelectLocationActivity.class.getSimpleName();

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    private SupportMapFragment supportMapFragment;
    private Marker mMarker;
    private String addressText;

    boolean mIsGetLocationFromGps = true;

    private GoogleMap mMap;
    PlaceBean mPlaceBean;

    AppCompatImageView gpsButton;
    AppCompatImageView infoButton;

    private PlaceAutocompleteFragment mPlaceAutoCompleteFragment;

    private boolean mIsPlaceSelected;
    AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        mActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackCloseColor());

        setNavigationBarTitle(getString(R.string.title_my_location));

        Intent intent = getIntent();

        if (intent != null) {

            mPlaceBean = (PlaceBean) getIntent().getSerializableExtra(Constant.DATA_ADDRESS);

            if (mPlaceBean != null && mPlaceBean.getName() != null) {
                mIsPlaceSelected = true;
            }
        }

        gpsButton = (AppCompatImageView) findViewById(R.id.button_gps);
        infoButton = (AppCompatImageView) findViewById(R.id.button_info);

        try {
            initializeMap();
        } catch (Exception e) {
            Log.d(SelectLocationActivity.class.getSimpleName(), "Failed loading map " + e.getMessage());
        }

        gpsButton.setOnClickListener(getGpsButtonOnClickListener());
        infoButton.setOnClickListener(getInfoButtonOnClickListener());

        mPlaceAutoCompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment_place_auto_complete);

        AppCompatEditText placeText = (AppCompatEditText) mPlaceAutoCompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        placeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        placeText.setTextColor(ContextCompat.getColor(this, R.color.colorMediumGrey));
        placeText.setPadding(0, 0, 0, 0);
        Typeface typeFace = TypefaceUtils.load(getAssets(),"fonts/HelveticaLTStd-Light.otf");
        placeText.setTypeface(typeFace);

        LatLng topLeftLatLng = new LatLng(-6.342198095632309, 106.60528142005205);
        LatLng btmRightLatLng = new LatLng(-6.008504616657515, 107.01505489647388);

        LatLngBounds latLngBounds = new LatLngBounds(topLeftLatLng, btmRightLatLng);
        mPlaceAutoCompleteFragment.setBoundsBias(latLngBounds);

        mPlaceAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mIsPlaceSelected = true;
                mIsGetLocationFromGps = false;

                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Lat: " + place.getLatLng().latitude);
                Log.i(TAG, "Lng: " + place.getLatLng().longitude);

                mPlaceBean = new PlaceBean();
                mPlaceBean.setName(place.getName().toString());
                mPlaceBean.setAddress("");

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15f));

                mPlaceBean = new PlaceBean();
                mPlaceBean.setName(place.getName().toString());
                mPlaceBean.setAddress(getAddress(place.getLatLng()));
                mPlaceBean.setLat(place.getLatLng().latitude);
                mPlaceBean.setLng(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
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

        LatLng latLng = new LatLng(-6.1754256408522386, 106.82713463902473);

        if (mPlaceBean != null) {
            latLng = new LatLng(mPlaceBean.getLat(), mPlaceBean.getLng());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

        try {
            Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (loc != null) {
                latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            }

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);

        }catch (SecurityException e) {
            e.printStackTrace();
        }

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                LatLng latLng = cameraPosition.target;

                Log.i(TAG, "Lat: " + latLng.latitude);
                Log.i(TAG, "Lng: " + latLng.longitude);

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                mMap.clear();

                Bitmap bitmap = CommonUtil.createBitmap(mActivity, R.drawable.icon_pin_green, R.dimen.map_marker_width, R.dimen.map_marker_height);

                addressText = getAddress(latLng);

                mMarker = mMap.addMarker(
                        new MarkerOptions().position(latLng)
                                .title("Use this Location")
                                .snippet(addressText)
                                .infoWindowAnchor(0, 0)
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(mActivity));
                mMarker.showInfoWindow();

                /* String zone = "(-6.194540804505909,106.8229104951024) ; (-6.1931048663116535,106.81465331465006) ; (-6.20149844970059,106.812301017344) ; (-6.204456604372787,106.80072125047445) ; (-6.21037619698406,106.79617825895548) ; (-6.239954163005847,106.79612997919321) ; (-6.239304247583373,106.81763626635073) ; (-6.240774055158198,106.82050053030251) ; (-6.240425101216033,106.82553101330996) ; (-6.236757904522889,106.82722516357897) ; (-6.233808597214559,106.83099232614039) ; (-6.229845387329124,106.83311261236668) ; (-6.224022337513995,106.8473819643259) ; (-6.219184134272892,106.84496898204088) ; (-6.206461807157475,106.83833554387091) ; (-6.204085294376281,106.82804994285107) ; (-6.199767213228014,106.82821355760098)";
                String[] points = zone.split(";",-1);

                PolygonOptions polygonOptions = new PolygonOptions();

                List<LatLng> latLngs = new ArrayList<LatLng>();

                for (String point : points) {

                    point = point.trim();
                    point = point.replaceAll("\\(", Constant.EMPTY_STRING);
                    point = point.replaceAll("\\)", Constant.EMPTY_STRING);

                    String[] ltLg = point.split(",");

                    double lt = Double.parseDouble(ltLg[0]);
                    double lg = Double.parseDouble(ltLg[1]);

                    latLngs.add(new LatLng(lt, lg));
                }

                polygonOptions.addAll(latLngs);
                polygonOptions.strokeColor(getResources().getColor(R.color.colorMapBorder));
                polygonOptions.fillColor(getResources().getColor(R.color.colorMapCenter));

                mMap.addPolygon(polygonOptions);*/

                mMap.setOnInfoWindowClickListener(getOnInfoWindowClickListener());

                if (!mIsPlaceSelected) {

                    mPlaceBean = new PlaceBean();
                    mPlaceBean.setAddress(addressText);
                    mPlaceBean.setLat(latLng.latitude);
                    mPlaceBean.setLng(latLng.longitude);

                    try {
                        mPlaceAutoCompleteFragment.setText(Constant.EMPTY_STRING);
                    } catch (Exception e) {}

                } else {

                    mPlaceAutoCompleteFragment.setText(mPlaceBean.getName());
                    mIsPlaceSelected = false;
                }
            }
        });

        mMap.setOnInfoWindowClickListener(getOnInfoWindowClickListener());
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

                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constant.INTENT_DATA_RESULT, mPlaceBean);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        };
    }

    private GoogleMap.OnInfoWindowClickListener getInfoWindowOnClikcListener() {

        return new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.d(TAG, "Longitude " + mMarker.getPosition().latitude + " Longitude " + mMarker.getPosition().longitude);
                Intent addressIntent = new Intent();
                addressIntent.putExtra("addressText", mMarker.getSnippet());
                addressIntent.putExtra("addressLatitude", "" + mMarker.getPosition().latitude);
                addressIntent.putExtra("addressLongitude", "" + mMarker.getPosition().longitude);
                setResult(RESULT_OK, addressIntent);
                SelectLocationActivity.this.finish();
            }
        };
    }

    private View.OnClickListener getGpsButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    mIsGetLocationFromGps = true;

                    Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                    if (latLng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    }

                }catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener getInfoButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMarker != null) {
                    if (mMarker.isInfoWindowShown())
                        mMarker.hideInfoWindow();
                    else
                        mMarker.showInfoWindow();
                }
            }
        };
    }

    private String getAddress(LatLng latLng) {

        List<Address> addresses = null;
        String errorMessage = null;

        String addrStr = null;

        try {

            Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());

            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, get just a single address.
                    1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";

        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (CommonUtil.isEmpty(errorMessage)) {
                errorMessage = "no_address_found";
            }
        } else {

            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            addrStr = Constant.EMPTY_STRING;

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.

            int addrLine = 2 < address.getMaxAddressLineIndex() ? 2 : address.getMaxAddressLineIndex();

            for (int i = 0; i <= addrLine; i++) {

                addressFragments.add(address.getAddressLine(i));

                if (i == 0) {
                    addrStr = address.getAddressLine(i);

                    int index = addrStr.indexOf("No.");

                    if (index != -1) {
                        addrStr = addrStr.substring(0,index);
                        addrStr = addrStr.trim();
                    }

                } else {
                    addrStr += ", " + address.getAddressLine(i);
                }
            }
        }

        return addrStr;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mIsGetLocationFromGps) {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
