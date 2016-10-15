package id.urbanwash.wozapp.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResponseBean {

    public static final String SUCCESS = "SUC";
    public static final String ERROR = "ERR";

    protected String respCode;
    protected String respDescription;
    protected Date respDate;

    protected String securityKey;

    protected CustomerBean customer;
    protected EmployeeBean employee;
    protected OrderBean order;
    protected CreditBean credit;
    protected ImageBean image;
    protected PromoBean promo;
    protected MessageBean message;

    protected Integer pendingCreditCount;
    protected Float minimumOrder;

    protected List<OrderBean> orders;
    protected List<OrderBean> outstandingOrders;
    protected List<OrderBean> completedOrders;
    protected List<ProductBean> products;
    protected List<EmployeeBean> employees;
    protected List<CustomerBean> customers;
    protected List<PlaceBean> places;
    protected List<CreditBean> credits;
    protected List<PromoBean> promos;
    protected List<MessageBean> messages;
    protected List<ReportBean> reports;
    protected Map<Date, List<Long>> collectionDateTimes;
    protected Map<Date, List<Long>> deliveryDateTimes;
    protected Map<String, Long> orderSummary;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDescription() {
        return respDescription;
    }

    public void setRespDescription(String respDescription) {
        this.respDescription = respDescription;
    }

    public Date getRespDate() {
        return respDate;
    }

    public void setRespDate(Date respDate) {
        this.respDate = respDate;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public EmployeeBean getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeBean employee) {
        this.employee = employee;
    }

    public CreditBean getCredit() {
        return credit;
    }

    public void setCredit(CreditBean credit) {
        this.credit = credit;
    }

    public PromoBean getPromo() {
        return promo;
    }

    public void setPromo(PromoBean promo) {
        this.promo = promo;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public List<PlaceBean> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceBean> places) {
        this.places = places;
    }

    public List<CreditBean> getCredits() {
        return credits;
    }

    public void setCredits(List<CreditBean> credits) {
        this.credits = credits;
    }

    public List<OrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderBean> orders) {
        this.orders = orders;
    }

    public List<OrderBean> getOutstandingOrders() {
        return outstandingOrders;
    }

    public void setOutstandingOrders(List<OrderBean> outstandingOrders) {
        this.outstandingOrders = outstandingOrders;
    }

    public List<OrderBean> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<OrderBean> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

    public List<PromoBean> getPromos() {
        return promos;
    }

    public void setPromos(List<PromoBean> promos) {
        this.promos = promos;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<EmployeeBean> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeBean> employees) {
        this.employees = employees;
    }

    public List<CustomerBean> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerBean> customers) {
        this.customers = customers;
    }

    public List<MessageBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageBean> messages) {
        this.messages = messages;
    }

    public List<ReportBean> getReports() {
        return reports;
    }

    public void setReports(List<ReportBean> reports) {
        this.reports = reports;
    }

    public Map<String, Long> getOrderSummary() {
        return orderSummary;
    }

    public void setOrderSummary(Map<String, Long> orderSummary) {
        this.orderSummary = orderSummary;
    }

    public Map<Date, List<Long>> getCollectionDateTimes() {
        return collectionDateTimes;
    }

    public void setCollectionDateTimes(Map<Date, List<Long>> collectionDateTimes) {
        this.collectionDateTimes = collectionDateTimes;
    }

    public Map<Date, List<Long>> getDeliveryDateTimes() {
        return deliveryDateTimes;
    }

    public void setDeliveryDateTimes(Map<Date, List<Long>> deliveryDateTimes) {
        this.deliveryDateTimes = deliveryDateTimes;
    }

    public Integer getPendingCreditCount() {
        return pendingCreditCount;
    }

    public void setPendingCreditCount(Integer pendingCreditCount) {
        this.pendingCreditCount = pendingCreditCount;
    }

    public Float getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(Float minimumOrder) {
        this.minimumOrder = minimumOrder;
    }
}
