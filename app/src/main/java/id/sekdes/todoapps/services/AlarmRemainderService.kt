package id.sekdes.todoapps.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import id.sekdes.todoapps.MainActivity
import id.sekdes.todoapps.R
import id.sekdes.todoapps.models.TodoModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmRemainderService: BroadcastReceiver() {

    companion object{
        private const val CHANNEL_ID = "channel_alarm_001"
        private const val CHANNEL_NAME = "Reminder Alarm"
        private const val DATA_TODO = "dataExtraTodo"

        const val TIME_FORMAT = "HH:mm"

        fun setAlarmReminder(mContext: Context, todoData: TodoModel){
            if (!isFormatValid(todoData.dueTime)){
                Toast.makeText(mContext, mContext.getString(R.string.reminder_failed), Toast.LENGTH_SHORT).show()
                return
            }

            val mAlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val mIntent = Intent(mContext, AlarmRemainderService::class.java)
                .putExtra(
                    DATA_TODO, Gson().toJson(todoData))
            val mTimeArray = todoData.dueTime.split(":".toRegex()).dropLastWhile {
                it.isEmpty()
            }.toTypedArray()

            if (todoData.reminder){
                val minuteBefore = mTimeArray[1].toInt().minus(todoData.reminderTime)

                mTimeArray[1] = minuteBefore.toString()

                if (minuteBefore < 0){
                    mTimeArray[0] = mTimeArray[0].toInt().minus(1).toString()
                    mTimeArray[1] = 60.plus(minuteBefore).toString()
                }
            }

            val mCalendarOfDay = Calendar.getInstance()
            mCalendarOfDay.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mTimeArray[0]))
            mCalendarOfDay.set(Calendar.MINUTE, Integer.parseInt(mTimeArray[1]))
            mCalendarOfDay.set(Calendar.SECOND, 0)

            val mPendingIntent = PendingIntent.getBroadcast(mContext, todoData.id.toInt(), mIntent,0)
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendarOfDay.timeInMillis, mPendingIntent)

            if (todoData.reminder)
                Toast.makeText(mContext,mContext.getString(R.string.reminder_success), Toast.LENGTH_SHORT).show()

        }

        fun cancelAlarmReminder(mContext: Context, mIdReminder: Int){
            if (isReminderExist(mContext, mIdReminder)){
                val mAlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val mIntent = Intent(mContext, AlarmRemainderService::class.java)
                val mPendingIntent = PendingIntent.getBroadcast(mContext, mIdReminder,mIntent, 0)

                mPendingIntent.cancel()
                mAlarmManager.cancel(mPendingIntent)
            }
            Toast.makeText(mContext, mContext.getString(R.string.reminder_remove), Toast.LENGTH_SHORT).show()
        }

        private fun isReminderExist(mContext: Context, mIdReminder: Int): Boolean{
            return PendingIntent.getBroadcast(
                mContext,
                mIdReminder,
                Intent(mContext, AlarmRemainderService::class.java),
                PendingIntent.FLAG_NO_CREATE
            )!=null
        }

        private fun isFormatValid(mTimerSet: String): Boolean{
            return try {
                val df = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
                df.isLenient = false
                df.parse(mTimerSet)
                true
            }catch (error: ParseException){
                false
            }
        }
    }


    override fun onReceive(mContext: Context?, mIntent: Intent?) {
        showAlarmNotification(mContext, mIntent)
    }

    private fun showAlarmNotification(mContext: Context?, intent: Intent?){
        val todoData:TodoModel? = Gson().fromJson(intent?.getStringExtra(DATA_TODO),TodoModel::class.java)

        val mNotificationManager = mContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mAlarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mVibration = longArrayOf(1000,1000,1000,1000)

        val mIntent = Intent(mContext, MainActivity::class.java)
        mIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        val mResultPendingIntent = PendingIntent.getActivity(
            mContext,
            System.currentTimeMillis().toInt(),
            mIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val mNotificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(mContext.getString(R.string.reminder_notification_title))
            .setContentText(todoData?.title ?: mContext.getString(R.string.reminder_notification_body))
            .setColor(ContextCompat.getColor(mContext,android.R.color.transparent))
            .setVibrate(mVibration)
            .setContentIntent(mResultPendingIntent)
            .setSound(mAlarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mChannelNotification = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            mChannelNotification.enableVibration(true)
            mChannelNotification.vibrationPattern = mVibration

            mNotificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(mChannelNotification)
        }

        mNotificationManager.notify(0,mNotificationBuilder.build())

        if (todoData !=null && todoData.reminder){
            todoData.reminder = false
            setAlarmReminder(mContext, todoData)
        }
    }



}