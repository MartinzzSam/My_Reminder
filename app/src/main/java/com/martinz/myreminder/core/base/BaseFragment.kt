package com.martinz.myreminder.core.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseFragment<VB: ViewBinding, VM: ViewModel>(
    private val viewModelClass: Class<VM>?,
    private val viewModelTest: VM?
) : Fragment(), LifecycleObserver {


    lateinit var viewModel: VM


    abstract fun getViewBinding(): VB
    private var _binding: ViewBinding? = null
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        viewModelClass?.let {
            viewModel = viewModelTest ?: ViewModelProvider(requireActivity())[it]
        }
        return binding.root
    }


}