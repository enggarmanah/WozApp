package id.urbanwash.wozapp.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.listener.NewOrderServicePieceListener;
import id.urbanwash.wozapp.model.LaundryItemBean;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductItemBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 4/10/16.
 */
public class PieceServiceAdapter extends RecyclerView.Adapter<PieceServiceAdapter.PieceServiceViewHolder> {

    private AppCompatActivity mAppCompatActivity;

    private NewOrderServicePieceListener mNewOrderServicePieceListener;

    private List<LaundryItemBean> mLaudryItemBeans;
    private List<OrderProductItemBean> mOrderProductItemBeans;
    private Map<String, OrderProductItemBean> mOrderProductItemMap;

    public PieceServiceAdapter(AppCompatActivity appCompatActivity, List<LaundryItemBean> laudryItemBeans, List<OrderProductItemBean> orderProductItemBeans) {

        mAppCompatActivity = appCompatActivity;
        mLaudryItemBeans = laudryItemBeans;
        mOrderProductItemBeans = orderProductItemBeans;

        mOrderProductItemMap = new HashMap<String, OrderProductItemBean>();

        for (OrderProductItemBean orderProductItemBean : mOrderProductItemBeans) {
            mOrderProductItemMap.put(orderProductItemBean.getLaundryItem().getCode(), orderProductItemBean);
        }

        mNewOrderServicePieceListener = (NewOrderServicePieceListener) mAppCompatActivity;
    }

