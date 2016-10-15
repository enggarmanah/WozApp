package id.urbanwash.wozapp.listener;

import id.urbanwash.wozapp.model.ImageBean;
import id.urbanwash.wozapp.model.OrderBean;

/**
 * Created by apridosandyasa on 4/11/16.
 */
public interface OrderTrackingListener {

    void onCancelOrder(OrderBean orderBean);

    void onChangeOrderDeliveryDate(OrderBean orderBean);

    void onDeliveryDateUpdated();

    void onChangePaymentType(String paymentType);

    void onCall(String name, ImageBean image, String mobile);

    void onSms(String name, ImageBean image, String mobile);
}
