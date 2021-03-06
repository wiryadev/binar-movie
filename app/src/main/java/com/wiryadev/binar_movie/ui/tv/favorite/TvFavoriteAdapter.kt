package com.wiryadev.binar_movie.ui.tv.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.local.entity.MovieEntity
import com.wiryadev.binar_movie.data.local.entity.TvEntity
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.databinding.ItemListBinding
import com.wiryadev.binar_movie.databinding.ItemListFavoriteBinding
import com.wiryadev.binar_movie.ui.createImagePlaceholderDrawable
import com.wiryadev.binar_movie.ui.dpToPx

class TvFavoriteAdapter(
    private val onItemClick: (Int) -> Unit,
) : ListAdapter<TvEntity, TvFavoriteAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    inner class MovieViewHolder(
        private val binding: ItemListFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvEntity) {

            with(binding) {
                val circularProgressDrawable = createImagePlaceholderDrawable(root.context)
                circularProgressDrawable.start()

                ivPoster.load("${BuildConfig.BASE_IMAGE_URL}${data.posterPath}") {
                    transformations(RoundedCornersTransformation(dpToPx(16)))
                    placeholder(circularProgressDrawable)
                    error(R.drawable.ic_baseline_broken_image_24)
                }
                tvTitle.text = data.title

                root.setOnClickListener {
                    onItemClick.invoke(data.tvId)
                }
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
        val binding = ItemListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvEntity>() {
            override fun areItemsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TvEntity, newItem: TvEntity): Boolean {
                return oldItem.tvId == newItem.tvId
            }
        }
    }
}