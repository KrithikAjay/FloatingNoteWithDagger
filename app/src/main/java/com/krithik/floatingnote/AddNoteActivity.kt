package com.krithik.floatingnote

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.krithik.floatingnote.database.Note
import com.krithik.floatingnote.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_REPLY = "com.krithik.floatingnote.AddNoteActivity.REPLY"
    }

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        binding.lifecycleOwner = this
        binding.buttonSave.setOnClickListener{
            val replyIntent = Intent()

            if (TextUtils.isEmpty(binding.editWord.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else  {
                val word = binding.editWord.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
        }

        }












