package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.fragment.RegisterStep3Fragment;
import id.urbanwash.wozapp.model.PlaceBean;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class NewPlaceActivity extends BaseActivity {

    private AppCompatTextView mOkButton;

    private AppCompatActivity mAppCompatActivity;

    private RegisterStep3Fragment mNewPlaceFragment;

    private LinearLayout mBottomPanel;

    private static final String PLACE_FRAGMENT = "PLACE_FRAGMENT";

    private PlaceBean mPlaceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mNewPlaceFragment = new RegisterStep3Fragment(mAppCompatActivity);
        replaceFragment(mNewPlaceFragment, PLACE_FRAGMENT);

        mOkButton = (AppCompatTextView) findViewById(R.id.button_next);

        mBottomPanel = (LinearLayout) findViewById(R.id.panel_bottom);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_new_address));

        mOkButton.setOnClickListener(getOkButtonOnClickListener());
    }

    private View.OnClickListener getOkButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNewPlaceFragment.isValidated()) {

                    mPlaceBean = mNewPlaceFragment.getPlace();
                    mPlaceBean.setCustomer(Session.getCustomer());

                    showProgressDlg(getString(R.string.message_async_saving));

                    HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                    httpAsyncManager.addPlace(mPlaceBean);
                }
            }
        };
    }

    @Override
    public void onAsyncGetPlaces(List<PlaceBean> placeBeans) {

        hideProgressDlgNow();

        mPlaceBean = placeBeans.get(0);

        Session.setPlace(mPlaceBean);
        Session.setPlaces(placeBeans);

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    };

    @Override
    protected void onKeyboardVisible() {
        mBottomPanel.setVisibility(View.GONE);
    }

    @Override
    protected void onKeyboardGone() {
        mBottomPanel.setVisibility(View.VISIBLE);
    }
}