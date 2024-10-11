package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val task = intent.getStringExtra("task") ?: "No Task"

        // Create notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "AlarmChannel"
            val descriptionText = "Channel for Alarm Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to open the app when the notification is clicked
        val notificationIntent = Intent(context, MainActivity9::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.ic_notification) // Replace with your app icon
            .setContentTitle("Alarm Reminder")
            .setContentText("Task: $task")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }
}
