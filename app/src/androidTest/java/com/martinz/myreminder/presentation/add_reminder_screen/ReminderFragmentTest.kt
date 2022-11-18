package com.martinz.myreminder.presentation.add_reminder_screen


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.google.android.gms.maps.model.LatLng
import com.martinz.myreminder.R
import com.martinz.myreminder.di.ActivityModule
import com.martinz.myreminder.di.AppModule
import com.martinz.myreminder.di.ViewModelModule
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.util.ReminderFragmentFactoryAndroidTest
import com.martinz.myreminder.util.ToastMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import launchFragmentInHiltContainer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReminderFragmentTest {



    lateinit var fragmentFactoryTest : ReminderFragmentFactoryAndroidTest



    var hiltRule = HiltAndroidRule(this)

    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val chain = RuleChain.outerRule(instantTaskExecutorRule).around(hiltRule)

    @Inject
    @Named("test_repo")
    lateinit var repository: ReminderRepository

    @Inject
    @Named("test_reminder_use_case")
    lateinit var reminderUseCase: ReminderUseCase



    @Before
    fun setup() {

        hiltRule.inject()


        fragmentFactoryTest = ReminderFragmentFactoryAndroidTest(repository , reminderUseCase)



    }



    @Test
    fun noTitleWillFail()  {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<ReminderFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.saveBtn)).perform(click())


        onView(withText(("Title can't be empty"))).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

    }

    @Test
    fun noLocationWillFail() {
        val navController = mock(NavController::class.java)


        launchFragmentInHiltContainer<ReminderFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.etTitle)).perform(typeText("Title"))
        closeSoftKeyboard()

        onView(withId(R.id.saveBtn)).perform(click())
        onView(withText(("No Location Selected"))).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))


    }


    // This Test Will Fail if Location is Turned Off or if Location Permission is not granted
    @Test
    fun validDataWillSucceed() {

        var viewModel : ReminderViewModel? = null
        val navController = mock(NavController::class.java)


        launchFragmentInHiltContainer<ReminderFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = this.viewModel
        }

        onView(withId(R.id.etTitle)).perform(typeText("Title"))
        closeSoftKeyboard()

        onView(withId(R.id.etDesc)).perform(typeText("Description"))
        closeSoftKeyboard()

        viewModel!!.state = MutableStateFlow(viewModel!!.state.value.copy(
            location = LatLng(123.123,123.123),
            range = 100f
        ))

        onView(withId(R.id.saveBtn)).perform(click())

        verify(navController).navigate(ReminderFragmentDirections.actionReminderFragmentToMainFragment())
    }
}