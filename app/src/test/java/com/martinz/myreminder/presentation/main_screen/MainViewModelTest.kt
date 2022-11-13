package com.martinz.myreminder.presentation.main_screen


import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.data.repository.FakeReminderRepository
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MainViewModelTest {

     private lateinit var viewModel: MainViewModel
     private lateinit var repository: FakeReminderRepository


    @get:Rule
    var instantTaskErrorActionRule = InstantTaskExecutorRule()



    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



     @Before
     fun setup() {
         repository = FakeReminderRepository()
         viewModel = MainViewModel(repository)
     }





    @Test
    fun `getAllReminders()`() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1"  , description = "Desc 1" , latLong =  LatLng(123.123 , 123.123))
        val save = repository.saveReminder(reminder).first()
        assertThat(save , `is`(Response.Success("reminder saved successfully")))
        viewModel.getAllReminders()
        val result = viewModel.reminder.first()
        assertThat(result!!.contains(reminder), `is`(true))
        assertThat(result, notNullValue())

    }
    @Test
    fun `delete reminder if deleted returns true`() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1"  , description = "Desc 1" , latLong =  LatLng(123.123 , 123.123))
        repository.saveReminder(reminder)
        viewModel.onEvent(MainEvent.DeleteReminder(reminder))
        val result = viewModel.uiEvent.first()
        assertTrue( result is UiEvent.ShowSnackBar)
    }

    @Test
    fun `signOut() return false`()  = runBlocking{
        viewModel.onEvent(MainEvent.SignOut)
        val result = viewModel.uiEvent.first()
        assertTrue(result is UiEvent.Navigate)
    }
 }