package id.urbanwash.wozapp.async;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import id.urbanwash.wozapp.Config;
import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
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
import id.urbanwash.wozapp.model.RequestBean;
import id.urbanwash.wozapp.model.ResponseBean;
import id.urbanwash.wozapp.model.ServiceAreaBean;
import id.urbanwash.wozapp.util.CommonUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpAsyncManager {

    private HttpAsyncTask task;
    private HttpAsyncCommonListener mHttpAsyncCommonListener;
    private HttpAsyncImageListener mHttpAsyncImageListener;

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    private Call mCall = null;

    private Context mContext;

    private ObjectMapper mapper;

    private Long mStartTime;
    private int mAppVersion;

    private CustomerBean mCustomerBean;
    private EmployeeBean mEmployeeBean;
    private OrderBean mOrderBean;
    private PlaceBean mPlaceBean;
    private ServiceAreaBean mServiceAreaBean;
    private CreditBean mCreditBean;
    private PromoBean mPromoBean;
    private MessageBean mMessageBean;
    private ImageBean mImageBean;

    private String mOrderStatus;
    private String mSearch;
    private Integer mPage;

    private String mEmail;
    private String mPassword;

    private Date mDate;

    public HttpAsyncManager(Context context) {

        this.mContext = context;

        if (context instanceof  HttpAsyncCommonListener) {
            mHttpAsyncCommonListener = (HttpAsyncCommonListener) context;
        }

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mAppVersion = pInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");

        mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public HttpAsyncManager(Context context, HttpAsyncImageListener listener) {

        this.mContext = context;
        this.mHttpAsyncImageListener = listener;

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mAppVersion = pInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");

        mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void executeTask(String t) {

        mStartTime = new Date().getTime();

        task = new HttpAsyncTask();
        task.execute(t);
    }

    public Runnable getTimeOutHandler(final HttpAsyncTask task) {

        return new Runnable() {

            @Override
            public void run() {

                if (task.getStatus() == AsyncTask.Status.RUNNING) {
                    task.cancel(true);
                    abortHttpRequest();
                    mHttpAsyncCommonListener.onAsyncTimeOut();
                }
            }
        };
    }

    public void authenticate(String email, String password) {

        mEmail = email;
        mPassword = password;

        executeTask(Constant.TASK_AUTHENTICATE);
    }

    public void logout(CustomerBean customerBean) {

        mCustomerBean = customerBean;

        executeTask(Constant.TASK_LOGOUT);
    }

    public void logout(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;

        executeTask(Constant.TASK_LOGOUT);
    }

    public void registerCustomer(CustomerBean customerBean, PlaceBean placeBean) {

        mCustomerBean = customerBean;
        mPlaceBean = placeBean;

        executeTask(Constant.TASK_CUSTOMER_REGISTER);
    }

    public void checkCustomerEmail(CustomerBean customerBean) {

        mCustomerBean = customerBean;

        executeTask(Constant.TASK_CUSTOMER_EMAIL_CHECK);
    }

    public void updateCustomer(CustomerBean customerBean) {

        mCustomerBean = customerBean;

        executeTask(Constant.TASK_CUSTOMER_UPDATE);
    }

    public void updateEmployee(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;

        executeTask(Constant.TASK_EMPLOYEE_UPDATE);
    }

    public void verifyMobile(CustomerBean customerBean) {

        mCustomerBean = customerBean;

        executeTask(Constant.TASK_CUSTOMER_MOBILE_VERIFY);
    }

    public void verifyMobile(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;

        executeTask(Constant.TASK_EMPLOYEE_MOBILE_VERIFY);
    }

    //public void resetPassword(String email, String mobile) {
    public void resetPassword(String email) {

        mCustomerBean = new CustomerBean();
        mCustomerBean.setEmail(email);
        //mCustomerBean.setMobile(mobile);

        mEmployeeBean = new EmployeeBean();
        mEmployeeBean.setEmail(email);
        //mEmployeeBean.setMobile(mobile);

        executeTask(Constant.TASK_RESET_PASSWORD);
    }

    public void changePassword(String email, String password, String newPassword) {

        mCustomerBean = new CustomerBean();
        mCustomerBean.setEmail(email);
        mCustomerBean.setPassword(password);
        mCustomerBean.setNewPassword(newPassword);

        mEmployeeBean = new EmployeeBean();
        mEmployeeBean.setEmail(email);
        mEmployeeBean.setPassword(password);
        mEmployeeBean.setNewPassword(newPassword);

        executeTask(Constant.TASK_CHANGE_PASSWORD);
    }

    public void refreshCustomerData(CustomerBean customerBean) {

        mCustomerBean = customerBean;

        executeTask(Constant.TASK_CUSTOMER_REFRESH);
    }

    public void refreshAdminData(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;

        executeTask(Constant.TASK_ADMIN_REFRESH);
    }

    public void refreshTransporterData(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;

        executeTask(Constant.TASK_TRANSPORTER_REFRESH);
    }

    public void refreshPendingCreditCount() {

        executeTask(Constant.TASK_CREDIT_GET_PENDING_APPROVAL_COUNT);
    }

    public void submitOrder(OrderBean orderBean) {

        mOrderBean = orderBean;
        executeTask(Constant.TASK_ORDER_SUBMIT);
    }

    public void cancelOrder(OrderBean orderBean) {

        mOrderBean = orderBean;
        executeTask(Constant.TASK_ORDER_CANCEL);
    }

    public void addCredit(CreditBean creditBean) {

        mCreditBean = creditBean;
        executeTask(Constant.TASK_CREDIT_ADD);
    }

    public void approveCredit(CreditBean creditBean) {

        mCreditBean = creditBean;
        executeTask(Constant.TASK_CREDIT_APPROVE);
    }

    public void verifyPromo(PromoBean promoBean) {

        mPromoBean = promoBean;
        executeTask(Constant.TASK_PROMO_VERIFY);
    }

    public void addPromo(PromoBean promoBean) {

        mPromoBean = promoBean;
        executeTask(Constant.TASK_PROMO_ADD);
    }

    public void updatePromo(PromoBean promoBean) {

        mPromoBean = promoBean;
        executeTask(Constant.TASK_PROMO_UPDATE);
    }

    public void getPromos(int page) {

        mPage = page;
        executeTask(Constant.TASK_PROMO_GET);
    }

    public void getOrderPlaceReport() {

        executeTask(Constant.TASK_REPORT_ORDER_PLACE);
    }

    public void getProspectivePlaceReport() {

        executeTask(Constant.TASK_REPORT_PROSPECTIVE_PLACE);
    }

    public void getYearlyReport() {

        executeTask(Constant.TASK_REPORT_YEARLY);
    }

    public void getMonthlyReport(Date year) {

        mDate = year;
        executeTask(Constant.TASK_REPORT_MONTHLY);
    }

    public void getDailyReport(Date month) {

        mDate = month;
        executeTask(Constant.TASK_REPORT_DAILY);
    }

    public void getDailyCollectionReport() {

        executeTask(Constant.TASK_REPORT_DAILY_COLLECTION);
    }

    public void getDailyCollectionReport(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;
        executeTask(Constant.TASK_REPORT_DAILY_COLLECTION);
    }

    public void getHourlyCollectionReport(Date collectionDate) {

        mDate = collectionDate;
        executeTask(Constant.TASK_REPORT_HOURLY_COLLECTION);
    }

    public void getHourlyCollectionReport(EmployeeBean employeeBean, Date collectionDate) {

        mEmployeeBean = employeeBean;
        mDate = collectionDate;

        executeTask(Constant.TASK_REPORT_HOURLY_COLLECTION);
    }

    public void getDailyDeliveryReport() {

        executeTask(Constant.TASK_REPORT_DAILY_DELIVERY);
    }

    public void getDailyDeliveryReport(EmployeeBean employeeBean) {

        mEmployeeBean = employeeBean;
        executeTask(Constant.TASK_REPORT_DAILY_DELIVERY);
    }

    public void getHourlyDeliveryReport(Date deliveryDate) {

        mDate = deliveryDate;
        executeTask(Constant.TASK_REPORT_HOURLY_DELIVERY);
    }

    public void getHourlyDeliveryReport(EmployeeBean employeeBean, Date deliveryDate) {

        mEmployeeBean = employeeBean;
        mDate = deliveryDate;

        executeTask(Constant.TASK_REPORT_HOURLY_DELIVERY);
    }

    public void getDailyReport(Date month, EmployeeBean pic) {

        mDate = month;
        mEmployeeBean = pic;

        executeTask(Constant.TASK_REPORT_DAILY);
    }

    public void getDailyReportPic(Date date) {

        mDate = date;
        executeTask(Constant.TASK_REPORT_DAILY_PIC);
    }

    public void getDailyReportPicDetail(EmployeeBean pic, Date date) {

        mEmployeeBean = pic;
        mDate = date;

        executeTask(Constant.TASK_REPORT_DAILY_PIC_DETAIL);
    }

    public void addMessage(MessageBean messageBean) {

        mMessageBean = messageBean;
        executeTask(Constant.TASK_MESSAGE_ADD);
    }

    public void getMessages(int page) {

        mPage = page;
        executeTask(Constant.TASK_MESSAGE_GET);
    }

    public void updateOrderStatus(OrderBean orderBean) {

        mOrderBean = orderBean;
        mEmployeeBean = Session.getEmployee();

        executeTask(Constant.TASK_ORDER_UPDATE_STATUS);
    }

    public void updateOrderDeliveryDate(OrderBean orderBean) {

        mOrderBean = orderBean;

        executeTask(Constant.TASK_ORDER_UPDATE_DELIVERY_DATE);
    }

    public void updateOrderPaymentType(OrderBean orderBean) {

        mOrderBean = orderBean;

        executeTask(Constant.TASK_ORDER_UPDATE_PAYMENT_TYPE);
    }

    public void initOrder(CustomerBean customerBean, Date date, ServiceAreaBean serviceAreaBean) {

        mDate = date;
        mCustomerBean = customerBean;
        mServiceAreaBean = serviceAreaBean;

        executeTask(Constant.TASK_ORDER_INIT);
    }

    public void repeatOrder(Date date, ServiceAreaBean serviceAreaBean) {

        mDate = date;
        mServiceAreaBean = serviceAreaBean;

        executeTask(Constant.TASK_ORDER_REPEAT_ORDER);
    }

    public void initDeliveryTimes(Date date, ServiceAreaBean serviceAreaBean) {

        mDate = date;
        mServiceAreaBean = serviceAreaBean;

        executeTask(Constant.TASK_ORDER_DELIVERY_TIMES);
    }

    public void updateOrderServiceAndStatus(OrderBean orderBean) {

        mOrderBean = orderBean;
        mEmployeeBean = Session.getEmployee();

        executeTask(Constant.TASK_ORDER_UPDATE_PRODUCT_AND_STATUS);
    }

    public void getOutstandingOrders(CustomerBean customerBean) {

        mCustomerBean = customerBean;
        executeTask(Constant.TASK_ORDER_OUTSTANDING);
    }

    public void getCompletedOrders(CustomerBean customerBean) {

        mCustomerBean = customerBean;
        executeTask(Constant.TASK_ORDER_COMPLETED);
    }

    public void getAllOrders(CustomerBean customerBean) {

        mCustomerBean = customerBean;
        executeTask(Constant.TASK_ORDER_ALL);
    }

    public void getOrdersByStatus(EmployeeBean employee, String orderStatus, Integer page) {

        mEmployeeBean = employee;
        mOrderStatus = orderStatus;
        mPage = page;

        executeTask(Constant.TASK_ORDER_BY_STATUS);
    }

    public void getCurrentOrders(EmployeeBean employee) {

        mEmployeeBean = employee;

        executeTask(Constant.TASK_ORDER_CURRENT);
    }

    public void getCollectionOrders(Date collectionDate) {

        mDate = collectionDate;

        executeTask(Constant.TASK_ORDER_COLLECTION);
    }

    public void getCollectionOrders(EmployeeBean employee, Date deliveryDate) {

        mEmployeeBean = employee;
        mDate = deliveryDate;

        executeTask(Constant.TASK_ORDER_COLLECTION);
    }

    public void getDeliveryOrders(Date collectionDate) {

        mDate = collectionDate;

        executeTask(Constant.TASK_ORDER_DELIVERY);
    }

    public void getDeliveryOrders(EmployeeBean employee, Date deliveryDate) {

        mEmployeeBean = employee;
        mDate = deliveryDate;

        executeTask(Constant.TASK_ORDER_DELIVERY);
    }

    public void searchOrders(String search, Integer page) {

        mSearch = search;
        mPage = page;

        executeTask(Constant.TASK_ORDER_SEARCH);
    }

    public void searchCustomers(String search, Integer page) {

        mSearch = search;
        mPage = page;

        executeTask(Constant.TASK_CUSTOMER_SEARCH);
    }

    public void addPlace(PlaceBean placeBean) {

        mPlaceBean = placeBean;
        mCustomerBean = mPlaceBean.getCustomer();
        executeTask(Constant.TASK_PLACE_ADD);
    }

    public void deletePlace(PlaceBean placeBean) {

        mPlaceBean = placeBean;
        executeTask(Constant.TASK_PLACE_DELETE);
    }

    public void getPlaces(CustomerBean customerBean) {

        mCustomerBean = customerBean;
        executeTask(Constant.TASK_PLACE_GET);
    }

    public void getImage(ImageBean imageBean) {

        mImageBean = imageBean;
        executeTask(Constant.TASK_IMAGE_GET);
    }

    public void getImageUpdate(ImageBean imageBean) {

        mImageBean = imageBean;
        executeTask(Constant.TASK_IMAGE_GET_UPDATE);
    }

    public void getCredits(CustomerBean customerBean, Integer page) {

        mCustomerBean = customerBean;
        mPage = page;

        executeTask(Constant.TASK_CREDIT_GET);
    }

    public void getPendingCredits(Integer page) {

        mPage = page;

        executeTask(Constant.TASK_CREDIT_GET_PENDING_APPROVAL);
    }

    public void getProducts() {

        executeTask(Constant.TASK_PRODUCT_GET);
    }

    public void getCollectionPics(ServiceAreaBean serviceAreaBean, Date collectionDate) {

        mServiceAreaBean = serviceAreaBean;
        mDate = collectionDate;

        executeTask(Constant.TASK_PIC_COLLECTION_GET);
    }

    public void getDeliveryPics(ServiceAreaBean serviceAreaBean, Date deliveryDate) {

        mServiceAreaBean = serviceAreaBean;
        mDate = deliveryDate;

        executeTask(Constant.TASK_PIC_DELIVERY_GET);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        String task;
        int index = 0;

        @Override
        protected String doInBackground(String... params) {

            task = params[0];

            String url = Constant.EMPTY_STRING;

            RequestBean request = new RequestBean();

            request.setSecurityKey(Session.getSecurityKey());
            request.setAppVersion(mAppVersion);
            request.setDeviceType(Constant.DEVICE_TYPE_ANDROID);
            request.setDeviceId(Session.getDeviceId());

            request.setCertDn(CommonUtil.getCertDN(mContext));
            request.setTask(task);

            if (Constant.TASK_AUTHENTICATE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_AUTHENTICATION;

                request.setEmail(mEmail);
                request.setPassword(mPassword);

            } else if (Constant.TASK_LOGOUT.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_AUTHENTICATION;

                request.setCustomer(mCustomerBean);
                request.setEmployee(mEmployeeBean);

            } else if (Constant.TASK_CUSTOMER_REGISTER.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CUSTOMER;
                request.setCustomer(mCustomerBean);
                request.setPlace(mPlaceBean);

            } else if (Constant.TASK_CUSTOMER_EMAIL_CHECK.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CUSTOMER;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_CUSTOMER_UPDATE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CUSTOMER;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_CUSTOMER_REFRESH.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CUSTOMER;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_EMPLOYEE_UPDATE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_EMPLOYEE;
                request.setEmployee(mEmployeeBean);

            } else if (Constant.TASK_ADMIN_REFRESH.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_EMPLOYEE;

                EmployeeBean employeeBean = mEmployeeBean.clone();
                employeeBean.setImage(null);

                request.setEmployee(employeeBean);

            } else if (Constant.TASK_TRANSPORTER_REFRESH.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_EMPLOYEE;

                EmployeeBean employeeBean = mEmployeeBean.clone();
                employeeBean.setImage(null);

                request.setEmployee(employeeBean);

            } else if (Constant.TASK_ORDER_INIT.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setCustomer(mCustomerBean);
                request.setDate(mDate);
                request.setServiceArea(mServiceAreaBean);

            } else if (Constant.TASK_ORDER_REPEAT_ORDER.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setDate(mDate);
                request.setServiceArea(mServiceAreaBean);

            } else if (Constant.TASK_ORDER_DELIVERY_TIMES.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setDate(mDate);
                request.setServiceArea(mServiceAreaBean);

            } else if (Constant.TASK_ORDER_SUBMIT.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;
                request.setOrder(mOrderBean);

            } else if (Constant.TASK_ORDER_CANCEL.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;
                request.setOrder(mOrderBean);

            } else if (Constant.TASK_ORDER_UPDATE_STATUS.equals(task) ||
                    Constant.TASK_ORDER_UPDATE_PRODUCT_AND_STATUS.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setOrder(mOrderBean);

                EmployeeBean employeeBean = mEmployeeBean.clone();
                employeeBean.setImage(null);

                request.setEmployee(employeeBean);

            } else if (Constant.TASK_ORDER_UPDATE_DELIVERY_DATE.equals(task) ||
                    Constant.TASK_ORDER_UPDATE_PAYMENT_TYPE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setOrder(mOrderBean);

            } else if (Constant.TASK_ORDER_OUTSTANDING.equals(task) ||
                    Constant.TASK_ORDER_COMPLETED.equals(task) ||
                    Constant.TASK_ORDER_ALL.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_ORDER_BY_STATUS.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setEmployee(mEmployeeBean);
                request.setOrderStatus(mOrderStatus);
                request.setPage(mPage);

            } else if (Constant.TASK_ORDER_CURRENT.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setEmployee(mEmployeeBean);

            } else if (Constant.TASK_ORDER_COLLECTION.equals(task) ||
                    Constant.TASK_ORDER_DELIVERY.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;

                request.setEmployee(mEmployeeBean);
                request.setDate(mDate);

            } else if (Constant.TASK_ORDER_SEARCH.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_ORDER;
                request.setSearch(mSearch);
                request.setPage(mPage);

            } else if (Constant.TASK_CUSTOMER_SEARCH.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CUSTOMER;
                request.setSearch(mSearch);
                request.setPage(mPage);

            } else if (Constant.TASK_PLACE_ADD.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PLACE;
                request.setPlace(mPlaceBean);
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_PLACE_GET.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PLACE;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_PLACE_DELETE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PLACE;
                request.setPlace(mPlaceBean);

            } else if (Constant.TASK_IMAGE_GET.equals(task) ||
                    Constant.TASK_IMAGE_GET_UPDATE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_IMAGE;
                request.setImage(mImageBean);

            } else if (Constant.TASK_CREDIT_GET.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CREDIT;
                request.setCustomer(mCustomerBean);
                request.setPage(mPage);

            } else if (Constant.TASK_CREDIT_GET_PENDING_APPROVAL_COUNT.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CREDIT;
                request.setPage(mPage);

            } else if (Constant.TASK_CREDIT_GET_PENDING_APPROVAL.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CREDIT;
                request.setPage(mPage);

            } else if (Constant.TASK_CREDIT_ADD.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CREDIT;
                request.setCredit(mCreditBean);

            } else if (Constant.TASK_CREDIT_APPROVE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_CREDIT;
                request.setCredit(mCreditBean);

            } else if (Constant.TASK_PROMO_ADD.equals(task) ||
                    Constant.TASK_PROMO_UPDATE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PROMO;
                request.setPromo(mPromoBean);

            } else if (Constant.TASK_PROMO_VERIFY.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PROMO;
                request.setPromo(mPromoBean);

            } else if (Constant.TASK_RESET_PASSWORD.equals(task) ||
                    Constant.TASK_CHANGE_PASSWORD.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_AUTHENTICATION;
                request.setCustomer(mCustomerBean);
                request.setEmployee(mEmployeeBean);
                request.setLocale(CommonUtil.getLocale().toString());

            } else if (Constant.TASK_PROMO_GET.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PROMO;
                request.setPage(mPage);

            } else if (Constant.TASK_REPORT_YEARLY.equals(task) ||
                    Constant.TASK_REPORT_MONTHLY.equals(task) ||
                    Constant.TASK_REPORT_DAILY.equals(task) ||
                    Constant.TASK_REPORT_DAILY_PIC.equals(task) ||
                    Constant.TASK_REPORT_DAILY_COLLECTION.equals(task) ||
                    Constant.TASK_REPORT_HOURLY_COLLECTION.equals(task) ||
                    Constant.TASK_REPORT_DAILY_DELIVERY.equals(task) ||
                    Constant.TASK_REPORT_HOURLY_DELIVERY.equals(task) ||
                    Constant.TASK_REPORT_ORDER_PLACE.equals(task) ||
                    Constant.TASK_REPORT_PROSPECTIVE_PLACE.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_REPORT;

                request.setDate(mDate);
                request.setEmployee(mEmployeeBean);

            } else if (Constant.TASK_REPORT_DAILY_PIC_DETAIL.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_REPORT;

                request.setEmployee(mEmployeeBean);
                request.setDate(mDate);

            } else if (Constant.TASK_MESSAGE_ADD.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_MESSAGE;
                request.setMessage(mMessageBean);

            } else if (Constant.TASK_MESSAGE_GET.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_MESSAGE;
                request.setPage(mPage);

            } else if (Constant.TASK_PIC_COLLECTION_GET.equals(task) ||
                    Constant.TASK_PIC_DELIVERY_GET.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_PIC;

                request.setServiceArea(mServiceAreaBean);
                request.setDate(mDate);

            } else if (Constant.TASK_CUSTOMER_MOBILE_VERIFY.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_VERIFICATION;
                request.setCustomer(mCustomerBean);

            } else if (Constant.TASK_EMPLOYEE_MOBILE_VERIFY.equals(task)) {

                url = Config.SERVER_URL + Constant.URL_VERIFICATION;
                request.setEmployee(mEmployeeBean);
            }

            return post(url, request);
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                ResponseBean resp = mapper.readValue(result, ResponseBean.class);

                if (ResponseBean.ERROR.equals(resp.getRespCode())) {

                    if (mHttpAsyncCommonListener != null) {
                        mHttpAsyncCommonListener.onAsyncError(getErrorDescription(resp.getRespDescription()));

                    } else if (mHttpAsyncImageListener != null) {
                        mHttpAsyncImageListener.onAsyncError(getErrorDescription(resp.getRespDescription()));
                    }

                } else if (ResponseBean.SUCCESS.equals(resp.getRespCode())) {

                    if (Constant.TASK_AUTHENTICATE.equals(task)) {

                        CustomerBean customerBean = resp.getCustomer();
                        Session.setCustomer(customerBean);

                        EmployeeBean employeeBean = resp.getEmployee();
                        Session.setEmployee(employeeBean);

                        List<ProductBean> productBeans = resp.getProducts();
                        Session.setProducts(productBeans);

                        if (customerBean != null) {
                            List<PlaceBean> placeBeans = resp.getPlaces();
                            Session.setPlaces(placeBeans);
                            //customerBean.setPlaces(placeBeans);
                        }

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_LOGOUT.equals(task)) {

                        mHttpAsyncCommonListener.onAsyncLogout();

                    } else if (Constant.TASK_CUSTOMER_EMAIL_CHECK.equals(task)) {

                        CustomerBean customerBean = resp.getCustomer();
                        mHttpAsyncCommonListener.onAsyncCheckEmail(customerBean);

                    } else if (Constant.TASK_CUSTOMER_REGISTER.equals(task)) {

                        CustomerBean customerBean = resp.getCustomer();
                        Session.setCustomer(customerBean);

                        List<ProductBean> productBeans = resp.getProducts();
                        Session.setProducts(productBeans);

                        if (customerBean != null) {
                            List<PlaceBean> placeBeans = resp.getPlaces();
                            Session.setPlaces(placeBeans);
                            //customerBean.setPlaces(placeBeans);
                        }

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_CUSTOMER_UPDATE.equals(task)) {

                        ImageBean imageBean = Session.getCustomer().getImage();

                        CustomerBean customerBean = resp.getCustomer();
                        Session.setCustomer(customerBean);

                        // set image bytes from device
                        if (imageBean != null && imageBean.getBytes() != null && customerBean.getImage() != null) {
                            customerBean.getImage().setBytes(imageBean.getBytes());
                        }

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_CUSTOMER_REFRESH.equals(task)) {

                        Session.setCustomer(resp.getCustomer());
                        Session.setOutstandingOrders(resp.getOutstandingOrders());
                        Session.setCompletedOrders(resp.getCompletedOrders());
                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_EMPLOYEE_UPDATE.equals(task)) {

                        ImageBean imageBean = Session.getEmployee().getImage();

                        EmployeeBean employeeBean = resp.getEmployee();
                        Session.setEmployee(employeeBean);

                        // set image bytes from device
                        if (imageBean != null && imageBean.getBytes() != null && employeeBean.getImage() != null) {
                            employeeBean.getImage().setBytes(imageBean.getBytes());
                        }

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_ADMIN_REFRESH.equals(task)) {

                        Session.setCredits(resp.getCredits());
                        Session.setOrderSummary(resp.getOrderSummary());
                        Session.setPendingCreditCount(resp.getPendingCreditCount());

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_TRANSPORTER_REFRESH.equals(task)) {

                        Session.setCredits(resp.getCredits());
                        Session.setOrderSummary(resp.getOrderSummary());
                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_ORDER_INIT.equals(task)) {

                        Map<Date, List<Long>> collectionDateTimes = resp.getCollectionDateTimes();
                        Map<Date, List<Long>> deliveryDateTimes = resp.getDeliveryDateTimes();
                        List<ProductBean> productBeans = resp.getProducts();
                        List<PlaceBean> placeBeans = resp.getPlaces();
                        Float minimumOrder = resp.getMinimumOrder();

                        Session.setPlaces(placeBeans);
                        Session.setCollectionDateTimes(collectionDateTimes);
                        Session.setDeliveryDateTimes(deliveryDateTimes);
                        Session.setProducts(productBeans);
                        Session.setMinimumOrder(minimumOrder);

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_ORDER_REPEAT_ORDER.equals(task)) {

                        Map<Date, List<Long>> deliveryDateTimes = resp.getDeliveryDateTimes();
                        List<ProductBean> productBeans = resp.getProducts();

                        Session.setDeliveryDateTimes(deliveryDateTimes);
                        Session.setProducts(productBeans);
                        mHttpAsyncCommonListener.onAsyncRepeatOrder();

                    } else if (Constant.TASK_ORDER_DELIVERY_TIMES.equals(task)) {

                        Map<Date, List<Long>> deliveryDateTimes = resp.getDeliveryDateTimes();

                        Session.setDeliveryDateTimes(deliveryDateTimes);
                        mHttpAsyncCommonListener.onAsyncGetDeliveryTimes(deliveryDateTimes);

                    } else if (Constant.TASK_ORDER_SUBMIT.equals(task)) {

                        OrderBean orderBean = resp.getOrder();
                        List<OrderBean> orders = resp.getOutstandingOrders();

                        Session.setOrder(orderBean);
                        mHttpAsyncCommonListener.onAsyncGetOutstandingOrders(orders);

                    } else if (Constant.TASK_ORDER_CANCEL.equals(task)) {

                        List<OrderBean> orders = resp.getOutstandingOrders();

                        Session.setOutstandingOrders(orders);
                        mHttpAsyncCommonListener.onAsyncCancelOrder(orders);

                    } else if (Constant.TASK_ORDER_UPDATE_STATUS.equals(task) ||
                            Constant.TASK_ORDER_UPDATE_PRODUCT_AND_STATUS.equals(task)) {

                        OrderBean order = resp.getOrder();
                        Session.setOrders(resp.getOrders());

                        mHttpAsyncCommonListener.onAsyncUpdateOrderStatus(order);

                    } else if (Constant.TASK_ORDER_UPDATE_DELIVERY_DATE.equals(task)) {

                        OrderBean order = resp.getOrder();
                        CustomerBean customer = resp.getCustomer();

                        Session.setCustomer(customer);

                        mHttpAsyncCommonListener.onAsyncUpdateOrderDeliveryDate(order);

                    } else if (Constant.TASK_ORDER_UPDATE_PAYMENT_TYPE.equals(task)) {

                        OrderBean order = resp.getOrder();
                        CustomerBean customer = resp.getCustomer();

                        Session.setCustomer(customer);

                        mHttpAsyncCommonListener.onAsyncUpdateOrderPaymentType(order);

                    } else if (Constant.TASK_ORDER_OUTSTANDING.equals(task)) {

                        List<OrderBean> orders = resp.getOutstandingOrders();
                        mHttpAsyncCommonListener.onAsyncGetOutstandingOrders(orders);

                    } else if (Constant.TASK_ORDER_COMPLETED.equals(task)) {

                        List<OrderBean> orders = resp.getCompletedOrders();
                        mHttpAsyncCommonListener.onAsyncGetOutstandingOrders(orders);

                    } else if (Constant.TASK_ORDER_ALL.equals(task)) {

                        List<OrderBean> outstandingOrders = resp.getOutstandingOrders();
                        List<OrderBean> completedOrders = resp.getCompletedOrders();

                        mHttpAsyncCommonListener.onAsyncGetOutstandingOrders(outstandingOrders);
                        mHttpAsyncCommonListener.onAsyncGetCompletedOrders(completedOrders);

                    } else if (Constant.TASK_ORDER_BY_STATUS.equals(task) ||
                            Constant.TASK_ORDER_CURRENT.equals(task) ||
                            Constant.TASK_ORDER_COLLECTION.equals(task) ||
                            Constant.TASK_ORDER_DELIVERY.equals(task) ||
                            Constant.TASK_ORDER_SEARCH.equals(task)) {

                        List<OrderBean> orders = resp.getOrders();
                        mHttpAsyncCommonListener.onAsyncGetOrders(orders);

                    } else if (Constant.TASK_CUSTOMER_SEARCH.equals(task)) {

                        List<CustomerBean> customers = resp.getCustomers();
                        mHttpAsyncCommonListener.onAsyncGetCustomers(customers);

                    } else if (Constant.TASK_PLACE_GET.equals(task)) {

                        List<PlaceBean> places = resp.getPlaces();
                        mHttpAsyncCommonListener.onAsyncGetPlaces(places);

                    } else if (Constant.TASK_PLACE_ADD.equals(task)) {

                        List<PlaceBean> places = resp.getPlaces();
                        mHttpAsyncCommonListener.onAsyncGetPlaces(places);

                    } else if (Constant.TASK_PLACE_DELETE.equals(task)) {

                        List<PlaceBean> places = resp.getPlaces();
                        mHttpAsyncCommonListener.onAsyncGetPlaces(places);

                    } else if (Constant.TASK_IMAGE_GET.equals(task) ||
                            Constant.TASK_IMAGE_GET_UPDATE.equals(task)) {

                        ImageBean imageBean = resp.getImage();
                        mHttpAsyncImageListener.onAsyncGetImage(imageBean);

                    } else if (Constant.TASK_CREDIT_GET.equals(task)) {

                        List<CreditBean> credits = resp.getCredits();
                        mHttpAsyncCommonListener.onAsyncGetCredits(credits);

                    } else if (Constant.TASK_CREDIT_GET_PENDING_APPROVAL_COUNT.equals(task)) {

                        Session.setPendingCreditCount(resp.getPendingCreditCount());
                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_CREDIT_GET_PENDING_APPROVAL.equals(task)) {

                        List<CreditBean> credits = resp.getCredits();
                        mHttpAsyncCommonListener.onAsyncGetCredits(credits);

                    } else if (Constant.TASK_CREDIT_ADD.equals(task)) {

                        CreditBean credit = resp.getCredit();
                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_CREDIT_APPROVE.equals(task)) {

                        CreditBean credit = resp.getCredit();
                        List<CreditBean> credits = resp.getCredits();
                        mHttpAsyncCommonListener.onAsyncGetCredits(credits);

                    } else if (Constant.TASK_PROMO_ADD.equals(task) ||
                            Constant.TASK_PROMO_UPDATE.equals(task)) {

                        List<PromoBean> promos = resp.getPromos();
                        mHttpAsyncCommonListener.onAsyncGetPromos(promos);

                    } else if (Constant.TASK_PROMO_VERIFY.equals(task)) {

                        PromoBean promo = resp.getPromo();
                        mHttpAsyncCommonListener.onAsyncGetPromo(promo);

                    } else if (Constant.TASK_RESET_PASSWORD.equals(task) ||
                            Constant.TASK_CHANGE_PASSWORD.equals(task)) {

                        mHttpAsyncCommonListener.onAsyncCompleted();

                    } else if (Constant.TASK_PROMO_GET.equals(task)) {

                        List<PromoBean> promos = resp.getPromos();
                        mHttpAsyncCommonListener.onAsyncGetPromos(promos);

                    } else if (Constant.TASK_REPORT_YEARLY.equals(task) ||
                            Constant.TASK_REPORT_MONTHLY.equals(task) ||
                            Constant.TASK_REPORT_DAILY.equals(task) ||
                            Constant.TASK_REPORT_DAILY_PIC.equals(task) ||
                            Constant.TASK_REPORT_DAILY_PIC_DETAIL.equals(task) ||
                            Constant.TASK_REPORT_DAILY_COLLECTION.equals(task) ||
                            Constant.TASK_REPORT_HOURLY_COLLECTION.equals(task) ||
                            Constant.TASK_REPORT_DAILY_DELIVERY.equals(task) ||
                            Constant.TASK_REPORT_HOURLY_DELIVERY.equals(task) ||
                            Constant.TASK_REPORT_ORDER_PLACE.equals(task) ||
                            Constant.TASK_REPORT_PROSPECTIVE_PLACE.equals(task)) {

                        List<ReportBean> reports = resp.getReports();
                        mHttpAsyncCommonListener.onAsyncGetReports(reports);

                    } else if (Constant.TASK_MESSAGE_ADD.equals(task)) {

                        List<MessageBean> messages = resp.getMessages();
                        mHttpAsyncCommonListener.onAsyncGetMessages(messages);

                    } else if (Constant.TASK_MESSAGE_GET.equals(task)) {

                        List<MessageBean> messages = resp.getMessages();
                        mHttpAsyncCommonListener.onAsyncGetMessages(messages);

                    } else if (Constant.TASK_PIC_COLLECTION_GET.equals(task) ||
                            Constant.TASK_PIC_DELIVERY_GET.equals(task)) {

                        List<EmployeeBean> agents = resp.getEmployees();
                        mHttpAsyncCommonListener.onAsyncGetPics(agents);

                    } else if (Constant.TASK_PRODUCT_GET.equals(task)) {

                        List<ProductBean> products = resp.getProducts();
                        mHttpAsyncCommonListener.onAsyncGetProducts(products);

                    } else if (Constant.TASK_CUSTOMER_MOBILE_VERIFY.equals(task)) {

                        CustomerBean customerBean = resp.getCustomer();
                        mHttpAsyncCommonListener.onAsyncVerifyMobile(customerBean);

                    } else if (Constant.TASK_EMPLOYEE_MOBILE_VERIFY.equals(task)) {

                        EmployeeBean employeeBean = resp.getEmployee();
                        mHttpAsyncCommonListener.onAsyncVerifyMobile(employeeBean);
                    }
                }

            } catch (NullPointerException npe) {

                npe.printStackTrace();

                if (mHttpAsyncCommonListener != null) {

                    String message = mContext.getString(R.string.error_failed_to_connect);
                    mHttpAsyncCommonListener.onAsyncError(message);
                }

            } catch (Exception e) {

                e.printStackTrace();

                if (mHttpAsyncCommonListener != null) {
                    mHttpAsyncCommonListener.onAsyncError(e.getMessage());

                } else if (mHttpAsyncImageListener != null) {
                    mHttpAsyncImageListener.onAsyncError(e.getMessage());
                }
            }

            System.out.println("Processing TimeBean : " + (new Date().getTime() - mStartTime));
        }
    }

    private void abortHttpRequest() {

        if (mCall != null) {
            mCall.cancel();
        }
    }

    private String post(String url, Object object) {

        String resp = null;

        try {

            final OutputStream out = new ByteArrayOutputStream();
            final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

            writer.writeValue(out, object);

            String json = out.toString();

            RequestBody body = RequestBody.create(JSON, compress(json.getBytes()));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            mCall = mHttpClient.newCall(request);
            Response response = mCall.execute();
            resp = uncompress(response.body().bytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

    public String getErrorDescription(String code) {

        String errorDesc = mContext.getString(R.string.error_general);

        if (Constant.ERROR_SYSTEM_MAINTENANCE.equals(code)) {
            errorDesc = mContext.getString(R.string.error_system_maintenance);

        } else if (Constant.ERROR_APP_UPDATE_REQUIRED.equals(code)) {
            errorDesc = mContext.getString(R.string.error_app_update_required);

        } else if (Constant.ERROR_INVALID_TOKEN.equals(code)) {
            errorDesc = mContext.getString(R.string.error_invalid_token);

        } else if (Constant.ERROR_INVALID_APP_CERT.equals(code)) {
            errorDesc = mContext.getString(R.string.error_invalid_app_cert);

        } else if (Constant.ERROR_AUTHENTICATION_FAILED.equals(code)) {
            errorDesc = mContext.getString(R.string.error_authentication_failed);

        } else if (Constant.ERROR_REGISTER_CUSTOMER_EXIST.equals(code) ||
            Constant.ERROR_EMAIL_HAS_BEEN_REGISTERED.equals(code)) {

            errorDesc = mContext.getString(R.string.error_register_customer_exist);

        } else if (Constant.ERROR_LOCATION_NOT_WITHIN_SERVICE_AREA.equals(code)) {
            errorDesc = mContext.getString(R.string.error_location_not_within_service_area);

        } else if (Constant.ERROR_CHANGE_DELIVERY_NO_LONGER_ALLOWED.equals(code)) {
            errorDesc = mContext.getString(R.string.error_change_delivery_no_longer_allowed);

        } else if (Constant.ERROR_INSUFFICIENT_WALLET_BALANCE.equals(code)) {
            errorDesc = mContext.getString(R.string.error_insufficient_wallet_balance);

        } else if (Constant.ERROR_RESET_PASSWORD.equals(code)) {
            errorDesc = mContext.getString(R.string.error_reset_password);

        } else if (Constant.ERROR_CHANGE_PASSWORD.equals(code)) {
            errorDesc = mContext.getString(R.string.error_change_password_old_password_incorrect);
        }

        return errorDesc;
    }

    public byte[] compress(byte[] bytes) throws IOException {

        System.out.println("Size Original : " + bytes.length);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(baos);

        gos.write(bytes);
        gos.close();

        byte[] output = baos.toByteArray();

        System.out.println("Size Compressed : " + output.length);

        return output;
    }

    public String uncompress(byte[] bytes)
            throws IOException {

        String result = null;
        GZIPInputStream gis = null;

        try {

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            gis = new GZIPInputStream(bais);
            result = IOUtils.toString(gis);

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            if (gis != null) {
                gis.close();
            }
        }

        return result;
    }
}