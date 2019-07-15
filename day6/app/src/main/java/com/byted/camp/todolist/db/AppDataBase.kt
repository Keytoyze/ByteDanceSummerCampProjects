package com.byted.camp.todolist.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.byted.camp.todolist.model.Note

@TypeConverters(Converters::class)
@Database(entities = [Note::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        private const val DB_NAME = "todo.db"

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {


            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context,
                            AppDataBase::class.java,
                            DB_NAME)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}