package com.example.wanji.verificationcode

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = channelId1
            var channelName = "聊天消息"
            var importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)

            channelId = channelId2
            channelName = "订阅消息"
            importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)
        }

        submite.setOnClickListener {
            sendNotification(channelId2, "收到一条订阅消息", "楼下食堂今天有肉吃", Random().nextInt(10))
        }
        chat.setOnClickListener {
            sendNotification(channelId1, "收到一条聊天消息", "今天吃什么", Random().nextInt(10))
        }
        Observable.interval(5, TimeUnit.SECONDS).subscribeByThread {
            sendNotification(channelId2, "收到一条订阅消息", "楼下食堂今天有肉吃", it.toInt())
        }
    }


    private val channelId1 = "chat"
    private val channelId2 = "subscribe"

    private fun sendNotification(channelId: String, contentTitle: String, contentText: String, number: Int) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                startActivity(intent)
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show()
            }
        }
        val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setNumber(number)
                .build()
        when (channelId) {
            channelId1 -> manager.notify(1, notification)
            channelId2 -> manager.notify(2, notification)
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.setShowBadge(true)
        val notificationManager = getSystemService(
                NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
