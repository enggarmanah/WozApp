package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CodeUtil;
import id.urbanwash.wozapp.util.CommonUtil;

public class ReportDailyPicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;

    private List<ReportBean> mReports;

    public ReportDailyPicDetailAdapter(AppCompatActivity appCompatActivity, List<ReportBean> reports) {

        mAppCompatActivity = appCompatActivity;

        mReports = reports;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report_pic_detail, parent, false);
        ReportViewHolder holder = new ReportViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setupReportViewHolder((ReportViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView orderNo;
        private AppCompatTextView paymentTypeLabel;
        private AppCompatTextView valueLabel;

        ReportViewHolder(View view) {

            super(view);

            orderNo = (AppCompatTextView) view.findViewById(R.id.label_order_no);
            paymentTypeLabel = (AppCompatTextView) view.findViewById(R.id.button_payment_type);
            valueLabel = (AppCompatTextView) view.findViewById(R.id.label_value);
        }
    }

    private void setupReportViewHolder(ReportViewHolder holder, int position) {

        ReportBean reportBean = mReports.get(position);

        holder.orderNo.setText("# " + reportBean.getOrderNo());
        holder.paymentTypeLabel.setText(CodeUtil.getPaymentTypeLabel(reportBean.getPaymentType()));
        holder.valueLabel.setText(CommonUtil.formatCurrency(reportBean.getValue()));
    }
}
