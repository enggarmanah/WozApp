package id.urbanwash.wozapp;

/**
 * Created by Radix on 28/1/2016.
 */
public class Constant {

    public static final String EMPTY_STRING = "";
    public static final String NEW_LINE_STRING = "\n";
    public static final String SPACE_STRING = " ";

    public static final String URBANWASH_CONTACT = "082221911516";

    public static final String DEVICE_TYPE_ANDROID = "ANDROID";
    public static final String DEVICE_TYPE_IOS = "IOS";

    public static final int MAP_PADDING = 200;

    public static final String PRODUCT_KG_WASH_IRON = "KGWAIR";
    public static final String PRODUCT_ITEM_WASH_IRON = "ITWAIR";
    public static final String PRODUCT_DRY_CLEAN = "ITDRCL";

    public static final String PLACE_TYPE_HOME = "H";
    public static final String PLACE_TYPE_APARTMENT = "A";
    public static final String PLACE_TYPE_OFFICE = "O";

    public static final String STATUS_ACTIVE = "A";
    public static final String STATUS_INACTIVE = "I";

    public static final String STATUS_YES = "Y";
    public static final String STATUS_NO = "N";

    public static final String CONTACT_CALL = "CALL";
    public static final String CONTACT_SMS = "SMS";

    public static final String NOTIFICATION_TYPE_ALERT = "NOTIFICATION_TYPE_ALERT";
    public static final String NOTIFICATION_TYPE_INFO = "NOTIFICATION_TYPE_INFO";
    public static final String NOTIFICATION_TYPE_ERROR = "NOTIFICATION_TYPE_ERROR";

    public static final String FCM_TOPIC_PROMO = "promo";

    public static final float MAP_MIN_ZOOM = 17f;

    public static final String TIMEZONE = "GMT+7";

    //public static final int EARLIEST_HOUR_FOR_ORDER = 2;
    public static final int EARLIEST_HOUR_FOR_ORDER = 7;
    public static final int LATEST_HOUR_FOR_ORDER = 21;

    public static final int MAX_FETCH = 15;

    public static final Long TIME_HOUR = 60 * 60 * 1000l;
    public static final Long TIME_DAY = 24 * TIME_HOUR;

    public static final int MAX_COLLECTION_DATE = 7;
    public static final int MAX_DELIVERY_DATE = 14;
    public static final int MAX_RESCHEDULE_COUNT = 2;

    public static final int OPERATING_TIME_WINDOW = 2;

    public static final Long MIN_PROCESSING_TIME_REQUIRED = 20 * TIME_HOUR;

    public static final String INTENT_DATA_RESULT = "INTENT_DATA_RESULT";
    public static final String INTENT_DATA_TITLE = "INTENT_DATA_TITLE";
    public static final String INTENT_DATA_PRODUCT = "INTENT_DATA_PRODUCT";
    public static final String INTENT_DATA_ORDER = "INTENT_DATA_ORDER";
    public static final String INTENT_DATA_STATUS = "INTENT_DATA_STATUS";

    public static final int ORDER_STATUS_INT_OUTSTANDING = 0;
    public static final int ORDER_STATUS_INT_COMPLETED = 1;

    public static final String INFO_WINDOW_RED = "INFO_WINDOW_RED";
    public static final String INFO_WINDOW_GREEN = "INFO_WINDOW_GREEN";
    public static final String INFO_WINDOW_BLUE = "INFO_WINDOW_BLUE";

    public static final String SPEED_TYPE_REGULAR = "R";
    public static final String SPEED_TYPE_EXPRESS = "E";
    public static final String SPEED_TYPE_DELUXE = "D";

    public static final int PROCESS_TIME_REGULAR = 4;
    public static final int PROCESS_TIME_EXPRESS = 2;
    public static final int PROCESS_TIME_DELUXE = 1;

    public static final String ORDER_STATUS_NEW_ORDER = "NEW";
    public static final String ORDER_STATUS_ASSIGNED_FOR_COLLECTION = "ACO";
    public static final String ORDER_STATUS_COLLECTION_IN_PROGRESS = "CIN";
    public static final String ORDER_STATUS_COLLECTED = "COL";
    public static final String ORDER_STATUS_CLEANING_IN_PROGRESS = "CLI";
    public static final String ORDER_STATUS_CLEANED = "CLE";
    public static final String ORDER_STATUS_ASSIGNED_FOR_DELIVERY = "ADE";
    public static final String ORDER_STATUS_DELIVERY_IN_PROGRESS = "DEL";
    public static final String ORDER_STATUS_COMPLETED = "COM";

    public static final String ORDER_STATUS_WARNING = "WRN";
    public static final String ORDER_STATUS_CRITICAL = "CTL";

    public static final String MESSAGE_STATUS_PENDING = "P";
    public static final String MESSAGE_STATUS_DELIVERED = "D";

