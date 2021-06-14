package com.krithik.floatingnote.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note:Note) : Long

    @Update
    suspend fun updateNote(note:Note)  : Int

    @Delete
    suspend fun deleteNote(note:Note) : Int

    @Query("SELECT * FROM note_data_table ")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("DELETE FROM note_data_table "  )
    fun deleteAll()

}