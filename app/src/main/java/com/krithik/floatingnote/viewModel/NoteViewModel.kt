package com.krithik.floatingnote.viewModel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.krithik.floatingnote.database.Note
import com.krithik.floatingnote.database.NoteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
    ) : ViewModel() {

    val notes = MutableLiveData<String>()

    val noteList: LiveData<List<Note>> = repository.getAllNotes
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {

        notes.value = ""
    }

    fun add() {
        if (notes.value.isNullOrEmpty()) {
            statusMessage.value = Event("Please enter note")
        } else {
            val newNote = notes.value!!
            Log.i("insert", newNote)
            insertNote(Note(0,newNote)
            )
            notes.value = ""
        }
    }

    fun addfromReply(string: String) {
        insertNote(Note(0,string))


    }
    fun update(note: Note){
        viewModelScope.launch {
            repository.update(note)

        }
    }

    private fun insertNote(note: Note) {
        viewModelScope.launch {
            val newRowId = repository.insert(note)

            if (newRowId > -1) {
                statusMessage.value = Event("Notes Inserted Successfully ")
            } else {
                statusMessage.value = Event("Error Occurred")
            }




        }


    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
            statusMessage.value = Event("Note deleted")
        }
    }


}


open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}