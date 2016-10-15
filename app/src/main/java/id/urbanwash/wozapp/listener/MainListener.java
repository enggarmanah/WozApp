package id.urbanwash.wozapp.listener;

import id.urbanwash.wozapp.model.PlaceBean;

/**
 * Created by apridosandyasa on 4/9/16.
 */
public interface MainListener {

    void refreshOrderSummary();

    void refreshPendingCreditCount();

    void ShowProfileFromMainView();

    void onProcessOrder(String status);

    void onSearchOrder();

    void onRefreshOrders();

    void onUpdateProfile();

    void onDeletePlace(PlaceBean placeBean);

    void onLogout();
}
