package id.urbanwash.wozapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Radix on 19/2/2016.
 */
public class OrderProductBean {

    Long id;
    OrderBean order;
    ProductBean product;

    List<OrderProductItemBean> orderProductItems = new ArrayList<OrderProductItemBean>();

    float count;
    float charge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public List<OrderProductItemBean> getOrderProductItems() {
        return orderProductItems;
    }

    public void setOrderProductItems(List<OrderProductItemBean> orderProductItems) {
        this.orderProductItems = orderProductItems;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }
}
