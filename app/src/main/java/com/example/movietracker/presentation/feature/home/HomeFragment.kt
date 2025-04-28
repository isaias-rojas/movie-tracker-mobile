package com.example.movietracker.presentation.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietracker.R
import com.example.movietracker.databinding.FragmentHomeBinding
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.presentation.common.adapter.MovieAdapter
import com.example.movietracker.presentation.common.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Fragment showing the home screen with a list of all movies.
 *
 * This fragment displays a list of all movies and handles user interactions.
 * It follows the MVVM pattern and communicates with the ViewModel.
 */
@AndroidEntryPoint
class HomeFragment : androidx.fragment.app.Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        setupRecyclerView()
        setupSwipeRefresh()
        setupRetryButton()
    }

    private fun observeViewModel() {
        // Observe UI state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    handleMoviesState(state.moviesState)
                }
            }
        }

        // Observe one-time events
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(
            onMovieClicked = { movie -> viewModel.onMovieClicked(movie) },
            onFavoriteClicked = { movie -> viewModel.toggleFavorite(movie) },
            onWatchedClicked = { movie -> viewModel.toggleWatched(movie) }
        )

        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMovies()
        }
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadMovies()
        }
    }

    private fun handleMoviesState(state: UiState<List<Movie>>) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.tvError.isVisible = state is UiState.Error
        binding.btnRetry.isVisible = state is UiState.Error
        binding.swipeRefresh.isRefreshing = false

        when (state) {
            is UiState.Success -> {
                movieAdapter.submitList(state.data)
                binding.rvMovies.isVisible = state.data.isNotEmpty()

                if (state.data.isEmpty()) {
                    binding.tvError.isVisible = true
                    binding.tvError.text = getString(R.string.no_movies_found)
                }
            }
            is UiState.Error -> {
                binding.tvError.text = state.message
            }
            is UiState.Loading, is UiState.Idle -> {
                // Do nothing special
            }
        }
    }

    private fun handleEvent(event: HomeViewModel.HomeEvent) {
        when (event) {
            is HomeViewModel.HomeEvent.NavigateToMovieDetails -> {
                navigateToMovieDetails(event.movieId)
            }
            is HomeViewModel.HomeEvent.ShowError -> {
                showError(event.message)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}