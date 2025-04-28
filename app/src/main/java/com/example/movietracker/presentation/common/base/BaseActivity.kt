package com.example.movietracker.presentation.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Base Activity class providing common functionality.
 *
 * This abstract class provides a foundation for all activities in the app
 * with ViewBinding support. It follows the Template Method pattern.
 *
 * @param VB The type of the ViewBinding
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    /**
     * ViewBinding instance for this activity.
     */
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateViewBinding()
        setContentView(binding.root)

        setupUI()

        observeViewModel()
    }

    /**
     * Inflate the ViewBinding for this activity.
     *
     * @return An instance of the ViewBinding.
     */
    abstract fun inflateViewBinding(): VB

    /**
     * Setup the UI components for this activity.
     */
    abstract fun setupUI()

    /**
     * Observe the ViewModel for this activity.
     */
    abstract fun observeViewModel()
}