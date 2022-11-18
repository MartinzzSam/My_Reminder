package com.martinz.myreminder.presentation.main_screen


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.martinz.myreminder.R
import com.martinz.myreminder.data.local.ReminderDatabase
import com.martinz.myreminder.data.repository.ReminderRepositoryImpl
import com.martinz.myreminder.di.ActivityModule
import com.martinz.myreminder.di.AppModule
import com.martinz.myreminder.di.ViewModelModule
import com.martinz.myreminder.domain.model.Reminder
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.util.ReminderFragmentFactoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.matches
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.regex.Pattern.matches
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainFragmentTest {


    var hiltRule = HiltAndroidRule(this)

    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val chain = RuleChain.outerRule(instantTaskExecutorRule).around(hiltRule)


    @Inject
    @Named("test_repo")
    lateinit var repository: ReminderRepository

    @Inject
    @Named("test_reminder_use_case")
    lateinit var reminderUseCase : ReminderUseCase


    @Before
    fun setup() {
        hiltRule.inject()


    }


    @Test
    fun clickOnAddFab_navigatesToSaveReminderFragment() = runBlocking {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<MainFragment>(
            fragmentFactory = ReminderFragmentFactoryAndroidTest(repository , reminderUseCase)
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.floatingActionButton)).perform(click())
        verify(navController).navigate(MainFragmentDirections.actionMainFragmentToReminderFragment())
    }


    @Test
    fun withReminders_showsOnScreen() {
        val reminder = Reminder(title = "Test", description =  "Test Description",
            latLong = LatLng(123.123 ,123.123) , range = 100f)

        runBlocking {
            repository.addReminder(reminder)
        }
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<MainFragment>(
            fragmentFactory = ReminderFragmentFactoryAndroidTest(repository , reminderUseCase)
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withText(reminder.title)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(reminder.description)).check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun withoutReminders_showsNoData() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<MainFragment>(
            fragmentFactory = ReminderFragmentFactoryAndroidTest(repository , reminderUseCase)
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }


        onView(withText("No Reminders Added")).check(matches(isDisplayed()))
        onView(withId(R.id.indecator)).check(matches(isDisplayed()))
    }


}

