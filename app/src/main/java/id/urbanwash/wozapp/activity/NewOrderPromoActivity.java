package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.util.NotificationUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class NewOrderPromoActivity extends BaseActivity {

    private AppCompatEditText mPromoCodeText;
    private AppCompatButton mApplyButton;

    private AppCompatActivity mAppCompatActivity;

    private PromoBean mPromoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_promo);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        mPromoCodeText = (AppCompatEditText) findViewById(R.id.text_promo_code);
        mApplyButton = (AppCompatButton) findViewById(R.id.button_apply);

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_promo));

        mApplyButton.setOnClickListener(getApplyButtonOnClickListener());
    }

    private View.OnClickListener getApplyButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPromoBean = new PromoBean();
                mPromoBean.setCode(mPromoCodeText.getText().toString());

                showProgressDlg(getString(R.string.message_async_processing));

                HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
                httpAsyncManager.verifyPromo(mPromoBean);
            }
        };
    }

    @Override
    public void onAsyncGetPromo(PromoBean promoBean) {

        hideProgressDlgNow();

        if (promoBean != null) {

            Intent intent = getIntent();
            intent.putExtra(Constant.DATA_PROMO, promoBean);
            setResult(RESULT_OK, intent);
            finish();

        } else {
            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.error_invalid_promo_code));
        }
    };
}