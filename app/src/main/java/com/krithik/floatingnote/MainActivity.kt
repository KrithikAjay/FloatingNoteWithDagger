package com.krithik.floatingnote


import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels

import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.krithik.floatingnote.database.Note
import com.krithik.floatingnote.databinding.ActivityMainBinding
import com.krithik.floatingnote.service.*

import com.krithik.floatingnote.viewModel.NoteViewModel
import com.krithik.floatingnote.viewModel.RecyclerViewAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {
    private val newWordActivityRequestCode = 1
    private lateinit var binding: ActivityMainBinding
    //     forDagger
    @Inject  lateinit var noteViewModel : NoteViewModel

    private lateinit var adapter: RecyclerViewAdapter




    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //dagger injection
        AndroidInjection.inject(this)
        startFloatingService()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        noteViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        initRecyclerView()
        noteViewModel.noteList.observe(this, Observer {
            adapter.submitList(it)
        })
        receiveReplyInput()
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }


    private fun initRecyclerView() {
        binding.noteRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = RecyclerViewAdapter(this)
        binding.noteRecyclerView.adapter = adapter

    }


    override fun onDeleteNote(note: Note) {
        noteViewModel.deleteNote(note)
    }



    private fun Context.startFloatingService(command: String = "") {
        val intent = Intent(this, FloatingService::class.java)
        if (command.isNotBlank()) intent.putExtra(INTENT_COMMAND, command)
        Log.i("Command", INTENT_COMMAND + command)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun receiveReplyInput() {

        val replyKey = REPLY_KEY
        val intent = this.intent
        val replyInput = RemoteInput.getResultsFromIntent(intent)

        if (replyInput != null) {
            val inputReplyString = replyInput.getCharSequence(replyKey).toString()

            noteViewModel.addfromReply(inputReplyString)


//            val notificationId = NOTIFICATION_ID
//            val channelID = NOTIFICATION_CHANNEL_GENERAL
//
//            val updateCurrentNotification =
//                    NotificationCompat.Builder(this@MainActivity, channelID)
//                            .setLargeIcon(
//                                    BitmapFactory.decodeResource(
//                                            this.resources,
//                                            android.R.drawable.ic_dialog_info
//                                    )
//                            )
//                            .setSmallIcon(android.R.drawable.ic_dialog_info)
//                            .setContentTitle("Message sent success")
//                            .setContentText("Updated notification")
//                            .build()
//
//            val notificationManager =
//                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            notificationManager.notify(notificationId, updateCurrentNotification)

        }


    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("RequestCode", requestCode.toString())
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddNoteActivity.EXTRA_REPLY)?.let {

                noteViewModel.addfromReply(it)

            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
