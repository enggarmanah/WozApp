package id.urbanwash.wozapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Radix on 7/8/2016.
 */
public class BaseFragment extends Fragment {

    protected void showSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void hideSoftKeyboard(AppCompatEditText textView) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }
}
