package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.LaundryItemBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 3/30/16.
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PricingViewHolder> {

    private List<LaundryItemBean> mLaundryItemBeans;
    private String mSpeedType;

    public PriceAdapter(List<LaundryItemBean> laundryItemBeans, String speedType) {

        mLaundryItemBeans = laundryItemBeans;
        mSpeedType = speedType;
    }

    @Override
    public PricingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pricing_tab_content,
                parent, false);

        PricingViewHolder pricingViewHolder = new PricingViewHolder(view);

        return pricingViewHolder;
    }

    @Override
    public void onBindViewHolder(PricingViewHolder holder, int position) {

        LaundryItemBean laundryItemBean = mLaundryItemBeans.get(position);

        float price = 0;

        if (Constant.SPEED_TYPE_REGULAR.equals(mSpeedType)) {
            price = laundryItemBean.getPrice1();

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(mSpeedType)) {
            price = laundryItemBean.getPrice2();

        } else if (Constant.SPEED_TYPE_DELUXE.equals(mSpeedType)) {
            price = laundryItemBean.getPrice3();
        }

        holder.itemDescriptionLabel.setText(laundryItemBean.getName());
        holder.itemPriceLabel.setText(CommonUtil.formatCurrency(price));
    }

    @Override
    public int getItemCount() {

        return mLaundryItemBeans.size();
    }

    public static class PricingViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView itemDescriptionLabel;
        private AppCompatTextView itemPriceLabel;

        PricingViewHolder(View view) {
            super(view);

            this.itemDescriptionLabel = (AppCompatTextView) view.findViewById(R.id.label_item_description);
            this.itemPriceLabel = (AppCompatTextView) view.findViewById(R.id.label_item_price);
        }
    }

    public void setSpeedType(String speedType) {
        mSpeedType = speedType;
    }
}
