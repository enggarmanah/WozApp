package id.urbanwash.wozapp.model;

/**
 * Created by Radix on 11/2/2016.
 */
public class PlaceBean extends BaseBean {

    CustomerBean customer;
    ServiceAreaBean serviceArea;

    String type;
    String name;
    String address;

    Double lat;
    Double lng;

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public ServiceAreaBean getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(ServiceAreaBean serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
