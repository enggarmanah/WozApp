package id.urbanwash.wozapp.util;

import android.support.v7.app.AppCompatActivity;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.dialog.NotificationDialog;

public class NotificationUtil {
	
	private static String ALERT_DIALOG_FRAGMENT_TAG = "alertDialogFragment";

	public static synchronized void showInfoMessage(AppCompatActivity appCompatActivity, String message) {

		NotificationDialog mNotificationDialog = new NotificationDialog(appCompatActivity, Constant.NOTIFICATION_TYPE_INFO);

		if (mNotificationDialog.isAdded()) {
			return;
		}

		try {
			mNotificationDialog.show(appCompatActivity.getSupportFragmentManager(), ALERT_DIALOG_FRAGMENT_TAG);
			mNotificationDialog.setInfo(message);

		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void showErrorMessage(AppCompatActivity appCompatActivity, String message) {

		NotificationDialog mNotificationDialog = new NotificationDialog(appCompatActivity, Constant.NOTIFICATION_TYPE_ERROR);

		if (mNotificationDialog.isAdded()) {
			return;
		}

		try {
			mNotificationDialog.show(appCompatActivity.getSupportFragmentManager(), ALERT_DIALOG_FRAGMENT_TAG);
			mNotificationDialog.setInfo(message);

		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void showAlertMessage(AppCompatActivity appCompatActivity, String message) {

		NotificationDialog mNotificationDialog = new NotificationDialog(appCompatActivity, Constant.NOTIFICATION_TYPE_ALERT);

		if (mNotificationDialog.isAdded()) {
			return;
		}

		try {
			mNotificationDialog.show(appCompatActivity.getSupportFragmentManager(), ALERT_DIALOG_FRAGMENT_TAG);
			mNotificationDialog.setInfo(message);

		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
}
