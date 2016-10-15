package id.urbanwash.wozapp.adapter;

import android.support.v4.content.ContextCompat;
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
import id.urbanwash.wozapp.listener.ProcessOrderListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class ManageOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private ProcessOrderListener mProcessOrderListener;
    private boolean mIsStatusRequired;

    private List<OrderBean> mOrders;

    public ManageOrderAdapter(AppCompatActivity appCompatActivity, List<OrderBean> orders, boolean isStatusRequired) {

        this.mAppCompatActivity = appCompatActivity;
        mProcessOrderListener = (ProcessOrderListener) this.mAppCompatActivity;
        mIsStatusRequired = isStatusRequired;

        mOrders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
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

        if (!Constant.ORDER_STATUS_COMPLETED.equals(orderBean.getStatus()) &&
            (Constant.ORDER_STATUS_NEW_ORDER.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTION_IN_PROGRESS.equals(orderBean.getStatus())||
            Constant.ORDER_STATUS_COLLECTED.equals(orderBean.getStatus()))) {

            holder.collectionDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.collectionDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.collectionMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.collectionTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomDarkGreen));

            holder.deliveryDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.deliveryDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.deliveryMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.deliveryTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));

        } else if (!Constant.ORDER_STATUS_COMPLETED.equals(orderBean.getStatus())) {
            holder.collectionDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.collectionDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.collectionMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));
            holder.collectionTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorLightGrey));

            holder.deliveryDayLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.deliveryDateLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.deliveryMonthLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.deliveryTimeLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomDarkGreen));
        }

        if (!CommonUtil.isEmpty(orderBean.getPlaceName())) {
            holder.placeNameLabel.setText(orderBean.getPlaceName());
            holder.placeNameLabel.setVisibility(View.VISIBLE);
        } else {
            holder.placeNameLabel.setVisibility(View.GONE);
        }

        holder.placeAddressLabel.setText(orderBean.getPlaceAddress());
        holder.placeAddressLabel.setVisibility(View.VISIBLE);

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTED.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANED.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_COMPLETED.equals(orderBean.getStatus())) {

            holder.placeNameLabel.setVisibility(View.GONE);
            holder.placeAddressLabel.setVisibility(View.GONE);
        }

        holder.serviceLabel.setText(CommonUtil.getOrderProductInfo(orderBean));

        if (mIsStatusRequired) {
            holder.statusLabel.setText(CodeUtil.getOrderStatusLabel(orderBean.getStatus()));
            holder.statusLabel.setVisibility(View.VISIBLE);
        } else {
            holder.statusLabel.setVisibility(View.GONE);
        }

        if (mIsStatusRequired ||
            Constant.ORDER_STATUS_NEW_ORDER.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_COLLECTED.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANING_IN_PROGRESS.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_CLEANED.equals(orderBean.getStatus()) ||
            Constant.ORDER_STATUS_COMPLETED.equals(orderBean.getStatus())) {

            holder.speedTypeLabel.setText(CodeUtil.getSpeedTypeLabel(orderBean.getSpeedType()));
            holder.serviceChargeLabel.setText(CommonUtil.formatCurrency(orderBean.getTotalCharge()));

            holder.speedTypeLabel.setVisibility(View.VISIBLE);
            holder.serviceChargeLabel.setVisibility(View.VISIBLE);

            if (!mIsStatusRequired) {

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.speedTypeLabel.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                holder.speedTypeLabel.setLayoutParams(params);
            }
        } else {

            holder.speedTypeLabel.setVisibility(View.GONE);
            holder.serviceChargeLabel.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getMainPanelOnClickListener(final OrderBean orderBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessOrderListener.onOrderSelected(orderBean);
            }
        };
    }
}
