package com.krithik.floatingnote.di


import android.app.Application
import android.content.Context

import com.krithik.floatingnote.database.NoteDatabase
import dagger.Module
import dagger.Provides

import javax.inject.Singleton
//For Dagger
@Module

class AppModule (
        private val context: Context){

    @Singleton
    @Provides
    fun noteDatabase(): NoteDatabase {
        return NoteDatabase.getInstance(context.applicationContext)
    }


    @Provides
    fun noteDao(db: NoteDatabase) = db.noteDao








}

