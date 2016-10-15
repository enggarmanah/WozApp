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
import id.urbanwash.wozapp.model.MessageBean;
import id.urbanwash.wozapp.util.CommonUtil;

public class ManageMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity mAppCompatActivity;

    private List<MessageBean> mMessages;

    public ManageMessageAdapter(AppCompatActivity appCompatActivity, List<MessageBean> messages) {

        mAppCompatActivity = appCompatActivity;

        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setupMessageViewHolder((MessageViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mainPanel;

        private AppCompatTextView broadcastDateLabel;
        private AppCompatTextView contentLabel;

        MessageViewHolder(View view) {

            super(view);

            mainPanel = (RelativeLayout) view.findViewById(R.id.panel_main);

            broadcastDateLabel = (AppCompatTextView) view.findViewById(R.id.label_broadcast_date);
            contentLabel = (AppCompatTextView) view.findViewById(R.id.label_content);
        }
    }

    private void setupMessageViewHolder(MessageViewHolder holder, int position) {

        MessageBean messageBean = mMessages.get(position);

        holder.broadcastDateLabel.setText(CommonUtil.formatDate(messageBean.getBroadcastDate()));
        holder.contentLabel.setText(messageBean.getContent());
    }
}
