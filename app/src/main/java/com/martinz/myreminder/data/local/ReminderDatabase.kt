package com.martinz.myreminder.data.local

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.martinz.myreminder.domain.model.Reminder


@Database(entities = [Reminder::class],version = 1)
@TypeConverters(Converters::class)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao() : ReminderDao
}