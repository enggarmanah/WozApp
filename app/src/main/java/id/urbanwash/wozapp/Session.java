package id.urbanwash.wozapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;
import id.urbanwash.wozapp.model.MessageBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;
import id.urbanwash.wozapp.model.PlaceBean;
import id.urbanwash.wozapp.model.ProductBean;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.PlaceManager;
import id.urbanwash.wozapp.util.ProductManager;

/**
 * Created by Radix on 14/2/2016.
 */
public class Session {

    private static OrderBean order;
    private static CustomerBean customer;
    private static EmployeeBean employee;
    private static Context context;
    private static PlaceBean place;
    private static EmployeeBean pic;
    private static CreditBean credit;

    private static Integer pendingCreditCount;
    private static Float minimumOrder;

    private static List<OrderBean> orders;
    private static List<OrderBean> outstandingOrders;
    private static List<OrderBean> completedOrders;
    private static List<PlaceBean> places;
    private static List<CreditBean> credits;
    private static List<ProductBean> products;
    private static List<EmployeeBean> pics;
    private static List<PromoBean> promos;
    private static List<MessageBean> messages;
    private static List<ReportBean> reports;
    private static Map<String, Long> orderSummary;

    private static Map<Date, List<Long>> collectionDateTimes;
    private static Map<Date, List<Long>> deliveryDateTimes;

    private static String deviceId;

    public static void setContext(Context context) {

        Session.context = context;
    }

    public static CustomerBean getCustomer() {

        if (customer == null) {

            customer = Installation.getCustomerProfile(context);

            if (customer != null) {
                places = customer.getPlaces();
            }
        }

        return customer;
    }

    public static void setCustomer(CustomerBean customer) {

        Session.customer = customer;
    }

    public static EmployeeBean getEmployee() {

        if (employee == null) {

            employee = Installation.getEmployeeProfile(context);
        }

        return employee;
    }

    public static void setEmployee(EmployeeBean employeeBean) {

        Session.employee = employeeBean;
    }

    public static OrderBean getOrder() {

        return order;
    }

    public static void setOrder(OrderBean orderBean) {
        Session.order = orderBean;
    }

    public static CreditBean getCredit() {
        return credit;
    }

    public static void setCredit(CreditBean credit) {
        Session.credit = credit;
    }

    public static PlaceBean getPlace() {
        return place;
    }

    public static void setPlace(PlaceBean place) {
        Session.place = place;
    }

    public static OrderProductBean getOrderProduct(String productCode) {

        List<OrderProductBean> orderProductBeen = getOrder().getOrderProducts();
        OrderProductBean orderProductBean = null;

        for (OrderProductBean op : orderProductBeen) {
            if (op.getProduct().getCode().equals(productCode)) {
                orderProductBean = op;
                break;
            }
        }

        return orderProductBean;
    }

    public static List<OrderBean> getOutstandingOrders() {
        return outstandingOrders;
    }

    public static void setOutstandingOrders(List<OrderBean> outstandingOrders) {
        Session.outstandingOrders = outstandingOrders;
    }

    public static List<OrderBean> getOrders() {
        return orders;
    }

    public static void setOrders(List<OrderBean> orders) {
        Session.orders = orders;
    }

    public static List<OrderBean> getCompletedOrders() {
        return completedOrders;
    }

    public static void setCompletedOrders(List<OrderBean> completedOrders) {
        Session.completedOrders = completedOrders;
    }

    public static PlaceBean getDefaultPlace() {

        PlaceBean defaultPlace = null;

        if (getPlaces() != null && getPlaces().size() > 0) {
            defaultPlace = getPlaces().get(0);
        }

        return defaultPlace;
    }

    public static List<PlaceBean> getPlaces() {

        if (places == null) {

            PlaceManager placeManager = new PlaceManager(context);
            places = placeManager.getPlaces();
        }

        return places;
    }

    public static void setPlaces(List<PlaceBean> places) {
        Session.places = places;

        PlaceManager placeManager = new PlaceManager(context);
        placeManager.savePlaces(places);
    }

