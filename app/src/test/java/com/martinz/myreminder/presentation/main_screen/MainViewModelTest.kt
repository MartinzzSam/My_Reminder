package com.martinz.myreminder.presentation.main_screen



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.core.util.Response
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.data.repository.FakeReminderRepository
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.use_cases.RegisterUseCase
import com.martinz.myreminder.domain.use_cases.reminder.DeleteReminder
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.domain.use_cases.reminder.SaveReminder
import com.martinz.myreminder.presentation.geofence.GeofenceUtil
import com.martinz.myreminder.presentation.geofence.GeofenceUtilImpl
import com.martinz.myreminder.util.MainCoroutineRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

     private lateinit var viewModel: MainViewModel


     private lateinit var repository: FakeReminderRepository


     private lateinit var useCase: ReminderUseCase

     @Mock
     private lateinit var geofenceUtil : GeofenceUtil







    @get:Rule
    var instantTaskErrorActionRule = InstantTaskExecutorRule()



    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()



    private val testDispatcher = TestCoroutineDispatcher()



     @Before
     fun setup() {

         geofenceUtil = mock(GeofenceUtil::class.java)
         repository = FakeReminderRepository()

         useCase = ReminderUseCase(
             SaveReminder(repository , geofenceUtil),
             DeleteReminder(repository,geofenceUtil)
         )

         viewModel = MainViewModel(repository , useCase  , testDispatcher)
     }





    @Test
    fun `getAllReminders()`() = runBlocking {
        val reminder = Reminder(id = 1 , title = "Test 1"  , description = "Desc 1" , latLong =  LatLng(123.123 , 123.123) , range = 100f)
        repository.addReminder(reminder)
        viewModel.getAllReminders()
        val result = viewModel.reminder.first()
        assertTrue(result!!.contains(reminder))
        assertThat(result, notNullValue())

    }


    // Deleting reminder using geofence util and i guess it goes to android test because it needs context
//    @Test
//    fun `delete reminder if deleted Show Toast`() = runBlocking {
//        val reminder = Reminder(id = 1 , title = "Test 1"  , description = "Desc 1" , latLong =  LatLng(123.123 , 123.123), range = 100f)
//        repository.addReminder(reminder)
//        viewModel.onEvent(MainEvent.DeleteReminder(reminder))
//        val result = viewModel.uiEvent.first()
//        assertTrue( result is UiEvent.ShowToast)
//    }

    @Test
    fun `signOut()`()  = runBlocking{
        viewModel.onEvent(MainEvent.SignOut)
        val result = viewModel.uiEvent.first()
        assertTrue(result is UiEvent.Navigate)
    }
 }