package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RadioButton;

import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

public class PromoDetailActivity extends BaseActivity {

    private AppCompatActivity mAppCompatActivity;

    private AppCompatEditText mCodeText;
    private AppCompatEditText mDescriptionText;
    private AppCompatEditText mValueText;
    private AppCompatEditText mMinOrderText;
    private AppCompatEditText mStartDateText;
    private AppCompatEditText mEndDateText;

    private RadioButton mAmountRadioButton;
    private RadioButton mPercentageRadioButton;

    private RadioButton mActiveRadioButton;
    private RadioButton mInactiveRadioButton;

    private AppCompatTextView mSaveButton;

    private PromoBean mPromoBean;

    private static final int CONFIRM_SAVE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_manage_promo_new));

        mCodeText = (AppCompatEditText) findViewById(R.id.text_code);
        mDescriptionText = (AppCompatEditText) findViewById(R.id.text_description);
        mValueText = (AppCompatEditText) findViewById(R.id.text_value);
        mMinOrderText = (AppCompatEditText) findViewById(R.id.text_min_order);
        mStartDateText = (AppCompatEditText) findViewById(R.id.text_start_date);
        mEndDateText = (AppCompatEditText) findViewById(R.id.text_end_date);

        mStartDateText.setOnClickListener(getDateFieldOnClickListener("startDatePicker"));
        mEndDateText.setOnClickListener(getDateFieldOnClickListener("endDatePicker"));

        linkDatePickerWithInputField("startDatePicker", mStartDateText);
        linkDatePickerWithInputField("endDatePicker", mEndDateText);

        mAmountRadioButton = (RadioButton) findViewById(R.id.radio_button_amount);
        mPercentageRadioButton = (RadioButton) findViewById(R.id.radio_button_percentage);

        mActiveRadioButton = (RadioButton) findViewById(R.id.radio_button_active);
        mInactiveRadioButton = (RadioButton) findViewById(R.id.radio_button_inactive);

        mSaveButton = (AppCompatTextView) findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(getSaveButtonOnClickListener());

        Intent intent = getIntent();
        mPromoBean = (PromoBean) intent.getSerializableExtra(Constant.DATA_PROMO);

        if (mPromoBean != null) {

            titleLabel.setText(getString(R.string.title_manage_promo_update));

            mCodeText.setText(mPromoBean.getCode());
            mDescriptionText.setText(mPromoBean.getDescription());
            mValueText.setText(CommonUtil.formatString((int) mPromoBean.getValue()));
            mMinOrderText.setText(CommonUtil.formatString((int) mPromoBean.getMinOrder()));
            mStartDateText.setText(CommonUtil.formatDate(mPromoBean.getStartDate()));
            mEndDateText.setText(CommonUtil.formatDate(mPromoBean.getEndDate()));

            mAmountRadioButton.setChecked(true);

            if (Constant.PROMO_TYPE_PERCENTAGE.equals(mPromoBean.getType())) {
                mPercentageRadioButton.setChecked(true);
            }

            mActiveRadioButton.setChecked(true);

            if (Constant.CREDIT_STATUS_INACTIVE.equals(mPromoBean.getStatus())) {
                mInactiveRadioButton.setChecked(true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mCodeText.requestFocus();
        hideSoftKeyboard(mCodeText);
        hideSoftKeyboard(mDescriptionText);
        hideSoftKeyboard(mValueText);
        hideSoftKeyboard(mMinOrderText);
    }

    private View.OnClickListener getSaveButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    showConfirmDlg(CONFIRM_SAVE, getString(R.string.confirm_save_promo));
                }
            }
        };
    }

    private boolean validate() {

        boolean isValidated = true;

        String code = mCodeText.getText().toString();
        String description = mDescriptionText.getText().toString();
        String value = mValueText.getText().toString();
        String minOrder = mMinOrderText.getText().toString();
        Date startDate =  CommonUtil.parseDate(mStartDateText.getText().toString());
        Date endDate =  CommonUtil.parseDate(mEndDateText.getText().toString());

        if (CommonUtil.isEmpty(code)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_code_required));
            isValidated = false;

        } else if (CommonUtil.isEmpty(description)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_description_required));
            isValidated = false;

        } else if (CommonUtil.isEmpty(value)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_value_required));
            isValidated = false;

        } else if (CommonUtil.isEmpty(minOrder)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_min_order_required));
            isValidated = false;

        } else if (startDate == null) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_start_date_required));
            isValidated = false;

        } else if (endDate == null) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_end_date_required));
            isValidated = false;
        }

        return isValidated;
    }

    @Override
    public void onConfirm(int id) {

        if (id == CONFIRM_SAVE) {

            savePromo();
        }
    }

    private void savePromo() {

        String code = mCodeText.getText().toString();
        String description = mDescriptionText.getText().toString();
        float value = CommonUtil.parseFloat(mValueText.getText().toString());
        float minOrder = CommonUtil.parseFloat(mMinOrderText.getText().toString());
        Date startDate =  CommonUtil.parseDate(mStartDateText.getText().toString());
        Date endDate =  CommonUtil.parseDate(mEndDateText.getText().toString());
        String type = mAmountRadioButton.isChecked() ? Constant.PROMO_TYPE_AMOUNT : Constant.PROMO_TYPE_PERCENTAGE;
        String status = mActiveRadioButton.isChecked() ? Constant.CREDIT_STATUS_ACTIVE : Constant.CREDIT_STATUS_INACTIVE;

        if (mPromoBean == null) {
            mPromoBean = new PromoBean();
        }

        mPromoBean.setCode(code);
        mPromoBean.setDescription(description);
        mPromoBean.setValue(value);
        mPromoBean.setMinOrder(minOrder);
        mPromoBean.setStartDate(startDate);
        mPromoBean.setEndDate(endDate);
        mPromoBean.setType(type);
        mPromoBean.setStatus(status);

        showProgressDlg(getString(R.string.message_async_saving));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);

        if (mPromoBean.getId() == null) {
            httpAsyncManager.addPromo(mPromoBean);
        } else {
            httpAsyncManager.updatePromo(mPromoBean);
        }
    }

    @Override
    public void onAsyncGetPromos(List<PromoBean> promos) {

        hideProgressDlgNow();

        Session.setPromos(promos);

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    }
}