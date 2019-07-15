package com.byted.camp.todolist.db

import android.arch.persistence.room.TypeConverter
import com.byted.camp.todolist.model.Priority
import com.byted.camp.todolist.model.State
import java.util.*

class Converters {

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun longToDate(long: Long?): Date? {
        return long?.let { Date(it) }
    }

    @TypeConverter
    fun priorityToInt(priority: Priority?): Int? {
        return priority?.intValue
    }

    @TypeConverter
    fun intToPriority(int: Int?): Priority? {
        return int?.let { Priority.from(it)}
    }

    @TypeConverter
    fun stateToInt(state: State?): Int? {
        return state?.intValue
    }

    @TypeConverter
    fun intToState(int: Int?): State? {
        return int?.let { State.from(it)}
    }

}