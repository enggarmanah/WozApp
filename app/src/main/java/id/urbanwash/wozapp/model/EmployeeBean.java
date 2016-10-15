package id.urbanwash.wozapp.model;

public class EmployeeBean extends BaseBean {

	ImageBean image;

	String name;
	String mobile;
    String email;
    String password;
    String newPassword;
	String changePassword;
    String type;
	String status;
	String verificationCode;

    int assignedOrderCount;

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

    public int getAssignedOrderCount() {
        return assignedOrderCount;
    }

    public void setAssignedOrderCount(int assignedOrderCount) {
        this.assignedOrderCount = assignedOrderCount;
    }

    public EmployeeBean clone() {

		EmployeeBean employeeBean = new EmployeeBean();
		employeeBean.setBean(this);

		employeeBean.setImage(image);
		employeeBean.setName(name);
        employeeBean.setEmail(email);
        employeeBean.setPassword(password);
        employeeBean.setType(type);
		employeeBean.setMobile(mobile);
		employeeBean.setStatus(status);
		employeeBean.setVerificationCode(verificationCode);

		return employeeBean;
	}
}
