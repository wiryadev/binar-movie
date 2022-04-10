package com.wiryadev.binar_movie.ui.movie.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.databinding.ItemListBinding
import com.wiryadev.binar_movie.ui.createImagePlaceholderDrawable
import com.wiryadev.binar_movie.ui.dpToPx

class MoviesAdapter : PagingDataAdapter<MovieDto, MoviesAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    class MovieViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieDto) {

            with(binding) {
                val circularProgressDrawable = createImagePlaceholderDrawable(root.context)
                circularProgressDrawable.start()

                ivPoster.load("${BuildConfig.BASE_IMAGE_URL}${data.posterPath}") {
                    transformations(RoundedCornersTransformation(dpToPx(16)))
                    placeholder(circularProgressDrawable)
                    error(R.drawable.ic_baseline_broken_image_24)
                }
                tvTitle.text = data.title
                tvDate.text = data.releaseDate
                tvRating.text = data.voteAverage.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)
        data?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieDto>() {
            override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}