package com.unravel.streamofthought.ui.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Notes(@ColumnInfo(name = "Note Text") val text : String,
            @PrimaryKey(autoGenerate = true) val id : Int) {

}