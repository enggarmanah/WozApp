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
 * Created by apridosandyasa on 3/18/16.
 */
@SuppressLint("ValidFragment")
public class NewOrderFragment extends Fragment {

    private View rootView;
    private AppCompatActivity appCompatActivity;

    // This is empty constructor. Do not delete it otherwise it will cause force close
    public NewOrderFragment() {

    }

    public NewOrderFragment(AppCompatActivity aca) {
        this.appCompatActivity = aca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.rootView = inflater.inflate(R.layout.content_new_order, container, false);

        return this.rootView;
    }
}
