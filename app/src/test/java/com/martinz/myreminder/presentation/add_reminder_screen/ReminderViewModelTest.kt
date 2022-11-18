package com.martinz.myreminder.presentation.add_reminder_screen


import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.data.repository.FakeReminderRepository
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.use_cases.reminder.DeleteReminder
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.domain.use_cases.reminder.SaveReminder
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import com.martinz.myreminder.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ReminderViewModelTest {

    private lateinit var viewModel: ReminderViewModel

    private lateinit var useCase: ReminderUseCase

    @Mock
    private lateinit var geofenceUtil: GeofenceUtil

    private val testDispatcher = TestCoroutineDispatcher()



    @get:Rule
    var instantTaskErrorActionRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()


    @Before
    fun setup() {
         geofenceUtil = mock(GeofenceUtil::class.java)
        val repository = FakeReminderRepository()
        useCase = ReminderUseCase(
            SaveReminder(repository , geofenceUtil),
            DeleteReminder(repository , geofenceUtil)
        )

        viewModel = ReminderViewModel(useCase , testDispatcher)
    }


   //  Saving reminder using geofence util and i guess it goes to android test because it needs context
//    @Test
//    fun `test save reminder shows toast if saved successfully`() = runBlocking {
//        viewModel.state = MutableStateFlow(ReminderState(
//            title = "title",
//            description = "Test Description",
//            location = LatLng(123.123, 123.123),
//            range = 100f
//        ))
//        viewModel.onEvent(ReminderEvent.SaveReminder)
//        val result = viewModel.uiEvent.first()
//        println(result.toString())
//        assertTrue(result is UiEvent.ShowToast)
//
//    }

    @Test
    fun `test save reminder with empty title shows error toast`() = runBlocking {
        viewModel.state = MutableStateFlow(ReminderState(
            title = "",
            description = "Test Description",
            location = LatLng(123.123, 123.123),
            range = 100f
        ))
        viewModel.onEvent(ReminderEvent.SaveReminder)
        val result = viewModel.uiEvent.first()
        assertTrue(result.equals(UiEvent.ShowToast("Title can't be empty")))
    }


    @Test
    fun `test save reminder with empty location shows error toast`() = runBlocking {
        viewModel.state = MutableStateFlow(ReminderState(
            title = "Title 1",
            description = "Test Description",
            location = null,
            range = 100f
        ))
        viewModel.onEvent(ReminderEvent.SaveReminder)
        val result = viewModel.uiEvent.first()
        assertTrue(result.equals(UiEvent.ShowToast("No Location Selected")))
    }


}