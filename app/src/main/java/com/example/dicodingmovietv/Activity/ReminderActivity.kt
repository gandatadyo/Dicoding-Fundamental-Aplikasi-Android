package com.example.dicodingmovietv.Activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dicodingmovietv.AlarmReceiver
import com.example.dicodingmovietv.MainActivity
import com.example.dicodingmovietv.R
import kotlinx.android.synthetic.main.activity_reminder.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ReminderActivity : AppCompatActivity() {
    private val ID_ONETIME = 100
    private val ID_REPEATING = 101
    private val TYPE_RELEASE_MOVIETV = "ReleseMovieTV"
    private val TYPE_REPEATING = "RepeatingAlarm"
    lateinit var dateglobal :String
    lateinit var timeglobal :String
    lateinit var calenderGLobal:Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switchRelease.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)ReleaseReminder()
            else ReminderStop(TYPE_RELEASE_MOVIETV)
        }

        switchDaily.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)DailyReminderStart()
            else ReminderStop(TYPE_REPEATING)
        }

        btnTest.setOnClickListener {
            val CHANNEL_ID = "NewNews"
            val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val resultIntent = Intent(this, MainActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(resultPendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "\"com.example.dicodingmovietv\""
                val descriptionText = "Hallo this is message from menu notification"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            with(NotificationManagerCompat.from(this)) {
                val notificationId = 1234
                notify(notificationId, mBuilder.build())
            }


            Toast.makeText(this,"Date : ${dateglobal} / Time : ${timeglobal}",Toast.LENGTH_LONG).show()
        }

        btnDatePicker.setOnClickListener {ShowDataPickerDialog()}
        btnTimePicker.setOnClickListener {TimeDataPickerDialog()}
        InitDateTimeGlobal()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun InitDateTimeGlobal(){
        val cal = Calendar.getInstance()

        // set date programatically
//        cal.set(Calendar.HOUR_OF_DAY,7)
//        cal.set(Calendar.MINUTE, 0)
//        cal.set(Calendar.SECOND, 0)
//        cal.set(Calendar.YEAR, 2019)
//        cal.set(Calendar.MONTH, 10)
//        cal.set(Calendar.DAY_OF_MONTH, 1)

        calenderGLobal = cal
        timeglobal = SimpleDateFormat("HH:mm").format(cal.time)
        dateglobal = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
    }

    fun ShowDataPickerDialog(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            calenderGLobal.set(Calendar.YEAR, 2019)
            calenderGLobal.set(Calendar.MONTH, 10)
            calenderGLobal.set(Calendar.DAY_OF_MONTH, 1)

            dateglobal = "${year}-${monthOfYear+1}-${dayOfMonth}"
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
        }, year, month, day)
        dpd.show()
    }

    fun TimeDataPickerDialog(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            calenderGLobal.set(Calendar.HOUR_OF_DAY, hour)
            calenderGLobal.set(Calendar.MINUTE, minute)
            calenderGLobal.set(Calendar.SECOND, 0)


            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            timeglobal = SimpleDateFormat("HH:mm").format(cal.time)
            Toast.makeText(this, SimpleDateFormat("HH:mm").format(cal.time), Toast.LENGTH_LONG).show()
        }
        calenderGLobal = cal
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun ReminderStop(type:String){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val requestCode :Int = if(type==TYPE_RELEASE_MOVIETV) ID_ONETIME else ID_REPEATING

        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Pengingat Harian dibatalkan", Toast.LENGTH_SHORT).show()
    }

    fun DailyReminderStart(){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
//        intent.putExtra("smessage", "Daily Reminder. Date : ${dateglobal} / Time : ${timeglobal}")
        intent.putExtra("smessage", "Selamat Pagi, Waktunya cek film & siaran tv favorit mu")
        intent.putExtra("stype",AlarmReceiver.TYPE_REPEATING)

        // init for submission
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 7)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(this, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Date : ${dateglobal} / Time : ${timeglobal}. Pengingat Daily diaktifkan", Toast.LENGTH_SHORT).show()
    }

    fun ReleaseReminder(){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
//        intent.putExtra("smessage", "Daily Reminder. Date : ${dateglobal} / Time : ${timeglobal}")
        intent.putExtra("smessage", "Informasi Film Terbaru")
        intent.putExtra("stype",AlarmReceiver.TYPE_RELEASE_MOVIETV)

        // init for submission
//        val c = Calendar.getInstance()
//        c.set(Calendar.HOUR_OF_DAY, 8)
//        c.set(Calendar.MINUTE, 0)
//        c.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(this, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calenderGLobal.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Date : ${dateglobal} / Time : ${timeglobal}. Pengingat Relese diaktifkan", Toast.LENGTH_SHORT).show()





//        val DATE_FORMAT = "yyyy-MM-dd"
//        val TIME_FORMAT = "HH:mm"
//
////        val date = tv_once_date.text.toString()
////        val time = tv_once_time.text.toString()
//        val message = "Release"
//
//        // Validasi inputan date dan time terlebih dahulu
//        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.putExtra(EXTRA_MESSAGE, message)
//        intent.putExtra(EXTRA_TYPE, TYPE_RELEASE_MOVIETV)
//
//        Log.e("ONE TIME", "$date $time")
//        val dateArray = date.split("-").toTypedArray()
//        val timeArray = time.split(":").toTypedArray()
//
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
//        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
//        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
//        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
//        calendar.set(Calendar.SECOND, 0)
//
//        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0)
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//
//        Toast.makeText(context, "One time alarm set up", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}
