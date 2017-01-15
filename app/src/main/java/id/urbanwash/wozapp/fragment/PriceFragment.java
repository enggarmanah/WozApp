package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.util.Utility;

/**
 * Created by apridosandyasa on 3/30/16.
 */
@SuppressLint("ValidFragment")
public class PriceFragment extends Fragment {

    private AppCompatActivity appCompatActivity;
    private View rootView;
    private FragmentTabHost fragmentTabHost;

    public PriceFragment() {
    }

    public PriceFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.rootView = inflater.inflate(R.layout.content_pricing, container, false);

        this.fragmentTabHost = (FragmentTabHost) this.rootView.findViewById(android.R.id.tabhost);
        this.fragmentTabHost.setup(this.appCompatActivity, getChildFragmentManager(), R.id.realtabcontent);

        Bundle kgBundle = new Bundle();
        kgBundle.putString(Constant.INTENT_DATA_PRODUCT, Constant.PRODUCT_KG_WASH_IRON);

        Bundle pieceBundle = new Bundle();
        pieceBundle.putString(Constant.INTENT_DATA_PRODUCT, Constant.PRODUCT_ITEM_WASH_IRON);

        Bundle dryCleanBundle = new Bundle();
        dryCleanBundle.putString(Constant.INTENT_DATA_PRODUCT, Constant.PRODUCT_DRY_CLEAN);

        this.fragmentTabHost.addTab(
                setIndicator(this.appCompatActivity, this.fragmentTabHost.newTabSpec(getString(R.string.product_type_kilogram)), R.drawable.custom_tab_widget_backgroung),
                PriceContentFragment.class, kgBundle);
        this.fragmentTabHost.addTab(
                setIndicator(this.appCompatActivity, this.fragmentTabHost.newTabSpec(getString(R.string.product_type_piece)), R.drawable.custom_tab_widget_backgroung),
                PriceContentFragment.class, pieceBundle);
        /*this.fragmentTabHost.addTab(
                setIndicator(this.appCompatActivity, this.fragmentTabHost.newTabSpec(getString(R.string.product_type_dryclean)), R.drawable.custom_tab_widget_backgroung),
                PriceContentFragment.class, dryCleanBundle);*/

        this.rootView.setAnimation(Utility.AnimationUtility.setAnimationOnView(this.appCompatActivity));

        return this.rootView;
    }

    public TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec, int resid) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(ctx).inflate(R.layout.custom_tab_widget, null);
        v.setBackgroundResource(resid);
        TextView text = (TextView) v.findViewById(R.id.label_title);
        text.setText(spec.getTag());
        return spec.setIndicator(v);
    }
}
