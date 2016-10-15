package id.urbanwash.wozapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.urbanwash.wozapp.R;

/**
 * Created by apridosandyasa on 3/20/16.
 */
@SuppressLint("ValidFragment")
public class ServicesExpressConfirmFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private View rootView;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public ServicesExpressConfirmFragment() {

    }

    public ServicesExpressConfirmFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.rootView = inflater.inflate(R.layout.content_services_confirm, container, false);

        return this.rootView;
    }
}
