package com.martinz.myreminder.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.martinz.myreminder.domain.repositoy.ReminderRepository
import com.martinz.myreminder.domain.use_cases.reminder.ReminderUseCase
import com.martinz.myreminder.presentation.add_reminder_screen.ReminderFragment
import com.martinz.myreminder.presentation.add_reminder_screen.ReminderViewModel
import com.martinz.myreminder.presentation.main_screen.MainFragment
import com.martinz.myreminder.presentation.main_screen.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
class ReminderFragmentFactoryAndroidTest(
    private val repository: ReminderRepository,
    private val useCase: ReminderUseCase,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            MainFragment::class.java.name -> MainFragment(MainViewModel(repository, useCase))
            ReminderFragment::class.java.name -> ReminderFragment(ReminderViewModel(useCase))
            else -> super.instantiate(classLoader, className)
        }

    }
}