package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.listener.ReportDailyListener;
import id.urbanwash.wozapp.model.ReportBean;
import id.urbanwash.wozapp.util.CommonUtil;

public class ReportDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;
    private ReportDailyListener mReportDailyListener;

    private List<ReportBean> mReports;

    public ReportDailyAdapter(AppCompatActivity appCompatActivity, ReportDailyListener listener, List<ReportBean> reports) {

        mAppCompatActivity = appCompatActivity;
        mReportDailyListener = listener;

        mReports = reports;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_report, parent, false);
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

        private RelativeLayout mainPanel;

        private AppCompatTextView dateLabel;
        private AppCompatTextView valueLabel;

        ReportViewHolder(View view) {

            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            dateLabel = (AppCompatTextView) view.findViewById(R.id.label_description);
            valueLabel = (AppCompatTextView) view.findViewById(R.id.label_value);
        }
    }

    private void setupReportViewHolder(ReportViewHolder holder, int position) {

        ReportBean reportBean = mReports.get(position);

        holder.mainPanel.setOnClickListener(getMainPanelOnClickListener(reportBean));

        holder.dateLabel.setText(CommonUtil.formatDate(reportBean.getDate()));
        holder.valueLabel.setText(CommonUtil.formatCurrency(reportBean.getValue()));

    }

    private View.OnClickListener getMainPanelOnClickListener(final ReportBean reportBean) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReportDailyListener.onDateSelected(reportBean.getDate());
            }
        };
    }
}
