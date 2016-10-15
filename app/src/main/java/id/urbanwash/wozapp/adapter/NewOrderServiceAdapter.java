package id.urbanwash.wozapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 4/10/16.
 */
public class NewOrderServiceAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private AppCompatActivity appCompatActivity;

    public NewOrderServiceAdapter(FragmentManager fm, AppCompatActivity aca, Fragment fragment) {
        super(fm);
        this.appCompatActivity = aca;
        initFragmentListData(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    private void initFragmentListData(Fragment fragment) {
        fragmentList.add(fragment);
    }

}