    public static List<CreditBean> getCredits() {
        return credits != null ? credits : new ArrayList<CreditBean>();
    }

    public static void setCredits(List<CreditBean> credits) {
        Session.credits = credits;
    }

    public static List<PromoBean> getPromos() {
        return promos != null ? promos : new ArrayList<PromoBean>();
    }

    public static void setPromos(List<PromoBean> promos) {
        Session.promos = promos;
    }

    public static List<MessageBean> getMessages() {
        return messages;
    }

    public static void setMessages(List<MessageBean> messages) {
        Session.messages = messages;
    }

    public static List<ReportBean> getReports() {
        return reports;
    }

    public static void setReports(List<ReportBean> reports) {
        Session.reports = reports;
    }

    public static List<ProductBean> getProducts() {

        if (products == null) {

            ProductManager productManager = new ProductManager(context);
            products = productManager.getProducts();
        }

        return products;
    }

    public static ProductBean getProduct(String code) {

        products = getProducts();

        if (products != null) {

            for (ProductBean product : products) {
                if (product.getCode().equals(code)) {
                    return product;
                }
            }
        }

        return null;
    }

    public static void setProducts(List<ProductBean> mProductBeans) {
        Session.products = mProductBeans;

        ProductManager productManager = new ProductManager(context);
        productManager.saveProducts(mProductBeans);
    }

    public static List<EmployeeBean> getPics() {
        return pics;
    }

    public static void setPics(List<EmployeeBean> pics) {
        Session.pics = pics;
    }

    public static void clear() {

        order = null;
        customer = null;
        employee = null;

        outstandingOrders = null;
        places = null;
        products = null;
        pics = null;

        Installation.saveCustomerProfile(context, customer);
        Installation.saveEmployeeProfile(context, employee);
    }

    public static String getDeviceId() {

        return deviceId;
    }

    public static void setDeviceId(String deviceId) {

        Session.deviceId = deviceId;
    }

    public static String getSecurityKey() {

        String securityKey = null;

        if (customer != null && customer.getToken() != null) {
            securityKey = customer.getToken().getSecurityKey();
        }

        return securityKey;
    }

    public static Map<String, Long> getOrderSummary() {
        return orderSummary != null ? orderSummary : new HashMap<String, Long>();
    }

    public static void setOrderSummary(Map<String, Long> orderSummary) {
        Session.orderSummary = orderSummary;
    }

    public static EmployeeBean getPic() {
        return pic;
    }

    public static void setPic(EmployeeBean pic) {
        Session.pic = pic;
    }

    public static boolean isCustomer() {

        if (getCustomer() != null && getEmployee() == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAdminUser() {

        if (getEmployee() != null && Constant.EMPLOYEE_TYPE_ADMIN.equals(getEmployee().getType())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTransporterUser() {

        if (getEmployee() != null && Constant.EMPLOYEE_TYPE_TRANSPORTER.equals(getEmployee().getType())) {
            return true;
        } else {
            return false;
        }
    }

    public static Map<Date, List<Long>> getCollectionDateTimes() {
        return collectionDateTimes;
    }

    public static void setCollectionDateTimes(Map<Date, List<Long>> collectionDateTimes) {
        Session.collectionDateTimes = collectionDateTimes;
    }

    public static Map<Date, List<Long>> getDeliveryDateTimes() {
        return deliveryDateTimes;
    }

    public static void setDeliveryDateTimes(Map<Date, List<Long>> deliveryDateTimes) {
        Session.deliveryDateTimes = deliveryDateTimes;
    }

    public static Integer getPendingCreditCount() {
        return pendingCreditCount;
    }

    public static void setPendingCreditCount(Integer pendingCreditCount) {
        Session.pendingCreditCount = pendingCreditCount;
    }

    public static Float getMinimumOrder() {
        return minimumOrder;
    }

    public static void setMinimumOrder(Float minimumOrder) {
        Session.minimumOrder = minimumOrder;
    }
}