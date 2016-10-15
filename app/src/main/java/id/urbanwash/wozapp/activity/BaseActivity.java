package id.urbanwash.wozapp.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.async.HttpAsyncCommonListener;
import id.urbanwash.wozapp.dialog.ContactDialog;
import id.urbanwash.wozapp.listener.AcknowledgementDialogListener;
import id.urbanwash.wozapp.listener.ConfirmDialogListener;
import id.urbanwash.wozapp.dialog.AcknowledgmentDialog;
import id.urbanwash.wozapp.dialog.ConfirmDialog;
import id.urbanwash.wozapp.dialog.ProgressDialog;
import id.urbanwash.wozapp.fragment.DatePickerFragment;
import id.urbanwash.wozapp.listener.ContactDialogListener;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.model.MessageBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.model.ProductBean;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.NotificationUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity implements HttpAsyncCommonListener, ConfirmDialogListener, AcknowledgementDialogListener, ContactDialogListener {

    protected ProgressDialog mProgressDlgFragment;
    protected ConfirmDialog mConfirmDlgFragment;
    protected AcknowledgmentDialog mAckDlgFragment;
    protected ContactDialog mContactDlgFragment;

    protected String TAG;

    // get next page records variable

    protected Integer mPage = 1;

    private boolean mIsLoading = false;
    private int mFirstVisibleItemIndex;
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private boolean mIsReachLastPage;

    protected LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getLocalClassName();

        CodeUtil.initCodes(this);
        Session.setContext(this);

        mProgressDlgFragment = (ProgressDialog) getFragmentManager().findFragmentByTag(Constant.PROGRESS_DLG_TAG);

        if (mProgressDlgFragment == null) {
            mProgressDlgFragment = new ProgressDialog();
        }

        mProgressDlgFragment.setAppCompatActivity(this);

        mConfirmDlgFragment = (ConfirmDialog) getSupportFragmentManager().findFragmentByTag(Constant.CONFIRM_DLG_TAG);

        if (mConfirmDlgFragment == null) {
            mConfirmDlgFragment = new ConfirmDialog();
        }

        mConfirmDlgFragment.setAppCompatActivity(this);

        mAckDlgFragment = (AcknowledgmentDialog) getSupportFragmentManager().findFragmentByTag(Constant.ACK_DLG_TAG);

        if (mAckDlgFragment == null) {
            mAckDlgFragment = new AcknowledgmentDialog();
        }

        mAckDlgFragment.setAppCompatActivity(this);

        mContactDlgFragment = (ContactDialog) getSupportFragmentManager().findFragmentByTag(Constant.CONTACT_DLG_TAG);

        if (mContactDlgFragment == null) {
            mContactDlgFragment = new ContactDialog();
        }

        mContactDlgFragment.setAppCompatActivity(this);

        final View mainView = findViewById(android.R.id.content);

        mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainView.getWindowVisibleDisplayFrame(r);
                int screenHeight = mainView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("NewOrderActivity", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    onKeyboardVisible();
                } else {
                    onKeyboardGone();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                //Intent intent = getIntent();
                //setResult(RESULT_OK, intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        //Intent intent = getIntent();
        //setResult(RESULT_OK, intent);
        finish();
    }

    protected void onKeyboardVisible() {
    }

    protected void onKeyboardGone() {
    }

    public void showProgressDlg() {

        mProgressDlgFragment.show(getFragmentManager(), Constant.PROGRESS_DLG_TAG);
    }

    public void showProgressDlg(String message) {

        if (!mProgressDlgFragment.isAdded()) {

            mProgressDlgFragment.show(getFragmentManager(), Constant.PROGRESS_DLG_TAG);
            mProgressDlgFragment.setMessage(message);
        }
    }

    protected void hideProgressDlg() {

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                mProgressDlgFragment.dismissAllowingStateLoss();
            }
        };

        timer.schedule(timerTask, 1500);
    }

    protected void hideProgressDlgNow() {

        mProgressDlgFragment.dismissAllowingStateLoss();
    }

    public void showConfirmDlg(int confirmId, String message) {

        mConfirmDlgFragment.show(getSupportFragmentManager(), Constant.CONFIRM_DLG_TAG);
        mConfirmDlgFragment.setConfirm(confirmId, message);
    }

    public void showAcknowlegementDlg(int ackId, String message) {

        mAckDlgFragment.show(getSupportFragmentManager(), Constant.ACK_DLG_TAG);
        mAckDlgFragment.setAcknowledgement(ackId, message);
    }

    public void showContactDlg(String name, ImageBean image, String mobile, String type) {

        mContactDlgFragment.show(getSupportFragmentManager(), Constant.CONTACT_DLG_TAG);
        mContactDlgFragment.setContact(name, image, mobile, type);
    }

    protected synchronized void addFragment(Object fragment, String fragmentTag) {

        Fragment f = (Fragment) fragment;

        if (f.isAdded()) {
            return;
        }

        try {
            getFragmentManager().beginTransaction().add(R.id.panel_fragment, f, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected synchronized void replaceFragment(Object fragment, String fragmentTag) {

        Fragment f = (Fragment) fragment;

        if (f.isAdded()) {
            return;
        }

        try {
            getFragmentManager().beginTransaction().replace(R.id.panel_fragment, f, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeFragment(Object fragment) {

        getFragmentManager().beginTransaction().remove((Fragment) fragment).commit();
    }

    protected Drawable changeHomeButton() {

        /*Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.icon_back)).getBitmap();
        Drawable upArrow = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 48, 48, true));
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);*/

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        return upArrow;
    }

    protected RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {

        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0)
                {
                    mFirstVisibleItemIndex = mLayoutManager.findFirstVisibleItemPosition();
                    mVisibleItemCount = mLayoutManager.getChildCount();
                    mTotalItemCount = mLayoutManager.getItemCount();

                    if (!mIsLoading && !mIsReachLastPage)
                    {
                        if ( (mVisibleItemCount + mFirstVisibleItemIndex) >= mTotalItemCount)
                        {
                            mIsLoading = true;
                            mPage += 1;

                            getNextPageRecords();
                        }
                    }
                }
            }
        };
    }

    protected void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }

    protected void setReachLastPage(boolean isReachLastPage) {
        mIsReachLastPage = isReachLastPage;
    }

    protected void getNextPageRecords() {}

    @Override
    public void onAsyncTimeOut() {
    }

    @Override
    public void onAsyncCompleted() {

        hideProgressDlg();
    }

    @Override
    public void onAsyncError(String message) {

        hideProgressDlgNow();
        NotificationUtil.showErrorMessage(this, message);
    }

    @Override
    public void onAsyncAuthenticate(CustomerBean customerBean) {
    }

    @Override
    public void onAsyncRegisterCustomer(CustomerBean customerBean) {
    }

    @Override
    public void onAsyncCancelOrder(List<OrderBean> orders) {
    }

    @Override
    public void onAsyncUpdateOrderStatus(OrderBean orderBean) {
    }

    @Override
    public void onAsyncUpdateOrderPaymentType(OrderBean orderBean) {
    }

    @Override
    public void onAsyncUpdateOrderDeliveryDate(OrderBean orderBean) {
    }

    @Override
    public void onAsyncRepeatOrder() {
    }

    @Override
    public void onAsyncGetOutstandingOrders(List<OrderBean> orderBeans) {
    }

    @Override
    public void onAsyncGetCompletedOrders(List<OrderBean> orderBeans) {
    }

    @Override
    public void onAsyncGetOrders(List<OrderBean> orders) {
    }

    @Override
    public void onAsyncGetCustomers(List<CustomerBean> customers) {
    }

    @Override
    public void onAsyncGetProducts(List<ProductBean> productBeans) {
    }

    @Override
    public void onAsyncGetPics(List<EmployeeBean> employeeBeans) {
    }

    @Override
    public void onAsyncGetPlaces(List<PlaceBean> placeBeans) {
    }

    @Override
    public void onAsyncGetCredits(List<CreditBean> creditBeans) {
    }

    @Override
    public void onAsyncGetDeliveryTimes(Map<Date, List<Long>> deliveryDateTimes) {
    }

    @Override
    public void onAsyncGetPromo(PromoBean promo) {
    }

    public void onAsyncGetReports(List<ReportBean> reports) {
    }

    @Override
    public void onAsyncGetPromos(List<PromoBean> promos) {
    }

    @Override
    public void onAsyncCheckEmail(CustomerBean customerBean) {
    }

    @Override
    public void onAsyncVerifyMobile(CustomerBean customerBean) {
    }

    @Override
    public void onAsyncVerifyMobile(EmployeeBean employeeBean) {
    }

    @Override
    public void onAsyncGetMessages(List<MessageBean> messages) {
    }

    @Override
    public void onAsyncLogout() {
    }

    @Override
    public void onCall(String mobile) {

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            startActivity(callIntent);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSms(String mobile) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
            startActivity(intent);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfirm(int id) {}

    @Override
    public void onAcknowledge(int id) {}

    protected void showSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void hideSoftKeyboard(AppCompatEditText textView) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    protected void linkDatePickerWithInputField(String fragmentTag, AppCompatEditText inputField) {

        DatePickerFragment dp = (DatePickerFragment) getFragmentManager().findFragmentByTag(fragmentTag);
        if (dp != null) {
            dp.setInputField(inputField);
        }
    }

    protected View.OnClickListener getDateFieldOnClickListener(final String fragmentTag) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AppCompatEditText dateInput = (AppCompatEditText) v;

                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }

                DatePickerFragment fragment = null;

                if (getFragmentManager().findFragmentByTag(fragmentTag) != null) {
                    fragment = (DatePickerFragment) getFragmentManager().findFragmentByTag(fragmentTag);
                } else {
                    fragment = new DatePickerFragment();
                    fragment.setInputField(dateInput);
                }

                if (fragment.isAdded()) {
                    return;
                }

                fragment.show(getFragmentManager(), fragmentTag);
            }
        };
    }
}
