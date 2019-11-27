package com.proyek2.blkindramayu.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.activity.SemuaInfoActivity

@SuppressLint("Registered")
class NotificationServices : FirebaseMessagingService(){
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        // tampilkan di log
        Log.e("NOTIFCEK", "From ${p0?.from}")

        // cek apakah notifikasi null / tidak
        if (p0?.notification != null){
            Log.e("NOTIFBODY", "Pesan FCM ${p0.notification?.body}")

            // tampilkan notifikasi
            showNotification(p0)
        }

        // cek apakah notifikasi ada datanya
        if (p0?.data != null){
            for (i in p0.data){
                Log.e("NOTIFDATA", "Data Keys ${i.key}")
                Log.e("NOTIFDATA", "Data Values ${i.value}")
            }

        }
    }

    private val channelId = "Default"
    @SuppressLint("WrongConstant", "ResourceAsColor")
    private fun showNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, SemuaInfoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_blkindramayu)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        // trigger notifikasi
        manager.notify(0, builder.build())
    }
}