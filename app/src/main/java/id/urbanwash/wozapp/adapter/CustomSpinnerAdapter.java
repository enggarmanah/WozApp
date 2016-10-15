package id.urbanwash.wozapp.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import id.urbanwash.wozapp.R;

/**
 * Created by apridosandyasa on 1/23/16.
 */
public class CustomSpinnerAdapter extends BaseAdapter {
    private AppCompatActivity appCompatActivity;
    private String[] spinItems;
    private ViewHolder viewHolder;

    public CustomSpinnerAdapter(AppCompatActivity aca, String[] objects) {
        this.appCompatActivity = aca;
        this.spinItems = objects;
    }

    static class ViewHolder {
        TextView tv_item_spinner;
    }

    @Override
    public int getCount() {
        return this.spinItems.length;
    }

    @Override
    public Object getItem(int position) {
        return this.spinItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.appCompatActivity).inflate(R.layout.list_item_spinner, parent, false);
        this.viewHolder = new ViewHolder();
        convertView.setTag(this.viewHolder);
        this.viewHolder.tv_item_spinner = (TextView) convertView.findViewById(R.id.tv_item_spinner);
        this.viewHolder.tv_item_spinner.setText(this.spinItems[position]);
        return convertView;
    }
}
