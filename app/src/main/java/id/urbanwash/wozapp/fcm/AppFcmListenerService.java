/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.urbanwash.wozapp.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import id.urbanwash.wozapp.Constant;
import id.urbanwash.wozapp.R;
import id.urbanwash.wozapp.activity.MainActivity;
import id.urbanwash.wozapp.util.CommonUtil;
import id.urbanwash.wozapp.util.PushNotificationUtil;

public class AppFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "AppFcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();

        String message = CommonUtil.formatString(data.get(Constant.FCM_PARAM_MESSAGE));
        String orderNo = CommonUtil.formatString(data.get(Constant.FCM_PARAM_ORDER_NO));
        String orderStatus = CommonUtil.formatString(data.get(Constant.FCM_PARAM_ORDER_STATUS));

        Date orderDate = null;

        if (data.get(Constant.FCM_PARAM_ORDER_DATE) != null) {
            orderDate = new Date(Long.valueOf((String) data.get(Constant.FCM_PARAM_ORDER_DATE)));
        }

        String orderPlaceName = CommonUtil.formatString(data.get(Constant.FCM_PARAM_ORDER_PLACE_NAME));
        String orderPlaceAddress = CommonUtil.formatString(data.get(Constant.FCM_PARAM_ORDER_PLACE_ADDRESS));

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        PushNotificationUtil.setContext(getApplicationContext());

        if (!CommonUtil.isEmpty(message)) {
            PushNotificationUtil.createNotification(message);

        } else if (!CommonUtil.isEmpty(orderNo)) {
            PushNotificationUtil.createNotification(orderNo, orderStatus, orderDate, orderPlaceName, orderPlaceAddress);
        }
    }
}
