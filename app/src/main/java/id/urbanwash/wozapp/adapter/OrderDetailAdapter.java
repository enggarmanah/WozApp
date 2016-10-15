package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;
import id.urbanwash.wozapp.model.OrderProductItemBean;
import id.urbanwash.wozapp.util.CodeUtil;

/**
 * Created by apridosandyasa on 4/11/16.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderCompleteViewHolder> {
    private AppCompatActivity mAppCompatActivity;

    private OrderBean mOrderBean;
    private List<OrderProductItemBean> mOrderProductItemBeans = new ArrayList<OrderProductItemBean>();
    
    public OrderDetailAdapter(AppCompatActivity appCompatActivity, OrderBean orderBean) {
        
        mAppCompatActivity = appCompatActivity;
        mOrderBean = orderBean;
        
        refreshDataSet();
    }

    @Override
    public OrderCompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_item,
                parent, false);

        OrderCompleteViewHolder orderCompleteViewHolder = new OrderCompleteViewHolder(view);
        return orderCompleteViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderCompleteViewHolder holder, int position) {
        
        OrderProductItemBean orderProductItemBean = mOrderProductItemBeans.get(position);

        holder.productLabel.setText(orderProductItemBean.getProductType());
        holder.descriptionLabel.setText(orderProductItemBean.getLaundryItem().getName());
        holder.countLabel.setText("x " + orderProductItemBean.getCount());
    }

    @Override
    public int getItemCount() {
        return mOrderProductItemBeans.size();
    }

    public static class OrderCompleteViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView productLabel;
        private AppCompatTextView descriptionLabel;
        private AppCompatTextView countLabel;

        OrderCompleteViewHolder(View view) {

            super(view);

            productLabel = (AppCompatTextView) view.findViewById(R.id.label_product);
            descriptionLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            countLabel = (AppCompatTextView) view.findViewById(R.id.label_count_new_order);
        }
    }

    public void refreshDataSet() {

        mOrderProductItemBeans.clear();

        for (OrderProductBean orderProductBean : mOrderBean.getOrderProducts()) {

            for (OrderProductItemBean orderProductItemBean : orderProductBean.getOrderProductItems()) {

                String productType = CodeUtil.getProductLabel(orderProductBean.getProduct().getCode());

                orderProductItemBean.setProductType(productType);
                mOrderProductItemBeans.add(orderProductItemBean);
            }
        }

        super.notifyDataSetChanged();
    }
}
