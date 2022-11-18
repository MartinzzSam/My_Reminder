package com.martinz.myreminder.data.local



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.domain.model.Reminder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ReminderDaoTest {

    private lateinit var database : ReminderDatabase
    private lateinit var dao : ReminderDao


    @get:Rule
    var instantTaskErrorActionRule = InstantTaskExecutorRule()
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReminderDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.reminderDao()
    }


    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertReminder() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1" , description = "Desc 1" , latLong = LatLng(123.123 , 123.123) , 200f)
        dao.addReminder(reminder)
        val reminders = dao.getAllReminders().first()
        assertTrue(reminders.contains(reminder))
    }

    @Test
    fun deleteReminder() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1" , description = "Desc 1" , latLong = LatLng(123.123 , 123.123), 200f)
        dao.addReminder(reminder)
        dao.deleteReminder(reminder)
        val allReminders = dao.getAllReminders().first()
        assertTrue(!allReminders.contains(reminder))

    }

    @Test
    fun getReminderById() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1" , description = "Desc 1" , latLong = LatLng(123.123 , 123.123), 200f)
        dao.addReminder(reminder)
        val result = dao.getReminderById(reminder.id)
        assertTrue(result == reminder)
    }


}