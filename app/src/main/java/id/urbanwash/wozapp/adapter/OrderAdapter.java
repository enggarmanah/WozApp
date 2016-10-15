package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.MyOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private int viewMode;
    private MyOrderListener myOrderListener;

    private List<OrderBean> mOrders;

    public OrderAdapter(AppCompatActivity appCompatActivity, int mode, MyOrderListener listener, List<OrderBean> orders) {

        mAppCompatActivity = appCompatActivity;
        viewMode = mode;
        myOrderListener = listener;

        mOrders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return viewMode;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setupOrderViewHolder((OrderViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;

        private AppCompatTextView orderNoLabel;
        private AppCompatTextView collectionDayLabel;
        private AppCompatTextView collectionDateLabel;
        private AppCompatTextView collectionMonthLabel;
        private AppCompatTextView collectionTimeLabel;
        private AppCompatTextView deliveryDayLabel;
        private AppCompatTextView deliveryDateLabel;
        private AppCompatTextView deliveryMonthLabel;
        private AppCompatTextView deliveryTimeLabel;
        private AppCompatTextView placeNameLabel;
        private AppCompatTextView placeAddressLabel;
        private AppCompatTextView serviceLabel;
        private AppCompatTextView statusLabel;
        private AppCompatTextView speedTypeLabel;
        private AppCompatTextView serviceChargeLabel;

        OrderViewHolder(View view) {

            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            orderNoLabel = (AppCompatTextView) view.findViewById(R.id.label_order_no);
            collectionDayLabel = (AppCompatTextView) view.findViewById(R.id.label_collection_day);
            collectionDateLabel = (AppCompatTextView) view.findViewById(R.id.label_collection_date);
            collectionMonthLabel = (AppCompatTextView) view.findViewById(R.id.label_collection_month);
            collectionTimeLabel = (AppCompatTextView) view.findViewById(R.id.label_collection_time);
            deliveryDayLabel = (AppCompatTextView) view.findViewById(R.id.label_delivery_day);
            deliveryDateLabel = (AppCompatTextView) view.findViewById(R.id.label_delivery_date);
            deliveryMonthLabel = (AppCompatTextView) view.findViewById(R.id.label_delivery_month);
            deliveryTimeLabel = (AppCompatTextView) view.findViewById(R.id.label_delivery_time);
            placeNameLabel = (AppCompatTextView) view.findViewById(R.id.label_place_name);
            placeAddressLabel = (AppCompatTextView) view.findViewById(R.id.label_place_address);
            serviceLabel = (AppCompatTextView) view.findViewById(R.id.label_service);
            statusLabel = (AppCompatTextView) view.findViewById(R.id.label_status);
            speedTypeLabel = (AppCompatTextView) view.findViewById(R.id.label_speed_type);
            serviceChargeLabel = (AppCompatTextView) view.findViewById(R.id.label_service_charge);
        }
    }

    private void setupOrderViewHolder(OrderViewHolder holder, int position) {

        OrderBean orderBean = mOrders.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(orderBean));

        holder.orderNoLabel.setText("# " + orderBean.getOrderNo());

        String day = CommonUtil.formatDay(orderBean.getCollectionDate()) + ",";
        String date = CommonUtil.formatDateOfMonth(orderBean.getCollectionDate());
        String month = CommonUtil.formatMonth(orderBean.getCollectionDate());
        String time = CommonUtil.formatOperatingHrTimePeriod(orderBean.getCollectionDate());

        holder.collectionDayLabel.setText(day);
        holder.collectionDateLabel.setText(date);
        holder.collectionMonthLabel.setText(month);
        holder.collectionTimeLabel.setText(time);

        day = CommonUtil.formatDay(orderBean.getDeliveryDate()) + ",";
        date = CommonUtil.formatDateOfMonth(orderBean.getDeliveryDate());
        month = CommonUtil.formatMonth(orderBean.getDeliveryDate());
        time = CommonUtil.formatOperatingHrTimePeriod(orderBean.getDeliveryDate());

        holder.deliveryDayLabel.setText(day);
        holder.deliveryDateLabel.setText(date);
        holder.deliveryMonthLabel.setText(month);
        holder.deliveryTimeLabel.setText(time);

        holder.placeNameLabel.setVisibility(View.GONE);
        holder.placeAddressLabel.setVisibility(View.GONE);
        holder.serviceLabel.setVisibility(View.GONE);

        holder.serviceLabel.setText(CommonUtil.getOrderProductInfo(orderBean));

        if (!Constant.ORDER_STATUS_COMPLETED.equals(orderBean.getStatus())) {
            holder.statusLabel.setText(CodeUtil.getOrderStatusLabel(orderBean.getStatus()));
            holder.statusLabel.setVisibility(View.VISIBLE);
        } else {
            holder.statusLabel.setVisibility(View.GONE);
        }

        holder.speedTypeLabel.setText(CodeUtil.getSpeedTypeLabel(orderBean.getSpeedType()));
        holder.serviceChargeLabel.setText(CommonUtil.formatCurrency(orderBean.getTotalCharge()));
    }

    private View.OnClickListener getMainPanelOnClickListener(final OrderBean orderBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrderListener.onShowOrderDetail(orderBean);
            }
        };
    }
}