    public static final String PAYMENT_TYPE_CASH = "C";
    public static final String PAYMENT_TYPE_WALLET = "W";

    public static final String CREDIT_TYPE_VOUCHER = "V";
    public static final String CREDIT_TYPE_TOP_UP = "T";
    public static final String CREDIT_TYPE_PAYMENT = "P";

    public static final String PROMO_TYPE_AMOUNT = "A";
    public static final String PROMO_TYPE_PERCENTAGE = "P";

    public static final String CREDIT_STATUS_PENDING = "P";
    public static final String CREDIT_STATUS_ACTIVE = "A";
    public static final String CREDIT_STATUS_INACTIVE = "I";

    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_FEMALE = "FEMALE";

    public static final String TITLE_MR = "Mr";
    public static final String TITLE_MRS = "Mrs";
    public static final String TITLE_MS = "Ms";

    public static final String PLACE_DLG_TAG = "PLACE_DLG_TAG";
    public static final String COLLECTION_DLG_TAG = "COLLECTION_DLG_TAG";
    public static final String TIME_DLG_TAG = "TIME_DLG_TAG";
    public static final String SPEED_TYPE_DLG_TAG = "SPEED_TYPE_DLG_TAG";
    public static final String GENDER_DLG_TAG = "GENDER_DLG_TAG";
    public static final String STATUS_DLG_TAG = "STATUS_DLG_TAG";
    public static final String AGENT_DLG_TAG = "AGENT_DLG_TAG";
    public static final String BIRTHDAY_DLG_TAG = "BIRTHDAY_DLG_TAG";
    public static final String PROGRESS_DLG_TAG = "PROGRESS_DLG_TAG";
    public static final String CONFIRM_DLG_TAG = "CONFIRM_DLG_TAG";
    public static final String ACK_DLG_TAG = "ACK_DLG_TAG";
    public static final String CONTACT_DLG_TAG = "CONTACT_DLG_TAG";
    public static final String CHANGE_DELIVERY_DATE_DLG_TAG = "CHANGE_DELIVERY_DATE_DLG_TAG";
    public static final String ORDER_STATUS_FRAGMENT_TAG = "ORDER_STATUS_FRAGMENT_TAG";

    public static final String LAUNDRY_SHIRT = "SHRT";
    public static final String LAUNDRY_TSHIRT = "TSRT";
    public static final String LAUNDRY_TROUSER = "TRSR";
    public static final String LAUNDRY_SHORT = "SHRT";
    public static final String LAUNDRY_JACKET = "JCKT";
    public static final String LAUNDRY_SUIT = "SUIT";
    public static final String LAUNDRY_DRESS = "DRSS";
    public static final String LAUNDRY_LONG_DRESS = "LDRS";
    public static final String LAUNDRY_SKIRT = "SKRT";
    public static final String LAUNDRY_BLAZER = "BLZR";
    public static final String LAUNDRY_BRA = "UBRA";
    public static final String LAUNDRY_BED_SHEET = "BDST";
    public static final String LAUNDRY_BED_COVER = "BDCV";
    public static final String LAUNDRY_TOWEL = "TOWL";
    public static final String LAUNDRY_MAT = "MATT";
    public static final String LAUNDRY_CARPET = "CRPT";
    public static final String LAUNDRY_CURTAIN = "CRTN";

    public static final String URL_AUTHENTICATION = "/authentication";
    public static final String URL_CUSTOMER = "/customer";
    public static final String URL_ORDER = "/order";
    public static final String URL_PRODUCT = "/product";
    public static final String URL_PROMO = "/promo";
    public static final String URL_REPORT = "/report";
    public static final String URL_MESSAGE = "/message";
    public static final String URL_EMPLOYEE = "/employee";
    public static final String URL_PLACE = "/place";
    public static final String URL_IMAGE = "/image";
    public static final String URL_CREDIT = "/credit";
    public static final String URL_PIC = "/pic";
    public static final String URL_VERIFICATION = "/verification";

    public static final String EMPLOYEE_TYPE_ADMIN = "A";
    public static final String EMPLOYEE_TYPE_TRANSPORTER = "T";

