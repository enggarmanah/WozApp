package id.urbanwash.wozapp.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;
import id.urbanwash.wozapp.model.ProductBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 4/10/16.
 */
public class NewOrderServiceKgActivity extends BaseActivity {

    private AppCompatEditText mWeightText;

    private AppCompatImageView mPlusButton;
    private AppCompatImageView mMinusButton;
    private AppCompatTextView mSaveButton;
    
    private AppCompatTextView mWeightLabel;
    private AppCompatTextView mServiceChargeLabel;

    private Float mWeight = 0f;
    private Float mServiceCharge = 0f;

    private ProductBean mProductBean;
    private OrderBean mOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_service_kg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackArrowColor());

        setNavigationBarTitle();
        
        mProductBean = (ProductBean) getIntent().getSerializableExtra(Constant.INTENT_DATA_PRODUCT);

        mWeightText = (AppCompatEditText) findViewById(R.id.text_weight);

        mPlusButton = (AppCompatImageView) findViewById(R.id.button_plus);
        mMinusButton = (AppCompatImageView) findViewById(R.id.button_minus);
        mSaveButton = (AppCompatTextView) findViewById(R.id.button_save);

        mWeightLabel = (AppCompatTextView) findViewById(R.id.label_weight);
        mServiceChargeLabel = (AppCompatTextView) findViewById(R.id.label_service_charge);

        mWeightLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeightText.requestFocus();
                showSoftKeyboard(mWeightText);
            }
        });

        mWeightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                if (!CommonUtil.isEmpty(input)) {
                    mWeight = CommonUtil.parseFloatNumber(input) / 10;
                } else {
                    mWeight = 0f;
                }

                refreshView();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mPlusButton.setOnClickListener(getPlusButtonOnClickListener());
        mMinusButton.setOnClickListener(getMinusButtonOnClickListener());
        mSaveButton.setOnClickListener(getSaveButtonOnClickListener());

        mOrderBean = Session.getOrder();
        OrderProductBean orderProductBean = mOrderBean.getOrderProduct(Constant.PRODUCT_KG_WASH_IRON);

        if (orderProductBean != null) {
            mWeight = orderProductBean.getCount();
        }

        String str = CommonUtil.formatString(Math.round(mWeight * 10));
        mWeightText.setText(str);

        if (Session.isCustomer()) {

            mPlusButton.setVisibility(View.VISIBLE);
            mMinusButton.setVisibility(View.VISIBLE);

            mWeightText.setFocusable(false);

        } else {

            mPlusButton.setVisibility(View.GONE);
            mMinusButton.setVisibility(View.GONE);

            mWeightText.setFocusable(true);
            mWeightText.requestFocus();
        }

        refreshView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Drawable changeBackArrowColor() {
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.icon_back_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    private void setNavigationBarTitle() {
        ((AppCompatTextView)getSupportActionBar().getCustomView().findViewById(R.id.label_title)).setText("ORDER SERVICE");
    }

    private void refreshView() {

        String unit = Constant.SPACE_STRING + getString(R.string.unit_kg);
        mWeightLabel.setText(CommonUtil.formatNumber(mWeight) + unit);

        float price = 0;

        if (Constant.SPEED_TYPE_REGULAR.equals(mOrderBean.getSpeedType())) {
            price = mProductBean.getPrice1();

        } else if (Constant.SPEED_TYPE_EXPRESS.equals(mOrderBean.getSpeedType())) {
            price = mProductBean.getPrice2();

        } else if (Constant.SPEED_TYPE_DELUXE.equals(mOrderBean.getSpeedType())) {
            price = mProductBean.getPrice3();
        }

        mServiceCharge = mWeight * price;

        mServiceChargeLabel.setText(CommonUtil.formatCurrency(mServiceCharge));

        if (mWeight > 0) {
            mMinusButton.setImageResource(R.drawable.icon_minus);
            mMinusButton.setEnabled(true);
        } else {
            mMinusButton.setImageResource(R.drawable.icon_minus_grey);
            mMinusButton.setEnabled(false);
        }
    }

    private View.OnClickListener getMinusButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWeight > 0) {
                    mWeight -= 1;
                }

                refreshView();
            }
        };
    }

    private View.OnClickListener getPlusButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWeight += 1;
                refreshView();
            }
        };
    }

    private View.OnClickListener getSaveButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderProductBean orderProductBean = mOrderBean.getOrderProduct(Constant.PRODUCT_KG_WASH_IRON);

                if (orderProductBean == null) {

                    orderProductBean = new OrderProductBean();

                    List<OrderProductBean> orderProductBeans = mOrderBean.getOrderProducts();

                    if (orderProductBeans == null) {
                        orderProductBeans = new ArrayList<OrderProductBean>();
                        mOrderBean.setOrderProducts(orderProductBeans);
                    }

                    orderProductBeans.add(orderProductBean);
                }

                orderProductBean.setProduct(mProductBean);
                orderProductBean.setCount(mWeight);
                orderProductBean.setCharge(mServiceCharge);

                mOrderBean.refreshTotalCharge();

                finish();
            }
        };
    }
}
