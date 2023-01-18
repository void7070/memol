package com.example.memoi

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App:Application() {
    companion object{
        const val ALERT_CHANNL_ID = "com.example.myalertnotification"
    }

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){//채널구현
            getSystemService(NotificationManager::class.java).run{
                val alertChannel = NotificationChannel(
                    ALERT_CHANNL_ID,
                    "Alert Tests",
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannel(alertChannel)
            }
        }
    }
}