    public static final String TASK_AUTHENTICATE = "TASK_AUTHENTICATE";
    public static final String TASK_LOGOUT = "TASK_LOGOUT";
    public static final String TASK_RESET_PASSWORD = "TASK_RESET_PASSWORD";
    public static final String TASK_CHANGE_PASSWORD = "TASK_CHANGE_PASSWORD";
    public static final String TASK_CUSTOMER_EMAIL_CHECK = "TASK_CUSTOMER_EMAIL_CHECK";
    public static final String TASK_CUSTOMER_REGISTER = "TASK_CUSTOMER_REGISTER";
    public static final String TASK_CUSTOMER_UPDATE = "TASK_CUSTOMER_UPDATE";
    public static final String TASK_CUSTOMER_REFRESH = "TASK_CUSTOMER_REFRESH";
    public static final String TASK_CUSTOMER_SEARCH = "TASK_CUSTOMER_SEARCH";
    public static final String TASK_EMPLOYEE_UPDATE = "TASK_EMPLOYEE_UPDATE";
    public static final String TASK_ADMIN_REFRESH = "TASK_ADMIN_REFRESH";
    public static final String TASK_TRANSPORTER_REFRESH = "TASK_TRANSPORTER_REFRESH";
    public static final String TASK_ORDER_ALL = "TASK_ORDER_ALL";
    public static final String TASK_ORDER_OUTSTANDING = "TASK_ORDER_OUTSTANDING";
    public static final String TASK_ORDER_COMPLETED = "TASK_ORDER_COMPLETED";
    public static final String TASK_ORDER_BY_STATUS = "TASK_ORDER_BY_STATUS";
    public static final String TASK_ORDER_CURRENT = "TASK_ORDER_CURRENT";
    public static final String TASK_ORDER_COLLECTION = "TASK_ORDER_COLLECTION";
    public static final String TASK_ORDER_DELIVERY = "TASK_ORDER_DELIVERY";
    public static final String TASK_ORDER_SEARCH = "TASK_ORDER_SEARCH";
    public static final String TASK_ORDER_SUBMIT = "TASK_ORDER_SUBMIT";
    public static final String TASK_ORDER_UPDATE_STATUS = "TASK_ORDER_UPDATE_STATUS";
    public static final String TASK_ORDER_UPDATE_DELIVERY_DATE = "TASK_ORDER_UPDATE_DELIVERY_DATE";
    public static final String TASK_ORDER_UPDATE_PAYMENT_TYPE = "TASK_ORDER_UPDATE_PAYMENT_TYPE";
    public static final String TASK_ORDER_UPDATE_PRODUCT_AND_STATUS = "TASK_ORDER_UPDATE_PRODUCT_AND_STATUS";
    public static final String TASK_ORDER_INIT = "TASK_ORDER_INIT";
    public static final String TASK_ORDER_REPEAT_ORDER = "TASK_ORDER_REPEAT_ORDER";
    public static final String TASK_ORDER_DELIVERY_TIMES = "TASK_ORDER_DELIVERY_TIMES";
    public static final String TASK_ORDER_SUMMARY = "TASK_ORDER_SUMMARY";
    public static final String TASK_ORDER_CANCEL = "TASK_ORDER_CANCEL";
    public static final String TASK_REPORT_YEARLY = "TASK_REPORT_YEARLY";
    public static final String TASK_REPORT_MONTHLY = "TASK_REPORT_MONTHLY";
    public static final String TASK_REPORT_DAILY = "TASK_REPORT_DAILY";
    public static final String TASK_REPORT_DAILY_COLLECTION = "TASK_REPORT_DAILY_COLLECTION";
    public static final String TASK_REPORT_HOURLY_COLLECTION = "TASK_REPORT_HOURLY_COLLECTION";
    public static final String TASK_REPORT_DAILY_DELIVERY = "TASK_REPORT_DAILY_DELIVERY";
    public static final String TASK_REPORT_HOURLY_DELIVERY = "TASK_REPORT_HOURLY_DELIVERY";
    public static final String TASK_REPORT_DAILY_PIC = "TASK_REPORT_DAILY_PIC";
    public static final String TASK_REPORT_DAILY_PIC_DETAIL = "TASK_REPORT_DAILY_PIC_DETAIL";
    public static final String TASK_REPORT_ORDER_PLACE = "TASK_REPORT_ORDER_PLACE";
    public static final String TASK_REPORT_PROSPECTIVE_PLACE = "TASK_REPORT_PROSPECTIVE_PLACE";
    public static final String TASK_PRODUCT_GET = "TASK_PRODUCT_GET";
    public static final String TASK_PIC_COLLECTION_GET = "TASK_PIC_COLLECTION_GET";
    public static final String TASK_PIC_DELIVERY_GET = "TASK_PIC_DELIVERY_GET";
    public static final String TASK_PLACE_ADD = "TASK_PLACE_ADD";
    public static final String TASK_PLACE_GET = "TASK_PLACE_GET";
    public static final String TASK_PLACE_DELETE = "TASK_PLACE_DELETE";
    public static final String TASK_PROMO_VERIFY = "TASK_PROMO_VERIFY";
    public static final String TASK_PROMO_GET = "TASK_PROMO_GET";
    public static final String TASK_PROMO_ADD = "TASK_PROMO_ADD";
    public static final String TASK_PROMO_UPDATE = "TASK_PROMO_UPDATE";
    public static final String TASK_MESSAGE_GET = "TASK_MESSAGE_GET";
    public static final String TASK_MESSAGE_ADD = "TASK_MESSAGE_ADD";
    public static final String TASK_MESSAGE_UPDATE = "TASK_MESSAGE_UPDATE";
    public static final String TASK_IMAGE_GET = "TASK_IMAGE_GET";
    public static final String TASK_IMAGE_GET_UPDATE = "TASK_IMAGE_GET_UPDATE";
    public static final String TASK_CREDIT_GET = "TASK_CREDIT_GET";
    public static final String TASK_CREDIT_GET_PENDING_APPROVAL = "TASK_CREDIT_GET_PENDING_APPROVAL";
    public static final String TASK_CREDIT_GET_PENDING_APPROVAL_COUNT = "TASK_CREDIT_GET_PENDING_APPROVAL_COUNT";
    public static final String TASK_CREDIT_ADD = "TASK_CREDIT_ADD";
    public static final String TASK_CREDIT_APPROVE = "TASK_CREDIT_APPROVE";
    public static final String TASK_CUSTOMER_MOBILE_VERIFY = "TASK_CUSTOMER_MOBILE_VERIFY";
    public static final String TASK_EMPLOYEE_MOBILE_VERIFY = "TASK_EMPLOYEE_MOBILE_VERIFY";;

