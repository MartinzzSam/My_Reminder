package com.martinz.myreminder.presentation.main_screen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martinz.myreminder.R
import com.martinz.myreminder.core.base.BaseFragment
import com.martinz.myreminder.core.changeStatusBarColor
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.uiEventObserver
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.databinding.FragmentMainBinding
import com.martinz.myreminder.presentation.main_screen.util.LocationAdapter
import com.martinz.myreminder.presentation.main_screen.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment(
    viewModelTestFragment: MainViewModel? = null
) : BaseFragment<FragmentMainBinding, MainViewModel>(
    MainViewModel::class.java, viewModelTestFragment
) {

    private lateinit var locationAdapter: LocationAdapter


    override fun getViewBinding() = FragmentMainBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeStatusBarColor(Color.WHITE)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        toolBar()

        uiEventObserver(viewModel.uiEvent)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateObserver()
            }
        }


        locationAdapter = LocationAdapter(onClickListener = OnClickListener { reminder ->
            viewModel.onEvent(MainEvent.DeleteReminder(reminder))
        })



        binding.apply {
            rvLocation.apply {
                adapter = locationAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

    }


    private fun onClicks() {
        binding.apply {
            floatingActionButton.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToReminderFragment())
            }
        }

    }


    private suspend fun stateObserver() {
        viewModel.reminder.collect { list ->
            if (list != null) {
                if (list.isEmpty()) {
                    binding.indecator.visibility = View.VISIBLE
                } else {
                    binding.indecator.visibility = View.GONE
                }
                locationAdapter.submitList(list)

            } else {
                binding.indecator.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun myUiObserver() {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(this.context, event.message, Toast.LENGTH_SHORT).show()
                    myLog("eventmessage ${event.message}")
                }

                is UiEvent.Navigate -> {
                    findNavController().navigate(event.directions)
                }

                else -> {}
            }


        }
    }

    private fun toolBar() {
        binding.apply {
            ToolBar.inflateMenu(R.menu.menu)
            ToolBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.action_settings -> {
                        viewModel.onEvent(MainEvent.SignOut)
                        true
                    }
                    else -> {
                        false
                    }
                }

            }
        }
    }


}






