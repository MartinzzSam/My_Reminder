package com.martinz.myreminder.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
 class ReminderRepositoryImplTest {

    private val mReminder= Reminder(
        title = "Test",
        description = "testing",
        latLong = LatLng(123.123, 123.123),
        range = 300f
    )

    var hiltRule = HiltAndroidRule(this)

    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val chain = RuleChain.outerRule(instantTaskExecutorRule).around(hiltRule)

    @Inject
    @Named("test_repo")
    lateinit var repository: ReminderRepository


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun saveReminder_Success() = runBlocking {

        repository.addReminder(mReminder)

        val result = repository.getAllReminders().first()

        assertTrue(result.contains(mReminder))

    }

    @Test
    fun deleteReminder_Success() = runBlocking {

        repository.addReminder(mReminder)

        repository.deleteReminder(mReminder)


        val result = repository.getAllReminders().first()

        assertTrue(!result.contains(mReminder))
    }


    @Test
    fun retrieveNonExistingReminderFails() = runBlocking {
        val result = repository.getReminderById(reminderId = mReminder.id)

        assertFalse(result != null)
    }
 }