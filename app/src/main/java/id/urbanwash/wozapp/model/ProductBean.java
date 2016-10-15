package id.urbanwash.wozapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Radix on 1/2/2016.
 */
public class ProductBean extends BaseBean {

    List<LaundryItemBean> laundryItems;

    @JsonIgnore
    Map<String, LaundryItemBean> laundryItemMap;

    Long id;
    String code;
    String name;
    String description;
    String type;
    float price1;
    float price2;
    float price3;

    public List<LaundryItemBean> getLaundryItems() {
        return laundryItems;
    }

    public void setLaundryItems(List<LaundryItemBean> laundryItems) {
        this.laundryItems = laundryItems;

        if (laundryItems != null) {
            laundryItemMap = new HashMap<String, LaundryItemBean>();
            for (LaundryItemBean laundryItemBean : laundryItems) {
                laundryItemMap.put(laundryItemBean.getCode(), laundryItemBean);
            }
        }
    }

    public LaundryItemBean getLaundryItem(String code) {

        return laundryItemMap.get(code);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice1() {
        return price1;
    }

    public void setPrice1(float price1) {
        this.price1 = price1;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }
}
