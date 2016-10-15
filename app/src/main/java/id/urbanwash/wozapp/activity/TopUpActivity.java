package id.urbanwash.wozapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.Date;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

public class TopUpActivity extends BaseActivity {

    private AppCompatActivity mAppCompatActivity;

    private AppCompatEditText mBankNameText;
    private AppCompatEditText mAccOwnerNameText;
    private AppCompatEditText mAmountText;

    private AppCompatTextView mSubmitButton;

    private static final int CONFIRM_SUBMIT = 1;
    private static final int ACK_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppCompatActivity = this;

        setContentView(R.layout.activity_top_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_top_up));

        mBankNameText = (AppCompatEditText) findViewById(R.id.text_bank_name);
        mAccOwnerNameText = (AppCompatEditText) findViewById(R.id.text_acc_owner_name);
        mAmountText = (AppCompatEditText) findViewById(R.id.text_amount);

        mSubmitButton = (AppCompatTextView) findViewById(R.id.button_submit);
        mSubmitButton.setOnClickListener(getSubmitButtonOnClickListener());
    }

    private View.OnClickListener getSubmitButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidated()) {

                    showConfirmDlg(CONFIRM_SUBMIT, getString(R.string.confirm_submit_credit));
                }
            }
        };
    }

    @Override
    public void onConfirm(int confirmId) {

        if (confirmId == CONFIRM_SUBMIT) {

            CreditBean creditBean = getCredit();

            showProgressDlg(getString(R.string.message_async_processing));

            HttpAsyncManager httpAsyncManager = new HttpAsyncManager(this);
            httpAsyncManager.addCredit(creditBean);
        }
    }

    @Override
    public void onAcknowledge(int ackId) {

        if (ackId == ACK_SUCCESS) {

            finish();
        }
    }

    public void onAsyncCompleted() {

        showAcknowlegementDlg(ACK_SUCCESS, getString(R.string.ack_submit_credit));
    }

    private boolean isValidated() {

        boolean isValidated = true;

        if (CommonUtil.isEmpty(mBankNameText.getText().toString())) {

            isValidated = false;
            mBankNameText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_bank_name_required));

        } else if (CommonUtil.isEmpty(mAccOwnerNameText.getText().toString())) {

            isValidated = false;
            mAccOwnerNameText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_acc_owner_name_required));

        } else if (CommonUtil.isEmpty(mAmountText.getText().toString())) {

            isValidated = false;
            mAmountText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_amount_required));

        } else if (CommonUtil.parseFloat(mAmountText.getText().toString()) == null) {

            isValidated = false;
            mAmountText.requestFocus();
            NotificationUtil.showAlertMessage(mAppCompatActivity, getString(R.string.alert_invalid_format));
        }

        return isValidated;
    }

    private CreditBean getCredit() {

        CreditBean creditBean = new CreditBean();
        creditBean.setCustomer(Session.getCustomer());
        creditBean.setType(Constant.CREDIT_TYPE_TOP_UP);
        creditBean.setBankName(mBankNameText.getText().toString());
        creditBean.setAccOwnerName(mAccOwnerNameText.getText().toString());
        creditBean.setValue(CommonUtil.parseFloat(mAmountText.getText().toString()));
        creditBean.setDescription("Top Up");
        creditBean.setTransactionDate(new Date());
        creditBean.setStatus(Constant.CREDIT_STATUS_PENDING);

        return creditBean;
    }
}
