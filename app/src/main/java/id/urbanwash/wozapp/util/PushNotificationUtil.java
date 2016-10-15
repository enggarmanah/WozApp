package id.urbanwash.wozapp.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Date;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.activity.MainActivity;

/**
 * Created by Radix on 20/8/2016.
 */
public class PushNotificationUtil {

    private static Context mContext;

    public static void setContext(Context context) {

        mContext = context;
    }

    public static void createNotification(String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        Intent intent = new Intent(mContext, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.icon_urbanwash_small);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(defaultSoundUri);

        builder.setAutoCancel(true);

        Notification notification = builder.build();

        RemoteViews expandedView = new RemoteViews(mContext.getPackageName(), R.layout.content_notification_simple);

        expandedView.setTextViewText(R.id.label_title, mContext.getString(R.string.app_name));
        expandedView.setTextViewText(R.id.label_content, message);
        expandedView.setTextViewText(R.id.label_time, CommonUtil.format12HrAmPmTime(new Date()));

        notification.bigContentView = expandedView;

        NotificationManager nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    public static void createNotification(String orderNo, String orderStatus, Date orderDate, String orderPlaceName, String orderPlaceAddress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(Constant.DATA_ORDER, orderNo);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        //builder.setTicker(CodeUtil.getOrderStatusLabel(orderBean.getStatus()));

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.icon_urbanwash_small);
        // END_INCLUDE(ticker)

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(defaultSoundUri);

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.content_notification_simple);

        contentView.setTextViewText(R.id.label_title, mContext.getString(R.string.app_name));
        contentView.setTextViewText(R.id.label_content, "Order # " + orderNo + ", " + CodeUtil.getOrderStatusLabel(orderStatus));
        contentView.setTextViewText(R.id.label_time, CommonUtil.format12HrAmPmTime(new Date()));

        notification.contentView = contentView;

        if (Constant.ORDER_STATUS_NEW_ORDER.equals(orderStatus) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_COLLECTION.equals(orderStatus) ||
            Constant.ORDER_STATUS_ASSIGNED_FOR_DELIVERY.equals(orderStatus)) {

            RemoteViews expandedView = new RemoteViews(mContext.getPackageName(), R.layout.content_notification);

            expandedView.setTextViewText(R.id.label_order_no, "# " + orderNo);
            expandedView.setTextViewText(R.id.label_status, CodeUtil.getOrderStatusLabel(orderStatus));

            Date dateInfo = orderDate;

            String day = CommonUtil.formatDay(dateInfo) + ",";
            String date = CommonUtil.formatDateOfMonth(dateInfo);
            String month = CommonUtil.formatMonth(dateInfo);
            String time = CommonUtil.formatOperatingHrTimePeriod(dateInfo);

            expandedView.setTextViewText(R.id.label_day, day);
            expandedView.setTextViewText(R.id.label_description, date);
            expandedView.setTextViewText(R.id.label_month, month);
            expandedView.setTextViewText(R.id.label_time, time);

            if (!CommonUtil.isEmpty(orderPlaceName)) {
                expandedView.setViewVisibility(R.id.label_place_name, View.VISIBLE);
                expandedView.setTextViewText(R.id.label_place_name, orderPlaceName);
            } else {
                expandedView.setViewVisibility(R.id.label_place_name, View.GONE);
            }

            expandedView.setTextViewText(R.id.label_place_address, orderPlaceAddress);

            // Add a big content view to the notification if supported.
            // Support for expanded notifications was added in API level 16.
            // (The normal contentView is shown when the notification is collapsed, when expanded the
            // big content view set here is displayed.)
            if (Build.VERSION.SDK_INT >= 16) {

                // Inflate and set the layout for the expanded notification view
                notification.bigContentView = expandedView;
            }
            // END_INCLUDE(customLayout)
        }

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }
}
