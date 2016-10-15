package id.urbanwash.wozapp.listener;

import id.urbanwash.wozapp.model.OrderBean;

/**
 * Created by apridosandyasa on 4/12/16.
 */
public interface ProcessOrderListener {

    void onOrderSelected(OrderBean orderBean);

    void onDeliveryDateUpdated();
}
