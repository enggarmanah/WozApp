package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.ManagePromoListener;
import id.urbanwash.wozapp.model.PromoBean;
import id.urbanwash.wozapp.util.CommonUtil;

public class ManagePromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private ManagePromoListener mManagePromoListener;

    private List<PromoBean> mPromos;

    public ManagePromoAdapter(AppCompatActivity appCompatActivity, ManagePromoListener listener, List<PromoBean> promos) {

        mAppCompatActivity = appCompatActivity;
        mManagePromoListener = listener;

        mPromos = promos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo, parent, false);
        PromoViewHolder holder = new PromoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setupPromoViewHolder((PromoViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mPromos.size();
    }

    public static class PromoViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;

        private AppCompatTextView promoCodeLabel;
        private AppCompatTextView promoValueLabel;
        
        private AppCompatTextView startDateDayLabel;
        private AppCompatTextView startDateDateLabel;
        private AppCompatTextView startDateMonthLabel;
        
        private AppCompatTextView endDateDayLabel;
        private AppCompatTextView endDateDateLabel;
        private AppCompatTextView endDateMonthLabel;
        
        private AppCompatTextView descriptionLabel;
        private AppCompatTextView statusLabel;

        PromoViewHolder(View view) {

            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            promoCodeLabel = (AppCompatTextView) view.findViewById(R.id.label_promo_code);
            promoValueLabel = (AppCompatTextView) view.findViewById(R.id.label_promo_value);
            startDateDayLabel = (AppCompatTextView) view.findViewById(R.id.label_start_date_day);
            startDateDateLabel = (AppCompatTextView) view.findViewById(R.id.label_start_date_date);
            startDateMonthLabel = (AppCompatTextView) view.findViewById(R.id.label_start_date_month);
            endDateDayLabel = (AppCompatTextView) view.findViewById(R.id.label_end_date_day);
            endDateDateLabel = (AppCompatTextView) view.findViewById(R.id.label_end_date_date);
            endDateMonthLabel = (AppCompatTextView) view.findViewById(R.id.label_end_date_month);
            descriptionLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            statusLabel = (AppCompatTextView) view.findViewById(R.id.label_status);
        }
    }

    private void setupPromoViewHolder(PromoViewHolder holder, int position) {

        PromoBean promoBean = mPromos.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(promoBean));

        holder.promoCodeLabel.setText(promoBean.getCode());
        holder.descriptionLabel.setText(promoBean.getDescription());

        if (Constant.PROMO_TYPE_AMOUNT.equals(promoBean.getType())) {
            holder.promoValueLabel.setText(CommonUtil.formatCurrency(promoBean.getValue()));
        } else {
            holder.promoValueLabel.setText(CommonUtil.formatNumber(promoBean.getValue()) + " %");
        }

        String day = CommonUtil.formatDay(promoBean.getStartDate()) + ",";
        String date = CommonUtil.formatDateOfMonth(promoBean.getStartDate());
        String month = CommonUtil.formatMonth(promoBean.getStartDate());

        holder.startDateDayLabel.setText(day);
        holder.startDateDateLabel.setText(date);
        holder.startDateMonthLabel.setText(month);

        day = CommonUtil.formatDay(promoBean.getEndDate()) + ",";
        date = CommonUtil.formatDateOfMonth(promoBean.getEndDate());
        month = CommonUtil.formatMonth(promoBean.getEndDate());

        holder.endDateDayLabel.setText(day);
        holder.endDateDateLabel.setText(date);
        holder.endDateMonthLabel.setText(month);

        long curTime = CommonUtil.getCalendar().getTime().getTime();

        boolean isActive = Constant.CREDIT_STATUS_ACTIVE.equals(promoBean.getStatus()) &&
                promoBean.getStartDate().getTime() <= curTime && curTime <= promoBean.getEndDate().getTime();

        if (isActive) {
            holder.statusLabel.setText(mAppCompatActivity.getString(R.string.status_active));
            holder.statusLabel.setBackgroundDrawable(mAppCompatActivity.getResources().getDrawable(R.drawable.bg_square_green));
        } else {
            holder.statusLabel.setText(mAppCompatActivity.getString(R.string.status_inactive));
            holder.statusLabel.setBackgroundDrawable(mAppCompatActivity.getResources().getDrawable(R.drawable.bg_square_red));
        }
    }

    private View.OnClickListener getMainPanelOnClickListener(final PromoBean promoBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManagePromoListener.onUpdatePromo(promoBean);
            }
        };
    }
}
