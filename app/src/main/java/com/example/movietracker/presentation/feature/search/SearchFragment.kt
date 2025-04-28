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
        collectFlow(viewModel.state) { state ->
            handleSearchResultsState(state.searchResults)
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

        binding.rvSearchResults.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.updateSearchQuery(text.toString())
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.performSearch()
                true
            } else {
                false
            }
        }

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
            } else {
                if (binding.etSearch.text.isNullOrEmpty()) {
                    viewModel.clearSearch()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
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