    @Override
    public PieceServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_piece_service, parent, false);
        PieceServiceViewHolder pieceServiceViewHolder = new PieceServiceViewHolder(view);
        return pieceServiceViewHolder;
    }

    @Override
    public void onBindViewHolder(PieceServiceViewHolder holder, int position) {

        LaundryItemBean laundryItemBean = mLaudryItemBeans.get(position);
        OrderProductItemBean orderProductItemBean = mOrderProductItemMap.get(laundryItemBean.getCode());

        int count = 0;

        if (orderProductItemBean != null) {
            count = orderProductItemBean.getCount();
        }

        OrderBean orderBean = Session.getOrder();
        float price = 0;

        if (Constant.SPEED_TYPE_REGULAR.equals(orderBean.getSpeedType())) {
            price = laundryItemBean.getPrice1();

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(orderBean.getSpeedType())) {
            price = laundryItemBean.getPrice2();

        } else if (Constant.SPEED_TYPE_DELUXE.equals(orderBean.getSpeedType())) {
            price = laundryItemBean.getPrice3();
        }

        holder.descriptionLabel.setText(laundryItemBean.getName());

        if (price == 0) {
            holder.priceLabel.setText(mAppCompatActivity.getString(R.string.service_min_4_days));
            return;
        }

        holder.priceLabel.setText(CommonUtil.formatCurrency(price));
        holder.countLabel.setText(CommonUtil.formatString(count));

        holder.minusButton.setOnClickListener(getMinusButtonOnClickListener(laundryItemBean, holder.descriptionLabel, holder.countLabel, holder.minusButton));
        holder.plusButton.setOnClickListener(getPlusButtonOnClickListener(laundryItemBean, holder.descriptionLabel, holder.countLabel, holder.minusButton));

        if (count > 0) {
            holder.descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
            holder.minusButton.setImageResource(R.drawable.icon_minus);
            holder.minusButton.setEnabled(true);
        } else {
            holder.descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
            holder.minusButton.setImageResource(R.drawable.icon_minus_grey);
            holder.minusButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return mLaudryItemBeans.size();
    }

    public static class PieceServiceViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView descriptionLabel;
        private AppCompatTextView priceLabel;
        private AppCompatTextView countLabel;

        private AppCompatImageView plusButton, minusButton;

        PieceServiceViewHolder(View view) {
            super(view);

            descriptionLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            priceLabel = (AppCompatTextView) view.findViewById(R.id.label_price);
            countLabel = (AppCompatTextView) view.findViewById(R.id.label_count_new_order);
            plusButton = (AppCompatImageView) view.findViewById(R.id.button_plus);
            minusButton = (AppCompatImageView) view.findViewById(R.id.button_minus);
        }
    }

    private View.OnClickListener getMinusButtonOnClickListener(final LaundryItemBean laundryItemBean, final AppCompatTextView descriptionLabel,
                                                               final AppCompatTextView countLabel, final AppCompatImageView minusButton) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderProductItemBean orderProductItemBean = mOrderProductItemMap.get(laundryItemBean.getCode());

                if (orderProductItemBean == null) {
                    orderProductItemBean = new OrderProductItemBean();
                    orderProductItemBean.setLaundryItem(laundryItemBean);
                    mOrderProductItemBeans.add(orderProductItemBean);
                    mOrderProductItemMap.put(laundryItemBean.getCode(), orderProductItemBean);
                }

                int count = orderProductItemBean.getCount();

                if (count > 0) {

                    OrderBean orderBean = Session.getOrder();
                    float price = 0;

                    if (Constant.SPEED_TYPE_REGULAR.equals(orderBean.getSpeedType())) {
                        price = laundryItemBean.getPrice1();

                    } else if (Constant.SPEED_TYPE_EXPRESS.equals(orderBean.getSpeedType())) {
                        price = laundryItemBean.getPrice2();

                    } else if (Constant.SPEED_TYPE_DELUXE.equals(orderBean.getSpeedType())) {
                        price = laundryItemBean.getPrice3();
                    }

                    count -= 1;
                    int charge = count * (int) price;

                    orderProductItemBean.setCount(count);
                    orderProductItemBean.setCharge(charge);
                }

                if (count == 0) {

                    mOrderProductItemBeans.remove(orderProductItemBean);
                    mOrderProductItemMap.remove(laundryItemBean.getCode());

                    minusButton.setImageResource(R.drawable.icon_minus_grey);
                    minusButton.setEnabled(false);
                }

                if (count > 0) {
                    descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
                } else {
                    descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
                }

                countLabel.setText(CommonUtil.formatString(count));

                mNewOrderServicePieceListener.onOrderProductItemUpdated();
            }
        };
    }

    private View.OnClickListener getPlusButtonOnClickListener(final LaundryItemBean laundryItemBean, final AppCompatTextView descriptionLabel,
                                                              final AppCompatTextView countLabel, final AppCompatImageView minusButton) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderProductItemBean orderProductItemBean = mOrderProductItemMap.get(laundryItemBean.getCode());

                if (orderProductItemBean == null) {
                    orderProductItemBean = new OrderProductItemBean();
                    orderProductItemBean.setLaundryItem(laundryItemBean);
                    mOrderProductItemBeans.add(orderProductItemBean);
                    mOrderProductItemMap.put(laundryItemBean.getCode(), orderProductItemBean);
                }

                OrderBean orderBean = Session.getOrder();
                float price = 0;

                if (Constant.SPEED_TYPE_REGULAR.equals(orderBean.getSpeedType())) {
                    price = laundryItemBean.getPrice1();

                } else if (Constant.SPEED_TYPE_EXPRESS.equals(orderBean.getSpeedType())) {
                    price = laundryItemBean.getPrice2();

                } else if (Constant.SPEED_TYPE_DELUXE.equals(orderBean.getSpeedType())) {
                    price = laundryItemBean.getPrice3();
                }

                int count = orderProductItemBean.getCount() + 1;
                int charge = count * (int) price;

                orderProductItemBean.setCount(count);
                orderProductItemBean.setCharge(charge);

                if (count > 0) {

                    minusButton.setImageResource(R.drawable.icon_minus);
                    minusButton.setEnabled(true);
                }

                if (count > 0) {
                    descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorCustomGreen));
                } else {
                    descriptionLabel.setTextColor(ContextCompat.getColor(mAppCompatActivity, R.color.colorDarkGrey));
                }

                countLabel.setText(CommonUtil.formatString(count));

                mNewOrderServicePieceListener.onOrderProductItemUpdated();
            }
        };
    }
}
