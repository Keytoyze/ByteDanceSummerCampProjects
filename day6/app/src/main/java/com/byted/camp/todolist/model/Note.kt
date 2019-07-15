package com.byted.camp.todolist.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.byted.camp.todolist.db.TodoContract.TodoNote
import java.util.Date

@Entity(tableName = TodoNote.TABLE_NAME)

data class Note(

    @ColumnInfo(name = TodoNote._ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = TodoNote.COLUMN_DATE)
    var date: Date? = Date(),

    @ColumnInfo(name = TodoNote.COLUMN_STATE)
    var state: State? = State.TODO,

    @ColumnInfo(name = TodoNote.COLUMN_CONTENT)
    var content: String? = null,

    @ColumnInfo(name = TodoNote.COLUMN_PRIORITY)
    var priority: Priority? = null
)
