package com.example.movietracker.presentation.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietracker.R
import com.example.movietracker.databinding.FragmentSearchBinding
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.presentation.common.adapter.MovieAdapter
import com.example.movietracker.presentation.common.base.BaseFragment
import com.example.movietracker.presentation.common.model.UiState
import com.example.movietracker.presentation.feature.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment showing the search screen for searching movies.
 *
 * This fragment allows users to search for movies by title.
 * It follows the MVVM pattern and communicates with the ViewModel.
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        setupRecyclerView()
        setupSearchInput()
    }

    override fun observeViewModel() {
        // Observe UI state
        collectFlow(viewModel.state) { state ->
            handleSearchResultsState(state.searchResults)
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

        binding.rvSearchResults.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSearchInput() {
        // Update search query as user types
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.updateSearchQuery(text.toString())
        }

        // Perform search when user presses search on keyboard
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.performSearch()
                true
            } else {
                false
            }
        }

        // Clear button for search
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Add clear button if needed
            } else {
                // Clear search if focus lost and text is empty
                if (binding.etSearch.text.isNullOrEmpty()) {
                    viewModel.clearSearch()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Clear previous results when returning to the fragment
        if (binding.etSearch.text.isNullOrEmpty()) {
            viewModel.clearSearch()
        }
    }

    private fun handleSearchResultsState(state: UiState<List<Movie>>) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.tvNoResults.isVisible = state is UiState.Success && state.data.isEmpty()

        when (state) {
            is UiState.Success -> {
                movieAdapter.submitList(state.data)
                binding.rvSearchResults.isVisible = state.data.isNotEmpty()
            }
            is UiState.Error -> {
                binding.tvNoResults.isVisible = true
                binding.tvNoResults.text = state.message
            }
            else -> {}
        }
    }

    private fun handleEvent(event: SearchViewModel.SearchEvent) {
        when (event) {
            is SearchViewModel.SearchEvent.NavigateToMovieDetails -> {
                navigateToMovieDetails(event.movieId)
            }
            is SearchViewModel.SearchEvent.ShowError -> {
                showError(event.message)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}