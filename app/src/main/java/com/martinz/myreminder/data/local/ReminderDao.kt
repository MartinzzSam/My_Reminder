package com.martinz.myreminder.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {

    @Insert(onConflict = REPLACE)
    fun addReminder(reminder : Reminder)

    @Delete
    fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM location_reminder")
    fun getAllReminders() : Flow<List<Reminder>>


    @Query("SELECT * FROM location_reminder WHERE id =:reminderId")
   suspend fun getReminderById(reminderId : Int) : Reminder
}