package com.krithik.floatingnote.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.krithik.floatingnote.MainActivity
import com.krithik.floatingnote.R
import com.krithik.floatingnote.viewModel.NoteViewModel

const val INTENT_COMMAND = "com.localazy.quicknote.COMMAND"
const val INTENT_COMMAND_EXIT = "EXIT"
const val INTENT_COMMAND_NOTE = "NOTE"
const val INTENT_COMMAND_REPLY = "REPLY"
const val REPLY_KEY = "reply_action"
//const val NOTIFICATION_ID = 1010


const val NOTIFICATION_CHANNEL_GENERAL = "quicknote_general"
private const val CODE_FOREGROUND_SERVICE = 1
private const val CODE_EXIT_INTENT = 2
private const val CODE_NOTE_INTENT = 3
private const val CODE_REPLY_INTENT = 4

class FloatingService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null


    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun showNotification() {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val replyIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_REPLY)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val replyPendingIntent = PendingIntent.getActivity(
                this, CODE_REPLY_INTENT, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        val exitIntent = Intent(this, FloatingService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_EXIT)
        }

        val noteIntent = Intent(this, FloatingService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_NOTE)
        }

        val exitPendingIntent = PendingIntent.getService(
                this, CODE_EXIT_INTENT, exitIntent, 0
        )

        val notePendingIntent = PendingIntent.getService(
                this, CODE_NOTE_INTENT, noteIntent, 0
        )

        // From Android O, it's necessary to create a notification channel first.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                with(
                        NotificationChannel(
                                NOTIFICATION_CHANNEL_GENERAL,
                                getString(R.string.notification_channel_general),
                                NotificationManager.IMPORTANCE_DEFAULT
                        )
                ) {

                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    manager.createNotificationChannel(this)
                }
            } catch (ignored: Exception) {
                // Ignore exception.
            }
        }
        val replyRemote = RemoteInput.Builder(REPLY_KEY).run {
            setLabel("Insert your message here")
            setTheme(R.style.Theme_AppCompat_Light)

            build()
        }
        val replyAction = NotificationCompat.Action.Builder(
                0, "Add Note", replyPendingIntent
        ).addRemoteInput(replyRemote)
                .build()


       with(NotificationCompat.Builder(
                this,
                NOTIFICATION_CHANNEL_GENERAL
        )) {

               setContentTitle(getString(R.string.app_name))
               setContentText(getString(R.string.notification_text))
               setAutoCancel(false)
               setOngoing(true)
               setSmallIcon(R.drawable.ic_baseline_add_24)
               setContentIntent(notePendingIntent)
               addAction(replyAction)
               addAction(
                       NotificationCompat.Action(
                               0,
                               getString(R.string.notification_exit),
                               exitPendingIntent
                       ))


           startForeground(CODE_FOREGROUND_SERVICE, build())
       }


    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val command = intent.getStringExtra(INTENT_COMMAND)



        if (command == INTENT_COMMAND_EXIT) {
            stopService()
            return START_NOT_STICKY
        }

        showNotification()

        if (command == INTENT_COMMAND_NOTE) {
            Toast.makeText(
                    this,
                    "Floating window to be added in the next lessons.",
                    Toast.LENGTH_SHORT
            ).show()
        }


        return START_STICKY
    }
}