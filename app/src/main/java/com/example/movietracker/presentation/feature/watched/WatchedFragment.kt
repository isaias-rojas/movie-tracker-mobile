package com.example.movietracker.presentation.feature.watched

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietracker.R
import com.example.movietracker.databinding.FragmentWatchedBinding
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.presentation.common.adapter.MovieAdapter
import com.example.movietracker.presentation.common.base.BaseFragment
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment showing the list of watched movies.
 *
 * This fragment displays all movies marked as watched by the user.
 * It follows the MVVM pattern and communicates with the ViewModel.
 */
@AndroidEntryPoint
class WatchedFragment : BaseFragment<FragmentWatchedBinding>() {

    private val viewModel: WatchedViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWatchedBinding {
        return FragmentWatchedBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        setupRecyclerView()
    }

    override fun observeViewModel() {
        collectFlow(viewModel.state) { state ->
            handleWatchedState(state.watchedState)
        }

        collectFlow(viewModel.event) { event ->
            handleEvent(event)
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(
            onMovieClicked = { movie -> viewModel.onMovieClicked(movie) },
            onFavoriteClicked = { movie -> viewModel.toggleFavorite(movie) },
            onWatchedClicked = { movie -> viewModel.toggleWatched(movie) }
        )

        binding.rvWatchedMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun handleWatchedState(state: UiState<List<Movie>>) {
        binding.progressBar.isVisible = state is UiState.Loading

        when (state) {
            is UiState.Success -> {
                movieAdapter.submitList(state.data)
                binding.rvWatchedMovies.isVisible = state.data.isNotEmpty()
                binding.tvNoWatched.isVisible = state.data.isEmpty()
            }
            is UiState.Error -> {
                binding.tvNoWatched.isVisible = true
                binding.tvNoWatched.text = state.message
            }
            else -> {}
        }
    }

    private fun handleEvent(event: WatchedViewModel.WatchedEvent) {
        when (event) {
            is WatchedViewModel.WatchedEvent.NavigateToMovieDetails -> {
                navigateToMovieDetails(event.movieId)
            }
            is WatchedViewModel.WatchedEvent.ShowError -> {
                showError(event.message)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        findNavController().navigate(R.id.action_watchedFragment_to_detailFragment, bundle)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}