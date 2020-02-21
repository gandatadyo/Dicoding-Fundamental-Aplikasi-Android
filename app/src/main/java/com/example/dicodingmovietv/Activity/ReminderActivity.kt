package com.example.dicodingmovietv.Activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingmovietv.AlarmReceiver
import com.example.dicodingmovietv.R
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*


class ReminderActivity : AppCompatActivity() {
    private val ID_ONETIME = 100
    private val ID_REPEATING = 101
    private val TYPE_RELEASE_MOVIETV = "ReleseMovieTV"
    private val TYPE_REPEATING = "RepeatingAlarm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        switchRelease.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)ReleaseReminder()
            else ReminderStop(TYPE_RELEASE_MOVIETV)
        }

        switchDaily.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)DailyReminderStart()
            else ReminderStop(TYPE_REPEATING)
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

    fun ReminderStop(type:String){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val requestCode :Int = if(type==TYPE_RELEASE_MOVIETV) ID_ONETIME else ID_REPEATING

        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    fun DailyReminderStart(){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("smessage", resources.getString(R.string.messagedailyreminder))
        intent.putExtra("stype",AlarmReceiver.TYPE_REPEATING)

        // init for submission
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 7)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(this, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
   }

    fun ReleaseReminder(){
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("smessage", "")
        intent.putExtra("stype",AlarmReceiver.TYPE_RELEASE_MOVIETV)

        // init for submission
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 8)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(this, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
     }
}
