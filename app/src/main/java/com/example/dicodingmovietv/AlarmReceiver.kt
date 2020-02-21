package com.example.dicodingmovietv

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101
        const val TYPE_RELEASE_MOVIETV = "ReleseMovieTV"
        const val TYPE_REPEATING = "RepeatingAlarm"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val type = intent?.getStringExtra("stype")
        val message = intent?.getStringExtra("smessage")
        val notifId = if (type==TYPE_RELEASE_MOVIETV) ID_ONETIME else ID_REPEATING

        if (type==TYPE_RELEASE_MOVIETV)  context?.let { GetInformationNewMovie(it,notifId) }
        else if (type==TYPE_REPEATING) context?.let { ShowAlarmNotification(it, message.toString(), notifId) }
    }

    private fun ShowAlarmNotification(context: Context, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun GetInformationNewMovie(context: Context?, notifId: Int){
        val queue = Volley.newRequestQueue(context)
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=1c82c78abfacc7ee0508966c9489f84c&primary_release_date.gte=2020-01-19&primary_release_date.lte=2020-01-19"

        val postRequest = object :  StringRequest(
            Method.GET, url,
            Response.Listener<String> { response ->
                HandleGetInformationNewMovie(context,notifId,response)
            }, Response.ErrorListener {
                Toast.makeText(context, Resources.getSystem().getString(R.string.somethingwrong), Toast.LENGTH_SHORT).show()
            }){}
        queue.add(postRequest)
    }

    private fun HandleGetInformationNewMovie(context: Context?,notifId: Int,response: String){
        val responseObject = JSONObject(response)
        val list: JSONArray = responseObject.getJSONArray("results")
        if(list.length()>0) {
            for (i in 0 until list.length()){
                ShowNotification(context, "Film Terbaru", list.getJSONObject(i).getString("original_title"), notifId+i)
            }
        }
    }

    private fun ShowNotification(context: Context?, title: String, message: String, notifId: Int) {
        val CHANNEL_ID = notifId.toString()
        val mBuilder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.popcorn)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "\"com.example.dicodingmovietv\""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = message
            }
            val notificationManager: NotificationManager =context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        if (mBuilder != null) {
            with(context.let { NotificationManagerCompat.from(it) }) {
                val notificationId = notifId
                this.notify(notificationId, mBuilder.build())
            }
        }
    }
}