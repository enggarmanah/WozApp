package id.urbanwash.wozapp.model;

/**
 * Created by Radix on 17/2/2016.
 */
public class OrderProductItemBean extends BaseBean {

    Long id;

    OrderProductBean orderProduct;
    LaundryItemBean laundryItem;
    String productType;

    int count = 0;
    float charge = 0;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public OrderProductBean getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProductBean orderProduct) {
        this.orderProduct = orderProduct;
    }

    public LaundryItemBean getLaundryItem() {
        return laundryItem;
    }

    public void setLaundryItem(LaundryItemBean laundryItem) {
        this.laundryItem = laundryItem;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
