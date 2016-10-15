package id.urbanwash.wozapp.model;

/**
 * Created by apridosandyasa on 4/24/16.
 */
public class SearchAddress {
    private String address;
    private double latitude;
    private double longitude;

    public SearchAddress() {

    }

    public SearchAddress(String a, double lat, double lng) {
        this.address = a;
        this.latitude = lat;
        this.longitude = lng;
    }

    public void setAddress(String a) {
        this.address = a;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double lng) {
        this.longitude = lng;
    }

    public String getAddress() {
        return this.address;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
