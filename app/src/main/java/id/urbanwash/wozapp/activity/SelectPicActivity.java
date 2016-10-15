package id.urbanwash.wozapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.Session;
import id.urbanwash.wozapp.adapter.PicAdapter;
import id.urbanwash.wozapp.listener.SelectPicListener;
import id.urbanwash.wozapp.model.EmployeeBean;

public class SelectPicActivity extends BaseActivity implements SelectPicListener {

    private AppCompatActivity mAppCompatActivity;

    private RecyclerView mPicView;
    private LinearLayoutManager mPicLayoutManager;
    private PicAdapter mPicAdapter;

    private List<EmployeeBean> mPicBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        mAppCompatActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_navigation_bar);
        getSupportActionBar().setHomeAsUpIndicator(changeHomeButton());

        AppCompatTextView titleLabel = (AppCompatTextView) getSupportActionBar().getCustomView().findViewById(R.id.label_title);
        titleLabel.setText(getString(R.string.title_select_pic));

        mPicView = (RecyclerView) findViewById(R.id.view_select_pic);
        mPicLayoutManager = new LinearLayoutManager(mAppCompatActivity);
        mPicView.setHasFixedSize(true);
        mPicView.setLayoutManager(mPicLayoutManager);

        mPicBeans = Session.getPics();

        if (mPicBeans == null) {
            mPicBeans = new ArrayList<EmployeeBean>();
        }

        mPicAdapter = new PicAdapter(mAppCompatActivity, mPicBeans, this);
        mPicView.setAdapter(mPicAdapter);
    }

    @Override
    public void onPicSelected(EmployeeBean picBean) {

        Session.setPic(picBean);

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}