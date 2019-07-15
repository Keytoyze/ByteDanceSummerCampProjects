package com.byted.camp.todolist.db

import android.arch.persistence.room.*
import com.byted.camp.todolist.model.Note
import com.byted.camp.todolist.db.TodoContract.TodoNote
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class TodoDao {

    @Insert
    abstract fun blockingInsert(note: Note): Long

    fun insert(note: Note) = Single.fromCallable {
        blockingInsert(note)
    }

    @Query("SELECT * FROM ${TodoNote.TABLE_NAME} ORDER BY ${TodoNote.COLUMN_PRIORITY} DESC")
    abstract fun getAll(): Flowable<List<Note>>

    @Update
    abstract fun blockingUpdate(note: Note)

    fun update(note: Note) = Completable.fromAction {
        blockingUpdate(note)
    }

    @Delete
    abstract fun blockingDelete(note: Note): Int

    fun delete(note: Note) = Single.fromCallable {
        blockingDelete(note)
    }
}