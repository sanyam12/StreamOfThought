package com.unravel.streamofthought.ui.home

import android.content.Context
import androidx.lifecycle.LiveData


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
abstract class NotesRepository(private val noteDao: noteDao) {

    abstract val applicationContext: Context
    val allNote: LiveData<List<Notes>> = noteDao.getAllNotes()
    suspend fun insert(notes: Notes){
        noteDao.insert(notes)
    }
    suspend fun delete(notes: Notes){
        noteDao.delete(notes)
    }
}