package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.util.Utility;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private AppCompatActivity appCompatActivity;

    private View contentGreenView;
    private View contentRedView;
    private View contentBlueView;

    private View contentView;

    private RelativeLayout titlePanel;
    private LinearLayout contentPanel;
    private AppCompatTextView titleLabel;
    private AppCompatTextView contentLabel;

    private Map<String, String> windowInfoMap;

    public CustomInfoWindowAdapter(AppCompatActivity aca) {
        
        appCompatActivity = aca;

        windowInfoMap = new HashMap<String, String>();

        contentGreenView = LayoutInflater.from(appCompatActivity).inflate(R.layout.custom_info_window_green, null);
        contentRedView = LayoutInflater.from(appCompatActivity).inflate(R.layout.custom_info_window_red, null);
        contentBlueView = LayoutInflater.from(appCompatActivity).inflate(R.layout.custom_info_window_blue, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        LatLng pos = marker.getPosition();
        String latLng = pos.latitude + "," + pos.longitude;

        String windowType = windowInfoMap.get(latLng);

        if (Constant.INFO_WINDOW_GREEN.equals(windowType)) {
            contentView = contentGreenView;

        } else if (Constant.INFO_WINDOW_RED.equals(windowType)) {
            contentView = contentRedView;

        } else if (Constant.INFO_WINDOW_BLUE.equals(windowType)) {
            contentView = contentBlueView;

        } else {
            contentView = contentGreenView;
        }

        titlePanel = (RelativeLayout) contentView.findViewById(R.id.panel_title);
        contentPanel = (LinearLayout) contentView.findViewById(R.id.panel_content);

        titleLabel = (AppCompatTextView) contentView.findViewById(R.id.label_title);
        contentLabel = (AppCompatTextView) contentView.findViewById(R.id.label_content);

        titleLabel.setText(marker.getTitle());
        contentLabel.setText(marker.getSnippet());

        setRelativeLayoutWidthHeight(titlePanel);
        setLinearLayoutWidthHeight(contentPanel);

        return contentView;
    }

    public void putMapping(LatLng latLng, String windowType) {

        String latLngStr = latLng.latitude + "," + latLng.longitude;
        windowInfoMap.put(latLngStr, windowType);
    }

    private void setRelativeLayoutWidthHeight(RelativeLayout relativeLayout) {

        DisplayMetrics dm = Utility.DisplayUtilty.getDisplayMetricFromWindow(appCompatActivity);
        int w = (int) (dm.widthPixels * 0.50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, dm));
        layoutParams.setMargins(120, 0, 0, 0);
        relativeLayout.setLayoutParams(layoutParams);
    }

    private void setLinearLayoutWidthHeight(LinearLayout linearLayout) {

        DisplayMetrics dm = Utility.DisplayUtilty.getDisplayMetricFromWindow(appCompatActivity);
        int w = (int) (dm.widthPixels * 0.50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(120, 0, 0, 20);
        linearLayout.setLayoutParams(layoutParams);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}