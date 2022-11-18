package com.martinz.myreminder.presentation

import android.app.Activity
import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.martinz.myreminder.core.util.EspressoIdlingResource
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.util.DataBindingIdlingResource
import com.martinz.myreminder.util.monitorActivity
import com.martinz.myreminder.R
import com.martinz.myreminder.presentation.main_screen.MainFragmentDirections
import com.martinz.myreminder.util.ToastMatcher
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config


@LargeTest
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()



    @get:Rule
    val hiltRule = HiltAndroidRule(this)




    @Test
    fun A_saveReminderScreen_showSnackBarTitleError() {


        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        dataBindingIdlingResource.monitorActivity(activityScenario)



        runBlocking {
            delay(1000)
        }

        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.saveBtn)).perform(click())


        onView(withText(("Title can't be empty"))).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))


        activityScenario.close()
    }

    @Test
    fun B_saveReminderScreen_showSnackBarLocationError() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        runBlocking {
            delay(1000L)
        }
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.etTitle)).perform(typeText("Title"))
        closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())


        onView(withText(("No Location Selected"))).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun C_saveReminderScreen_showDeletedSuccessfullyToast() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        runBlocking {
            delay(1000L)
        }
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.etTitle)).perform(typeText("Title"))
        closeSoftKeyboard()

        onView(withId(R.id.etDesc)).perform(typeText("Description"))
        closeSoftKeyboard()

        onView(withId(R.id.mapView)).perform(click())
        onView(withId(R.id.saveBtn)).perform(click())


        onView(withId(R.id.deleteBtn)).perform(click())


        onView(withText(("Geofence Deleted Successfully"))).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))


        activityScenario.close()
    }

    private fun getActivity(activityScenario: ActivityScenario<MainActivity>): Activity {
        lateinit var activity: Activity
        activityScenario.onActivity {
            activity = it
        }
        return activity
    }

}
