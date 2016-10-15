package id.urbanwash.wozapp.listener;

import id.urbanwash.wozapp.model.CreditBean;

public interface ProcessTopUpListener {

    void onProcessTopUp(CreditBean creditBean);
}
