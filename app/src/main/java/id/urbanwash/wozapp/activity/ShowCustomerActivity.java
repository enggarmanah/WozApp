package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.util.ImageManager;

public class ShowCustomerActivity extends BaseActivity {

    private CircleImageView mProfileImage;
    private AppCompatTextView mNameLabel;
    private AppCompatTextView mEmailLabel;
    private AppCompatTextView mMobileLabel;
    
    private AppCompatTextView mNewOrderButton;

    private AppCompatActivity mAppCompatActivity;
    
    private static final int CONFIRM_NEW_ORDER = 1;

    CustomerBean mCustomerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mProfileImage = (CircleImageView) findViewById(R.id.image_profile);
        mNameLabel = (AppCompatTextView) findViewById(R.id.label_name);
        mEmailLabel = (AppCompatTextView) findViewById(R.id.label_email);
        mMobileLabel = (AppCompatTextView) findViewById(R.id.label_mobile);

        mNewOrderButton = (AppCompatTextView) findViewById(R.id.button_new_order);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_customer));

        mNewOrderButton.setOnClickListener(getNextButtonOnClickListener());

        initView();
    }

    private void initView() {

        mCustomerBean = Session.getCustomer();

        ImageManager imageManager = new ImageManager(mAppCompatActivity);
        imageManager.setImage(mProfileImage, mCustomerBean.getImage());

        mNameLabel.setText(mCustomerBean.getName());
        mEmailLabel.setText(mCustomerBean.getEmail());
        mMobileLabel.setText(mCustomerBean.getMobile());
    }

    private View.OnClickListener getNextButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmDlg(CONFIRM_NEW_ORDER, getString(R.string.confirm_new_order));
            }
        };
    }

    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_NEW_ORDER) {

            showProgressDlg(getString(R.string.message_async_loading));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.getPlaces(mCustomerBean);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }
    
    @Override
    public void onAsyncGetPlaces(List<PlaceBean> places) {

        hideProgressDlgNow();

        Session.setPlaces(places);

        Session.setOrder(new OrderBean());

        Intent intent = new Intent(mAppCompatActivity, NewOrderActivity.class);
        mAppCompatActivity.startActivity(intent);
    }
}