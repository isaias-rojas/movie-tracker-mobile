package com.example.movietracker.presentation.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movietracker.R
import com.example.movietracker.databinding.FragmentDetailBinding
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.presentation.common.base.BaseFragment
import com.example.movietracker.presentation.common.binding.ImageBindingUtils
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment showing the details of a specific movie.
 *
 * This fragment displays detailed information about a movie and allows
 * the user to mark it as favorite or watched.
 * It follows the MVVM pattern and communicates with the ViewModel.
 */
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val viewModel: DetailViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        setupToolbar()
        setupButtons()
    }

    override fun observeViewModel() {
        collectFlow(viewModel.state) { state ->
            handleMovieState(state.movieState)
        }

        collectFlow(viewModel.event) { event ->
            handleEvent(event)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }
    }

    private fun setupButtons() {
        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }

        binding.btnWatched.setOnClickListener {
            viewModel.toggleWatched()
        }
    }

    private fun handleMovieState(state: UiState<Movie?>) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.tvError.isVisible = state is UiState.Error

        when (state) {
            is UiState.Success -> {
                val movie = state.data
                if (movie != null) {
                    updateUI(movie)
                }
            }
            is UiState.Error -> {
                binding.tvError.text = state.message
            }
            else -> {}
        }
    }

    private fun updateUI(movie: Movie) {
        binding.collapsingToolbar.title = movie.title
        binding.tvYear.text = getString(R.string.release_year, movie.year)

        ImageBindingUtils.loadImage(binding.ivMoviePoster, movie.imageUrl)

        updateButtonStates(movie)
    }

    private fun updateButtonStates(movie: Movie) {
        // Update favorite button
        val favoriteIcon = if (movie.isFavorite) {
            R.drawable.ic_favorite
        } else {
            R.drawable.ic_favorite
        }
        binding.btnFavorite.setIconResource(favoriteIcon)
        binding.btnFavorite.text = if (movie.isFavorite) {
            getString(R.string.marked_as_favorite)
        } else {
            getString(R.string.mark_as_favorite)
        }

        val watchedIcon = if (movie.isWatched) {
            R.drawable.ic_watched
        } else {
            R.drawable.ic_watched
        }
        binding.btnWatched.setIconResource(watchedIcon)
        binding.btnWatched.text = if (movie.isWatched) {
            getString(R.string.marked_as_watched)
        } else {
            getString(R.string.mark_as_watched)
        }
    }

    private fun handleEvent(event: DetailViewModel.DetailEvent) {
        when (event) {
            is DetailViewModel.DetailEvent.NavigateBack -> {
                findNavController().navigateUp()
            }
            is DetailViewModel.DetailEvent.ShowError -> {
                showError(event.message)
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}