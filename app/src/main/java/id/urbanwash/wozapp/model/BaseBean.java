package id.urbanwash.wozapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Radix on 17/2/2016.
 */
public class BaseBean implements Serializable {

    protected Long id;

    protected String createBy = "SYSTEM";
    protected Date createDate = new Date();
    protected String updateBy = "SYSTEM";
    protected Date updateDate = new Date();

    public void setBean(BaseBean bean) {

        this.id = bean.getId();
        this.createBy = bean.getCreateBy();
        this.createDate = bean.getCreateDate();
        this.updateBy = bean.getUpdateBy();
        this.updateDate = bean.getUpdateDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;

        if (createBy == null) {
            createBy = updateBy;
        }
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;

        if (createDate == null) {
            createDate = updateDate;
        }
    }
}