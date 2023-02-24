package com.kajileten.myapplication.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.Geofence.GeofenceTransition
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.kajileten.myapplication.R
import com.kajileten.myapplication.util.Constants.NOTIFICATION_CHANNEL_ID
import com.kajileten.myapplication.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.kajileten.myapplication.util.Constants.NOTIFICATION_ID

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    private val broadcastReceiver = "BroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if(geofencingEvent!!.hasError()){
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(broadcastReceiver, errorMessage)
            return
        }

        when(geofencingEvent.geofenceTransition){
            Geofence.GEOFENCE_TRANSITION_ENTER ->{
                Log.d(broadcastReceiver, "Geofence ENTER")
                displayNotification(context, "Geofence ENTER")
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d(broadcastReceiver, "Geofence EXIT")
                displayNotification(context, "Geofence EXIT")
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Log.d(broadcastReceiver, "Geofence DWELL")
                displayNotification(context, "Geofence DWELL")
            }
            else -> {
                Log.d(broadcastReceiver, "Invalid Type")
                displayNotification(context, "Invalid Type")
            }
        }

    }

    private fun displayNotification(context: Context, geofenceTransition: String){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Geofence")
            .setContentText(geofenceTransition)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}