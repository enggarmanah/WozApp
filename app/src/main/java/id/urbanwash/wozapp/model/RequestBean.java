
package id.urbanwash.wozapp.model;

import android.os.Message;

import java.util.Date;

public class RequestBean {

    protected String securityKey;
    protected String deviceId;
    protected String deviceType;
    protected String certDn;
    protected Integer appVersion;
    protected String task;
    protected String locale;

    protected String email;
    protected String password;

    protected CustomerBean customer;
    protected EmployeeBean employee;
    protected OrderBean order;
    protected PlaceBean place;
    protected ServiceAreaBean serviceArea;
    protected CreditBean credit;
    protected ImageBean image;
    protected PromoBean promo;
    protected MessageBean message;

    protected String search;
    protected String orderStatus;
    protected Date date;
    protected Integer page;

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCertDn() {
        return certDn;
    }

    public void setCertDn(String certDn) {
        this.certDn = certDn;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PlaceBean getPlace() {
        return place;
    }

    public void setPlace(PlaceBean place) {
        this.place = place;
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

    public ServiceAreaBean getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(ServiceAreaBean serviceArea) {
        this.serviceArea = serviceArea;
    }

    public CreditBean getCredit() {
        return credit;
    }

    public void setCredit(CreditBean credit) {
        this.credit = credit;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
