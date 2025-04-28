package com.example.movietracker.presentation.common.binding

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movietracker.R

/**
 * Utility class for loading images with Glide.
 *
 * This class provides helper methods for loading images into ImageViews.
 */
object ImageBindingUtils {

    /**
     * Load an image from a URL into an ImageView.
     *
     * @param view The ImageView to load the image into
     * @param url The URL of the image to load
     */
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(view)
        }
    }

    /**
     * Load an image from a URL with circular cropping.
     *
     * @param view The ImageView to load the image into
     * @param url The URL of the image to load
     */
    fun loadCircleImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_error)
                .into(view)
        }
    }
}