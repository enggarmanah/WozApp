package id.urbanwash.wozapp.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.PieceServiceAdapter;
import id.urbanwash.wozapp.listener.NewOrderServicePieceListener;
import id.urbanwash.wozapp.model.OrderBean;
import id.urbanwash.wozapp.model.OrderProductBean;
import id.urbanwash.wozapp.model.OrderProductItemBean;
import id.urbanwash.wozapp.model.ProductBean;
import id.urbanwash.wozapp.util.CommonUtil;

/**
 * Created by apridosandyasa on 4/10/16.
 */
public class NewOrderServicePieceActivity extends BaseActivity implements NewOrderServicePieceListener {

    private AppCompatTextView mServiceChargeLabel;
    private AppCompatTextView mSaveButton;

    private AppCompatImageView mProductImage;
    private AppCompatTextView mProductNameLabel;
    private AppCompatTextView mProductDescLabel;

    private RecyclerView mPiecesPanel;
    private LinearLayoutManager mPiecesPanelLayoutManager;
    private PieceServiceAdapter mPiecesAdapter;

    private ProductBean mProductBean;
    private OrderBean mOrderBean;

    List<OrderProductItemBean> mOrderProductItemBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_service_piece);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeBackArrowColor());

        setNavigationBarTitle();

        mProductImage = (AppCompatImageView) findViewById(R.id.image_product);
        mProductNameLabel = (AppCompatTextView) findViewById(R.id.label_product_name);
        mProductDescLabel = (AppCompatTextView) findViewById(R.id.label_product_description);

        mServiceChargeLabel = (AppCompatTextView) findViewById(R.id.label_service_charge);
        mSaveButton = (AppCompatTextView) findViewById(R.id.button_save);

        mSaveButton.setOnClickListener(getSaveButtonOnClickListener());

        mPiecesPanel = (RecyclerView) findViewById(R.id.panel_pieces);
        mPiecesPanelLayoutManager = new LinearLayoutManager(this);
        mPiecesPanel.setHasFixedSize(true);
        mPiecesPanel.setLayoutManager(mPiecesPanelLayoutManager);

        mProductBean = (ProductBean) getIntent().getSerializableExtra(Constant.INTENT_DATA_PRODUCT);
        mOrderBean = Session.getOrder();

        mOrderProductItemBeans = new ArrayList<OrderProductItemBean>();

        if (Constant.PRODUCT_ITEM_WASH_IRON.equals(mProductBean.getCode())) {

            mProductImage.setImageResource(R.drawable.icon_piece);
            mProductNameLabel.setText(getString(R.string.service_name_piece));

        } else if (Constant.PRODUCT_DRY_CLEAN.equals(mProductBean.getCode())) {

            mProductImage.setImageResource(R.drawable.icon_dryclean);
            mProductNameLabel.setText(getString(R.string.service_name_dry_clean));
        }

        OrderProductBean orderProductBean = mOrderBean.getOrderProduct(mProductBean.getCode());

        if (orderProductBean != null) {

            for (OrderProductItemBean orderProductItemBean : orderProductBean.getOrderProductItems()) {

                OrderProductItemBean opiBean = new OrderProductItemBean();
                opiBean.setLaundryItem(orderProductItemBean.getLaundryItem());
                opiBean.setCount(orderProductItemBean.getCount());
                opiBean.setCharge(orderProductItemBean.getCharge());

                mOrderProductItemBeans.add(opiBean);
            }
        }

        mPiecesAdapter = new PieceServiceAdapter(this, mProductBean.getLaundryItems(), mOrderProductItemBeans);
        mPiecesPanel.setAdapter(mPiecesAdapter);

        refreshTotalCharge();
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
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    private void setNavigationBarTitle() {
        ((AppCompatTextView)getSupportActionBar().getCustomView().findViewById(R.id.label_title)).setText("ORDER SERVICE");
    }

    private void refreshTotalCharge() {

        int totalCharge = 0;

        for (OrderProductItemBean orderProductItemBean : mOrderProductItemBeans) {
            totalCharge += orderProductItemBean.getCharge();
        }

        mServiceChargeLabel.setText(CommonUtil.formatCurrency(totalCharge));
    }

    public void onOrderProductItemUpdated() {

        refreshTotalCharge();
    }

    private View.OnClickListener getSaveButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderProductBean orderProductBean = mOrderBean.getOrderProduct(mProductBean.getCode());

                if (orderProductBean == null) {

                    orderProductBean = new OrderProductBean();
                    orderProductBean.setProduct(mProductBean);
                    mOrderBean.getOrderProducts().add(orderProductBean);
                }

                int totalCount = 0;
                int totalCharge = 0;

                for (OrderProductItemBean orderOrderProductItemBean : mOrderProductItemBeans) {

                    totalCount += orderOrderProductItemBean.getCount();
                    totalCharge += orderOrderProductItemBean.getCharge();
                }

                orderProductBean.setCount(totalCount);
                orderProductBean.setCharge(totalCharge);
                orderProductBean.setOrderProductItems(mOrderProductItemBeans);

                if (mOrderProductItemBeans.size() == 0) {

                    mOrderBean.getOrderProducts().remove(orderProductBean);
                }

                mOrderBean.refreshTotalCharge();

                finish();
            }
        };
    }
}
