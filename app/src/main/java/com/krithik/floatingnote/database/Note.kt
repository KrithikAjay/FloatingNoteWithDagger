package com.krithik.floatingnote.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_data_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="UserId")
    var id : Int,

    @ColumnInfo(name = "Note")
    var note : String
) :Parcelable