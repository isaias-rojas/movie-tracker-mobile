package com.example.movietracker.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Base Fragment class providing common functionality.
 *
 * This abstract class provides a foundation for all fragments in the app
 * with ViewBinding support. It follows the Template Method pattern.
 *
 * @param VB The type of the ViewBinding
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null

    /**
     * ViewBinding instance for this fragment.
     * This property is only valid between onCreateView and onDestroyView.
     */
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("ViewBinding is only valid between onCreateView and onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup UI
        setupUI()

        // Observe ViewModel
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Inflate the ViewBinding for this fragment.
     *
     * @param inflater The LayoutInflater
     * @param container The parent ViewGroup
     * @return An instance of the ViewBinding.
     */
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * Setup the UI components for this fragment.
     */
    abstract fun setupUI()

    /**
     * Observe the ViewModel for this fragment.
     */
    abstract fun observeViewModel()

    /**
     * Collect Flow in a lifecycle-aware manner.
     *
     * @param flow The Flow to collect
     * @param collect The lambda to execute when collecting
     */
    protected fun <T> collectFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect { collect(it) }
            }
        }
    }
}