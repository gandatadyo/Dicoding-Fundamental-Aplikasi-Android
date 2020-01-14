package com.example.dicodingmovietv

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings.System.DATE_FORMAT
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingmovietv.AlarmReceiver.Companion.EXTRA_TYPE
import com.example.dicodingmovietv.AlarmReceiver.Companion.TYPE_ONE_TIME
import com.example.dicodingmovietv.utils.DatePickerFragment
import com.example.dicodingmovietv.utils.TimePickerFragment
import kotlinx.android.synthetic.main.activity_reminder.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ReminderActivity : AppCompatActivity() {

    private val ID_ONETIME = 100
    private val ID_REPEATING = 101

    val TYPE_ONE_TIME = "OneTimeAlarm"
    val TYPE_REPEATING = "RepeatingAlarm"

    val DATE_PICKER_TAG = "DatePicker"
    val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switchRelease.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                ReleaseReminder(this)
//                DailyReminderStart()
            }
            else DailyReminderStop(TYPE_ONE_TIME)
        }

        switchDaily.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) DailyReminderStart()
            else DailyReminderStop(TYPE_REPEATING)
        }

        btn_once_date.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
        }

        btn_once_time.setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
        }
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

    fun DailyReminderStop(type:String){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Pengingat Harian dibatalkan", Toast.LENGTH_SHORT).show()
    }

    fun DailyReminderStart(){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, "Daily Reminder 07.00")
        intent.putExtra(EXTRA_TYPE, AlarmReceiver.TYPE_REPEATING)
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY,7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(this, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Pengingat setiap jam 07.00 diaktifkan", Toast.LENGTH_SHORT).show()
    }

    fun ReleaseReminder(context:Context){
        val DATE_FORMAT = "yyyy-MM-dd"
        val TIME_FORMAT = "HH:mm"

        val date = tv_once_date.text.toString()
        val time = tv_once_time.text.toString()
        val message = "Release"

        // Validasi inputan date dan time terlebih dahulu
        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, TYPE_ONE_TIME)

        Log.e("ONE TIME", "$date $time")
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(context, "One time alarm set up", Toast.LENGTH_SHORT).show()
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
