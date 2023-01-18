package com.example.memoi

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {//null이 아닐시 실행.

            //입력값들 가져오기
            val title= intent?.getStringExtra("title")
            val description = intent?.getStringExtra("description") ?: ""
            val url = intent?.getStringExtra("url") ?: ""
            val num = intent?.getIntExtra("num", 0)

            val contentIntent = Intent(context,MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context,num!!,contentIntent,
                PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(context, App.ALERT_CHANNL_ID)
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("$title")
                .setContentText("$description\n$url")
                .setContentIntent(pendingIntent)
                .build()
            context.getSystemService(NotificationManager::class.java)
                .notify(num?:0, notification)

        }
    }
}