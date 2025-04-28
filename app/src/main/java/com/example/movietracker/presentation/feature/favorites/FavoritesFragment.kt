package com.example.movietracker.presentation.feature.favorites

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
import com.example.movietracker.databinding.FragmentFavoritesBinding
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.presentation.common.adapter.MovieAdapter
import com.example.movietracker.presentation.common.base.BaseFragment
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment showing the list of favorite movies.
 *
 * This fragment displays all movies marked as favorites by the user.
 * It follows the MVVM pattern and communicates with the ViewModel.
 */
@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        setupRecyclerView()
    }

    override fun observeViewModel() {
        // Observe UI state
        collectFlow(viewModel.state) { state ->
            handleFavoritesState(state.favoritesState)
        }

        // Observe one-time events
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

        binding.rvFavoriteMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun handleFavoritesState(state: UiState<List<Movie>>) {
        binding.progressBar.isVisible = state is UiState.Loading

        when (state) {
            is UiState.Success -> {
                movieAdapter.submitList(state.data)
                binding.rvFavoriteMovies.isVisible = state.data.isNotEmpty()
                binding.tvNoFavorites.isVisible = state.data.isEmpty()
            }
            is UiState.Error -> {
                binding.tvNoFavorites.isVisible = true
                binding.tvNoFavorites.text = state.message
            }
            else -> {}
        }
    }

    private fun handleEvent(event: FavoritesViewModel.FavoritesEvent) {
        when (event) {
            is FavoritesViewModel.FavoritesEvent.NavigateToMovieDetails -> {
                navigateToMovieDetails(event.movieId)
            }
            is FavoritesViewModel.FavoritesEvent.ShowError -> {
                showError(event.message)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}