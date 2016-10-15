package id.urbanwash.wozapp.model;

import java.util.List;

/**
 * Created by Radix on 17/2/2016.
 */
public class CustomerBean extends BaseBean {

    List<PlaceBean> places;

    String name;
    String title;
    String mobile;
    String email;
    String password;
    String newPassword;
    String changePassword;
    String verificationCode;
    Float totalCredits;

    ImageBean image;
    TokenBean token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public TokenBean getToken() {
        return token;
    }

    public void setToken(TokenBean token) {
        this.token = token;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Float getTotalCredits() {

        if (totalCredits != null) {
            return totalCredits;
        } else {
            return 0f;
        }
    }

    public void setTotalCredits(Float totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<PlaceBean> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceBean> places) {
        this.places = places;
    }
}