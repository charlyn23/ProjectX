package charlyn23.c4q.nyc.projectx.map;

/**
 * Copyright 2014 Google Inc. All Rights Reserved.
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

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import charlyn23.c4q.nyc.projectx.MainActivity;
import charlyn23.c4q.nyc.projectx.R;

public class GeofenceIntentService extends IntentService {
    protected static final String GEOFENCE_NAME = "Geofence IntentService";

    public GeofenceIntentService() {
        super(GEOFENCE_NAME);
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            int errorCode = geofencingEvent.getErrorCode();
            switch (errorCode) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    Log.d(GEOFENCE_NAME, "HAS ERROR == Geofence service is not available now");
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    Log.d(GEOFENCE_NAME, "HAS ERROR == Your app has registered too many geofences");
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    Log.d(GEOFENCE_NAME, "HAS ERROR == You have provided too many PendingIntents to the addGeofences() call");
                default:
                    Log.d(GEOFENCE_NAME, "HAS ERROR == Unknown error: the Geofence service is not available now");
            }

            return;
        }

        // Get the fade_transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        sendNotification("Entering an area with highly reported incidents.");

        // Test that the reported fade_transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Send notification and log the fade_transition details.
            Log.i(GEOFENCE_NAME, "Geofence Notification sent");
        } else {
            // Log the error.
            Log.e(GEOFENCE_NAME, "Geofence fade_transition error: invalid fade_transition type %1$d");
        }
    }

    /**
     * Posts a notification in the notification bar when a fade_transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(this, MainActivity.class);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
}
