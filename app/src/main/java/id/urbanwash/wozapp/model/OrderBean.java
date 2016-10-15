package id.urbanwash.wozapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by Radix on 11/2/2016.
 */
public class OrderBean extends BaseBean {

    CustomerBean customer;
    EmployeeBean collectionPic;
    EmployeeBean deliveryPic;
    ServiceAreaBean serviceArea;
    PromoBean promo;

    List<OrderProductBean> orderProducts = new ArrayList<OrderProductBean>();

    String orderNo;

    String placeType;
    String placeName;
    String placeAddress;
    String remarks;

    double lat;
    double lng;

    Date orderDate;
    Date collectionDate;
    Date deliveryDate;
    Date completedDate;

    String speedType; //Constant.SPEED_TYPE_REGULAR;
    String paymentType = Constant.PAYMENT_TYPE_CASH;

    float discount;
    float totalCharge;

    int rescheduleCount;

    String note;
    String status;

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public EmployeeBean getCollectionPic() {
        return collectionPic;
    }

    public void setCollectionPic(EmployeeBean collectionPic) {
        this.collectionPic = collectionPic;
    }

    public EmployeeBean getDeliveryPic() {
        return deliveryPic;
    }

    public void setDeliveryPic(EmployeeBean deliveryPic) {
        this.deliveryPic = deliveryPic;
    }

    public ServiceAreaBean getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(ServiceAreaBean serviceArea) {
        this.serviceArea = serviceArea;
    }

    public List<OrderProductBean> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductBean> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderProductBean getOrderProduct(String productCode) {

        if (orderProducts != null) {

            for (OrderProductBean orderProductBean : orderProducts) {

                if (orderProductBean.getProduct().getCode().equals(productCode)) {

                    return orderProductBean;
                }
            }
        }

        return null;
    }

    public List<OrderProductItemBean> getOrderProductItems() {

        List<OrderProductItemBean> orderProductItemBeans = new ArrayList<OrderProductItemBean>();

        if (orderProducts != null) {

            for (OrderProductBean orderProductBean : orderProducts) {

                if (orderProductBean.getOrderProductItems() != null) {
                    orderProductItemBeans.addAll(orderProductBean.getOrderProductItems());
                }
            }
        }

        return orderProductItemBeans;
    }

    public void refreshTotalCharge() {

        float tc = 0f;

        if (orderProducts != null) {

            for (OrderProductBean orderProductBean : orderProducts) {

                tc += orderProductBean.getCharge();
            }
        }

        totalCharge = tc;
    }

    public boolean isEmptyOrder() {

        OrderProductBean orderProductKg = getOrderProduct(Constant.PRODUCT_KG_WASH_IRON);
        OrderProductBean orderProductPiece = getOrderProduct(Constant.PRODUCT_ITEM_WASH_IRON);
        OrderProductBean orderProductDryClean = getOrderProduct(Constant.PRODUCT_DRY_CLEAN);

        return !(orderProductKg != null && orderProductKg.getCount() > 0) &&
                !(orderProductPiece != null && orderProductPiece.getCharge() > 0) &&
                !(orderProductDryClean != null && orderProductDryClean.getCharge() > 0);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceInfo() {
        return (!CommonUtil.isEmpty(getPlaceName()) ? getPlaceName() + Constant.NEW_LINE_STRING : Constant.EMPTY_STRING) +
                getPlaceAddress();
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getSpeedType() {
        return speedType;
    }

    public void setSpeedType(String speedType) {
        this.speedType = speedType;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(float totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PromoBean getPromo() {
        return promo;
    }

    public void setPromo(PromoBean promo) {
        this.promo = promo;
    }

    public int getRescheduleCount() {
        return rescheduleCount;
    }

    public void setRescheduleCount(int rescheduleCount) {
        this.rescheduleCount = rescheduleCount;
    }
}