    public static final String ERROR_INVALID_TOKEN = "ERR001";
    public static final String ERROR_INVALID_APP_CERT = "ERR002";
    public static final String ERROR_APP_UPDATE_REQUIRED = "ERR003";
    public static final String ERROR_SYSTEM_MAINTENANCE = "ERR004";
    public static final String ERROR_REGISTER_CUSTOMER_EXIST = "ERR005";
    public static final String ERROR_AUTHENTICATION_FAILED = "ERR006";
    public static final String ERROR_LOCATION_NOT_WITHIN_SERVICE_AREA = "ERR008";
    public static final String ERROR_CHANGE_DELIVERY_NO_LONGER_ALLOWED = "ERR009";
    public static final String ERROR_INSUFFICIENT_WALLET_BALANCE = "ERR010";
    public static final String ERROR_RESET_PASSWORD = "ERR011";
    public static final String ERROR_CHANGE_PASSWORD = "ERR012";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String MESSAGE_ORDER_NEW_ORDER = "MESSAGE_ORDER_NEW_ORDER";
    public static final String MESSAGE_ORDER_ASSIGNED_FOR_COLLECTION = "MESSAGE_ORDER_ASSIGNED_FOR_COLLECTION";
    public static final String MESSAGE_ORDER_COLLECTION_IN_PROGRESS = "MESSAGE_ORDER_COLLECTION_IN_PROGRESS";
    public static final String MESSAGE_ORDER_COLLECTED = "MESSAGE_ORDER_COLLECTED";
    public static final String MESSAGE_ORDER_CLEANING_IN_PROGRESS = "MESSAGE_ORDER_CLEANING_IN_PROGRESS";
    public static final String MESSAGE_ORDER_CLEANED = "MESSAGE_ORDER_CLEANED";
    public static final String MESSAGE_ORDER_ASSIGNED_FOR_DELIVERY = "MESSAGE_ORDER_ASSIGNED_FOR_DELIVERY";
    public static final String MESSAGE_ORDER_DELIVERY_IN_PROGRESS = "MESSAGE_ORDER_DELIVERY_IN_PROGRESS";
    public static final String MESSAGE_ORDER_COMPLETED = "MESSAGE_ORDER_COMPLETED";
    public static final String MESSAGE_ORDER_CANCELLED = "MESSAGE_ORDER_CANCELLED";

    public static final String FCM_PARAM_MESSAGE = "MESSAGE";
    public static final String FCM_PARAM_ORDER_NO = "ORDER_NO";
    public static final String FCM_PARAM_ORDER_STATUS = "ORDER_STATUS";
    public static final String FCM_PARAM_ORDER_DATE = "ORDER_DATE";
    public static final String FCM_PARAM_ORDER_PLACE_NAME = "ORDER_PLACE_NAME";
    public static final String FCM_PARAM_ORDER_PLACE_ADDRESS = "ORDER_PLACE_ADDRESS";

    public static final String DATA_ADDRESS = "DATA_ADDRESS";
    public static final String DATA_PLACE = "DATA_PLACE";
    public static final String DATA_ORDER_STATUS = "DATA_ORDER_STATUS";
    public static final String DATA_REPEAT_ORDER = "DATA_REPEAT_ORDER";
    public static final String DATA_PROMO = "DATA_PROMO";
    public static final String DATA_DATE = "DATA_DATE";
    public static final String DATA_EMPLOYEE = "DATA_EMPLOYEE";
    public static final String DATA_ORDER = "DATA_ORDER";
}
