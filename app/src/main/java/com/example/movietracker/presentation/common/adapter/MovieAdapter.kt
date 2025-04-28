package com.example.movietracker.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movietracker.domain.model.Movie
import com.example.movietracker.databinding.ItemMovieBinding

/**
 * RecyclerView adapter for displaying a list of movies.
 *
 * This class uses ListAdapter for efficient updates with DiffUtil.
 * It follows the Adapter pattern for RecyclerView.
 *
 * @param onMovieClicked Callback when a movie is clicked
 * @param onFavoriteClicked Callback when the favorite button is clicked
 * @param onWatchedClicked Callback when the watched button is clicked
 */
class MovieAdapter(
    private val onMovieClicked: (Movie) -> Unit,
    private val onFavoriteClicked: (Movie) -> Unit,
    private val onWatchedClicked: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClicked(getItem(position))
                }
            }

            binding.btnFavorite.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFavoriteClicked(getItem(position))
                }
            }

            binding.btnWatched.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onWatchedClicked(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year.toString()

            // Set favorite and watched button states
            binding.btnFavorite.isSelected = movie.isFavorite
            binding.btnWatched.isSelected = movie.isWatched

            // Load movie poster
            Glide.with(binding.ivPoster.context)
                .load(movie.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivPoster)
        }
    }

    /**
     * DiffUtil callback for efficient updates.
     */
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}