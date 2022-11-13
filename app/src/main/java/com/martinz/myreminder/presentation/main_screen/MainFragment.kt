package com.martinz.myreminder.presentation.main_screen

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martinz.myreminder.R
import com.martinz.myreminder.core.changeStatusBarColor
import com.martinz.myreminder.core.myLog
import com.martinz.myreminder.core.uiEventObserver
import com.martinz.myreminder.core.util.UiEvent
import com.martinz.myreminder.databinding.FragmentMainBinding
import com.martinz.myreminder.presentation.main_screen.util.LocationAdapter
import com.martinz.myreminder.presentation.main_screen.util.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var locationAdapter: LocationAdapter
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeStatusBarColor(Color.WHITE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launchWhenCreated {
            uiEventObserver(mainViewModel.uiEvent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        toolBar()

        viewLifecycleOwner.lifecycleScope.launch {
            myLog("Installing Observers on lifecycle")

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                stateObserver()
            }
        }


        locationAdapter = LocationAdapter(onClickListener = OnClickListener { reminder ->
            mainViewModel.onEvent(MainEvent.DeleteReminder(reminder))
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
        mainViewModel.reminder.collect { list ->
            if (list != null) {
                if (list.isEmpty()) {
                    binding.indecator.visibility = View.VISIBLE
                } else {
                    binding.indecator.visibility = View.GONE
                }
                locationAdapter.submitList(list)

            }else {
             binding.indecator.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun myUiObserver() {
        mainViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                Toast.makeText(this.context , event.message , Toast.LENGTH_SHORT).show()
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
                        mainViewModel.onEvent(MainEvent.SignOut)
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







