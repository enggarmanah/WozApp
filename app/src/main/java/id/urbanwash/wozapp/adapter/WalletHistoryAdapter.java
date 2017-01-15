package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.CreditBean;
import id.urbanwash.wozapp.util.CommonUtil;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.WalletHistoryViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private List<CreditBean> mCredits;

    public WalletHistoryAdapter(AppCompatActivity appCompatActivity, List<CreditBean> credits) {
        
        mAppCompatActivity = appCompatActivity;
        mCredits = credits;
    }

    @Override
    public WalletHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wallet_history, parent, false);
        WalletHistoryViewHolder walletHistoryViewHolder = new WalletHistoryViewHolder(view);
        return walletHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(WalletHistoryViewHolder holder, int position) {

        CreditBean creditBean = mCredits.get(position);

        holder.dateLabel.setText(CommonUtil.formatDate(creditBean.getTransactionDate(), "dd-MM-yyyy"));
        holder.descriptionLabel.setText(creditBean.getDescription());
        holder.amountLabel.setText(CommonUtil.formatCurrency(creditBean.getValue()));
    }

    @Override
    public int getItemCount() {
        return mCredits.size();
    }

    public static class WalletHistoryViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView dateLabel;
        private AppCompatTextView descriptionLabel;
        private AppCompatTextView amountLabel;

        WalletHistoryViewHolder(View view) {
            
            super(view);

            dateLabel = (AppCompatTextView) view.findViewById(R.id.label_date);
            descriptionLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            amountLabel = (AppCompatTextView) view.findViewById(R.id.label_amount);
        }
    }
}