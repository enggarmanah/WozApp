package id.urbanwash.wozapp.listener;

import id.urbanwash.wozapp.model.PlaceBean;

/**
 * Created by apridosandyasa on 4/11/16.
 */
public interface NewOrderListener {

    void onOrderCompleted();

    void onOrderCancelled();

    void onDeliveryDateUpdated();

    void onChangePlace(PlaceBean placeBean);
}
