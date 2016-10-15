package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.ProcessTopUpListener;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 3/18/16.
 */
public class ProcessTopUpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private ProcessTopUpListener processTopUpListener;

    private List<CreditBean> mCredits;

    public ProcessTopUpAdapter(AppCompatActivity mppCompatActivity, ProcessTopUpListener listener, List<CreditBean> credits){

        mAppCompatActivity = mppCompatActivity;
        processTopUpListener = listener;

        mCredits = credits;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_credit, parent, false);
        CreditViewHolder holder = new CreditViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setupCompletedViewHolder((CreditViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mCredits.size();
    }

    public static class CreditViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;

        private AppCompatTextView dateLabel;
        private AppCompatTextView customerLabel;
        private AppCompatTextView bankNameLabel;
        private AppCompatTextView accountOwnerNameLabel;
        private AppCompatTextView amountLabel;
        private AppCompatImageView approveButton;

        CreditViewHolder(View view) {

            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            dateLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            customerLabel = (AppCompatTextView) view.findViewById(R.id.label_customer);
            bankNameLabel = (AppCompatTextView) view.findViewById(R.id.label_bank_name);
            accountOwnerNameLabel = (AppCompatTextView) view.findViewById(R.id.label_account_owner_name);
            amountLabel = (AppCompatTextView) view.findViewById(R.id.label_amount);
            approveButton = (AppCompatImageView) view.findViewById(R.id.button_approve);
        }
    }

    private void setupCompletedViewHolder(CreditViewHolder holder, int position) {

        CreditBean creditBean = mCredits.get(position);

        holder.dateLabel.setText(CommonUtil.formatDayDate(creditBean.getTransactionDate()));
        holder.customerLabel.setText(creditBean.getCustomer().getName());
        holder.bankNameLabel.setText(creditBean.getBankName());
        holder.accountOwnerNameLabel.setText(creditBean.getAccOwnerName());
        holder.amountLabel.setText(CommonUtil.formatCurrency(creditBean.getValue()));

        holder.approveButton.setOnClickListener(getApproveButtonOnClickListener(creditBean));
    }

    private View.OnClickListener getApproveButtonOnClickListener(final CreditBean creditBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processTopUpListener.onProcessTopUp(creditBean);
            }
        };
    }
}
