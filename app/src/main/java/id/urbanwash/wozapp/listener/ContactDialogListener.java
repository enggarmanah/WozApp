package id.urbanwash.wozapp.listener;

/**
 * Created by apridosandyasa on 4/11/16.
 */
public interface ContactDialogListener {

    void onCall(String mobile);

    void onSms(String mobile);
}
