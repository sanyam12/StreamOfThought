package com.unravel.streamofthought.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class noteViewModel(application: Application) : AndroidViewModel(application) {

    val repository: NotesRepository
    val allNotes: LiveData<List<Notes>>
    init{
        val dao = noteDatabase.getDatabase(application).getNoteDao()
        val repository = NotesRepository(dao)
        allNotes = repository.allNote
    }

    fun deleteNote(notes: Notes) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(notes)
    }

    fun insertNote(notes: Notes) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(notes)
    }
}