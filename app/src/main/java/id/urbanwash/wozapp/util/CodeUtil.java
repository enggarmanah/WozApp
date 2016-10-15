package id.urbanwash.wozapp.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.CodeBean;

public class CodeUtil {
	
	private static ArrayList<CodeBean> speedTypes;
    private static HashMap<String, String> speedTypeMap;

    private static ArrayList<CodeBean> speedTypesShort;
    private static HashMap<String, String> speedTypeShortMap;

    private static ArrayList<CodeBean> genders;
    private static HashMap<String, String> genderMap;

	private static ArrayList<CodeBean> orderStatuses;
    private static HashMap<String, String> orderStatusMap;

    private static ArrayList<CodeBean> placeTypes;
    private static HashMap<String, String> placeTypeMap;

    private static ArrayList<CodeBean> paymentTypes;
    private static HashMap<String, String> paymentTypeMap;

    private static ArrayList<CodeBean> products;
    private static HashMap<String, String> productMap;

    private static Context mContext;
	private static Boolean isInitialized = false;
	
	public static void initCodes(Context context) {
		
		if (isInitialized) {
			return;
		}
		
		mContext = context;
		
		speedTypes = new ArrayList<CodeBean>();
        speedTypeMap = new HashMap<String, String>();
		
		CodeBean codeBean = new CodeBean();
		codeBean.setCode(Constant.SPEED_TYPE_REGULAR);
		codeBean.setLabel(mContext.getString(R.string.order_speed_type_regular));
		speedTypes.add(codeBean);

		codeBean = new CodeBean();
		codeBean.setCode(Constant.SPEED_TYPE_EXPRESS);
		codeBean.setLabel(mContext.getString(R.string.order_speed_type_express));
        speedTypes.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.SPEED_TYPE_DELUXE);
        codeBean.setLabel(mContext.getString(R.string.order_speed_type_deluxe));
        speedTypes.add(codeBean);

        for (CodeBean cb : speedTypes) {
            speedTypeMap.put(cb.getCode(), cb.getLabel());
        }

        speedTypesShort = new ArrayList<CodeBean>();
        speedTypeShortMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.SPEED_TYPE_REGULAR);
        codeBean.setLabel(mContext.getString(R.string.speed_type_regular));
        speedTypesShort.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.SPEED_TYPE_EXPRESS);
        codeBean.setLabel(mContext.getString(R.string.speed_type_express));
        speedTypesShort.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.SPEED_TYPE_DELUXE);
        codeBean.setLabel(mContext.getString(R.string.speed_type_deluxe));
        speedTypesShort.add(codeBean);

        for (CodeBean cb : speedTypesShort) {
            speedTypeShortMap.put(cb.getCode(), cb.getLabel());
        }

        genders = new ArrayList<CodeBean>();
        genderMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.GENDER_MALE);
        codeBean.setLabel(mContext.getString(R.string.gender_male));
        genders.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.GENDER_FEMALE);
        codeBean.setLabel(mContext.getString(R.string.gender_female));
        genders.add(codeBean);

        for (CodeBean cb : genders) {
            genderMap.put(cb.getCode(), cb.getLabel());
        }

        orderStatuses = new ArrayList<CodeBean>();
        orderStatusMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_NEW_ORDER);
        codeBean.setLabel(mContext.getString(R.string.order_status_new_order));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION);
        codeBean.setLabel(mContext.getString(R.string.order_status_assigned_for_collection));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS);
        codeBean.setLabel(mContext.getString(R.string.order_status_collection_in_progress));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_COLLECTED);
        codeBean.setLabel(mContext.getString(R.string.order_status_collected));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_CLEANING_IN_PROGRESS);
        codeBean.setLabel(mContext.getString(R.string.order_status_cleaning_in_progress));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_CLEANED);
        codeBean.setLabel(mContext.getString(R.string.order_status_cleaned));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY);
        codeBean.setLabel(mContext.getString(R.string.order_status_assigned_for_delivery));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_DELIVERY_IN_PROGRESS);
        codeBean.setLabel(mContext.getString(R.string.order_status_delivery_in_progress));
        orderStatuses.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.ORDER_STATUS_COMPLETED);
        codeBean.setLabel(mContext.getString(R.string.order_status_completed));
        orderStatuses.add(codeBean);

        for (CodeBean cb : orderStatuses) {
            orderStatusMap.put(cb.getCode(), cb.getLabel());
        }

        placeTypes = new ArrayList<CodeBean>();
        placeTypeMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PLACE_TYPE_HOME);
        codeBean.setLabel(mContext.getString(R.string.place_type_home));
        placeTypes.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PLACE_TYPE_APARTMENT);
        codeBean.setLabel(mContext.getString(R.string.place_type_apartment));
        placeTypes.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PLACE_TYPE_OFFICE);
        codeBean.setLabel(mContext.getString(R.string.place_type_office));
        placeTypes.add(codeBean);

        for (CodeBean cb : placeTypes) {
            placeTypeMap.put(cb.getCode(), cb.getLabel());
        }

        paymentTypes = new ArrayList<CodeBean>();
        paymentTypeMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PAYMENT_TYPE_CASH);
        codeBean.setLabel(mContext.getString(R.string.payment_type_cash));
        paymentTypes.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PAYMENT_TYPE_WALLET);
        codeBean.setLabel(mContext.getString(R.string.payment_type_wallet));
        paymentTypes.add(codeBean);

        for (CodeBean cb : paymentTypes) {
            paymentTypeMap.put(cb.getCode(), cb.getLabel());
        }

        products = new ArrayList<CodeBean>();
        productMap = new HashMap<String, String>();

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PRODUCT_KG_WASH_IRON);
        codeBean.setLabel(mContext.getString(R.string.product_type_kilogram));
        products.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PRODUCT_ITEM_WASH_IRON);
        codeBean.setLabel(mContext.getString(R.string.product_type_piece));
        products.add(codeBean);

        codeBean = new CodeBean();
        codeBean.setCode(Constant.PRODUCT_DRY_CLEAN);
        codeBean.setLabel(mContext.getString(R.string.product_type_dryclean));
        products.add(codeBean);

        for (CodeBean cb : products) {
            productMap.put(cb.getCode(), cb.getLabel());
        }

		isInitialized = true;
	}
	
	public static List<CodeBean> getSpeedTypes() {
		return speedTypes;
	}

	public static String getSpeedTypeLabel(String code) {

        return speedTypeMap.get(code);
	}

    public static List<CodeBean> getSpeedTypesShort() {
        return speedTypesShort;
    }

    public static String getSpeedTypeShortLabel(String code) {

        return speedTypeShortMap.get(code);
    }

    public static List<CodeBean> getGenders() {
        return genders;
    }

    public static String getGenderLabel(String code) {

        return genderMap.get(code);
    }

    public static List<CodeBean> getOrderStatuses() {
        return orderStatuses;
    }

    public static String getOrderStatusLabel(String code) {

        return orderStatusMap.get(code);
    }

    public static List<CodeBean> getPlaceTypes() {
        return placeTypes;
    }

    public static String getPlaceTypeLabel(String code) {

        return placeTypeMap.get(code);
    }

    public static List<CodeBean> getPaymentTypes() {
        return paymentTypes;
    }

    public static String getPaymentTypeLabel(String code) {

        return paymentTypeMap.get(code);
    }

    public static List<CodeBean> getProducts() {
        return products;
    }

    public static String getProductLabel(String code) {

        return productMap.get(code);
    }
}
