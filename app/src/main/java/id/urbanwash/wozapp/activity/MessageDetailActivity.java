package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncManager;
import id.urbanwash.wozapp.model.MessageBean;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.NotificationUtil;

public class MessageDetailActivity extends BaseActivity {

    private AppCompatActivity mAppCompatActivity;

    private AppCompatEditText mContentText;
    
    private AppCompatTextView mSendButton;

    private MessageBean mMessageBean;

    private static final int CONFIRM_SENDING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_manage_message_new));

        mContentText = (AppCompatEditText) findViewById(R.id.text_content);

        mSendButton = (AppCompatTextView) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(getSendButtonOnClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();

        mContentText.requestFocus();
        hideSoftKeyboard(mContentText);
    }

    private View.OnClickListener getSendButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    showConfirmDlg(CONFIRM_SENDING, getString(R.string.confirm_send_message));
                }
            }
        };
    }

    public void onConfirm(int id) {

        if (id == CONFIRM_SENDING) {

            sendMessage();
        }
    }

    private void sendMessage() {

        String content = mContentText.getText().toString();
        Date broadcastDate = new Date();
        String status = Constant.MESSAGE_STATUS_PENDING;

        if (mMessageBean == null) {
            mMessageBean = new MessageBean();
        }

        mMessageBean.setContent(content);
        mMessageBean.setBroadcastDate(broadcastDate);
        mMessageBean.setStatus(status);

        showProgressDlg(getString(R.string.message_async_sending));

        HttpAsyncManager httpAsyncManager = new HttpAsyncManager(mAppCompatActivity);
        httpAsyncManager.addMessage(mMessageBean);
    }

    private boolean validate() {

        boolean isValidated = true;

        String content = mContentText.getText().toString();

        if (CommonUtil.isEmpty(content)) {

            NotificationUtil.showErrorMessage(mAppCompatActivity, getString(R.string.alert_message_required));
            isValidated = false;
        }

        return isValidated;
    }

    @Override
    public void onAsyncGetMessages(List<MessageBean> messages) {

        hideProgressDlgNow();

        Session.setMessages(messages);

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);

        finish();
    }
}