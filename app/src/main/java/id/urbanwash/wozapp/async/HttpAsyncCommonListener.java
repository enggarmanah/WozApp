package id.urbanwash.wozapp.async;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

public interface HttpAsyncCommonListener {
	
	public void onAsyncTimeOut();
	
	public void onAsyncCompleted();
	
	public void onAsyncError(String message);

    public void onAsyncAuthenticate(CustomerBean customerBean);

    public void onAsyncRegisterCustomer(CustomerBean customerBean);

    public void onAsyncCheckEmail(CustomerBean customerBean);

    public void onAsyncVerifyMobile(CustomerBean customerBean);

    public void onAsyncVerifyMobile(EmployeeBean employeeBeanBean);

    public void onAsyncCancelOrder(List<OrderBean> orders);

    public void onAsyncUpdateOrderStatus(OrderBean orderBean);

    public void onAsyncUpdateOrderPaymentType(OrderBean orderBean);

    public void onAsyncUpdateOrderDeliveryDate(OrderBean orderBean);

    public void onAsyncRepeatOrder();

    public void onAsyncGetOutstandingOrders(List<OrderBean> orders);

    public void onAsyncGetCompletedOrders(List<OrderBean> orders);

    public void onAsyncGetOrders(List<OrderBean> orders);

    public void onAsyncGetCustomers(List<CustomerBean> customers);

    public void onAsyncGetProducts(List<ProductBean> products);

    public void onAsyncGetPromo(PromoBean promo);

    public void onAsyncGetPromos(List<PromoBean> promos);

    public void onAsyncGetMessages(List<MessageBean> messages);

    public void onAsyncGetReports(List<ReportBean> reports);

    public void onAsyncGetDeliveryTimes(Map<Date, List<Long>> deliveryDateTimes);

    public void onAsyncGetPics(List<EmployeeBean> employees);

    public void onAsyncGetPlaces(List<PlaceBean> places);

    public void onAsyncGetCredits(List<CreditBean> credits);

    public void onAsyncLogout();
}
