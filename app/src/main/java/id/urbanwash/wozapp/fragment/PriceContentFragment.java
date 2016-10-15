package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.PriceAdapter;
import id.urbanwash.wozapp.model.LaundryItemBean;

/**
 * Created by apridosandyasa on 3/30/16.
 */
@SuppressLint("ValidFragment")
public class PriceContentFragment extends Fragment {

    private Activity mActivity;
    private View rootView;
    
    private AppCompatTextView mRegularButton;
    private AppCompatTextView mExpressButton;
    private AppCompatTextView mDeluxeButton;
    
    private RecyclerView mPricingView;
    private LinearLayoutManager mLinearLayoutManager;

    private PriceAdapter mPriceAdapter;
    private String mProductType;

    public PriceContentFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mProductType = bundle.getString(Constant.INTENT_DATA_PRODUCT);
    }

    @Override
    public void onAttach (Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.content_pricing_tab_content, container, false);

        mRegularButton = (AppCompatTextView) rootView.findViewById(R.id.button_regular);
        mExpressButton = (AppCompatTextView) rootView.findViewById(R.id.button_express);
        mDeluxeButton = (AppCompatTextView) rootView.findViewById(R.id.button_deluxe);
        mPricingView = (RecyclerView) rootView.findViewById(R.id.view_pricing);

        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mPricingView.setHasFixedSize(true);
        mPricingView.setLayoutManager(mLinearLayoutManager);

        List<LaundryItemBean> laundryItemBeans = new ArrayList<LaundryItemBean>();

        if (Session.getProducts() != null) {

            if (Constant.PRODUCT_KG_WASH_IRON.equals(mProductType)) {
                laundryItemBeans = Session.getProduct(Constant.PRODUCT_KG_WASH_IRON).getLaundryItems();

            } else if (Constant.PRODUCT_ITEM_WASH_IRON.equals(mProductType)) {
                laundryItemBeans = Session.getProduct(Constant.PRODUCT_ITEM_WASH_IRON).getLaundryItems();

            } else if (Constant.PRODUCT_DRY_CLEAN.equals(mProductType)) {
                laundryItemBeans = Session.getProduct(Constant.PRODUCT_DRY_CLEAN).getLaundryItems();
            }
        }

        mPriceAdapter = new PriceAdapter(laundryItemBeans, Constant.SPEED_TYPE_REGULAR);

        mPricingView.setAdapter(mPriceAdapter);

        mRegularButton.setOnClickListener(new SetActiveRegularList());
        mExpressButton.setOnClickListener(new SetActiveExpressList());
        mDeluxeButton.setOnClickListener(new SetActiveDeluxeList());

        SetActiveTabContentPricingList(Constant.SPEED_TYPE_REGULAR);

        return rootView;
    }

    class SetActiveRegularList implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SetActiveTabContentPricingList(Constant.SPEED_TYPE_REGULAR);
        }
    }

    class SetActiveExpressList implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SetActiveTabContentPricingList(Constant.SPEED_TYPE_EXPRESS);
        }
    }

    class SetActiveDeluxeList implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SetActiveTabContentPricingList(Constant.SPEED_TYPE_DELUXE);
        }
    }

    private void SetActiveTabContentPricingList(String service) {
        switch (service) {
            case Constant.SPEED_TYPE_REGULAR :

                mPriceAdapter.setSpeedType(Constant.SPEED_TYPE_REGULAR);
                mPriceAdapter.notifyDataSetChanged();
                
                mRegularButton.setBackgroundResource(R.drawable.btn_pop_up_normal);
                mExpressButton.setBackgroundColor(Color.WHITE);
                mDeluxeButton.setBackgroundColor(Color.WHITE);

                mRegularButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
                mExpressButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                mDeluxeButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                break;

            case Constant.SPEED_TYPE_EXPRESS:

                mPriceAdapter.setSpeedType(Constant.SPEED_TYPE_EXPRESS);
                mPriceAdapter.notifyDataSetChanged();
                
                mRegularButton.setBackgroundColor(Color.WHITE);
                mExpressButton.setBackgroundResource(R.drawable.btn_pop_up_normal);
                mDeluxeButton.setBackgroundColor(Color.WHITE);
                mRegularButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                mExpressButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
                mDeluxeButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                break;

            case Constant.SPEED_TYPE_DELUXE:

                mPriceAdapter.setSpeedType(Constant.SPEED_TYPE_DELUXE);
                mPriceAdapter.notifyDataSetChanged();

                mRegularButton.setBackgroundColor(Color.WHITE);
                mExpressButton.setBackgroundColor(Color.WHITE);
                mDeluxeButton.setBackgroundResource(R.drawable.btn_pop_up_normal);
                mRegularButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                mExpressButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorCustomGreen));
                mDeluxeButton.setTextColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
                break;
        }
    }
}
