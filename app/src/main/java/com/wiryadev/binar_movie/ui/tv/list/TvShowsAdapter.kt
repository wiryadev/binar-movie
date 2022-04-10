package com.wiryadev.binar_movie.ui.tv.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto
import com.wiryadev.binar_movie.databinding.ItemListBinding
import com.wiryadev.binar_movie.ui.createImagePlaceholderDrawable
import com.wiryadev.binar_movie.ui.dpToPx

class TvShowsAdapter(
    private val onItemClick: (Int) -> Unit,
) : PagingDataAdapter<TvDto, TvShowsAdapter.TvViewHolder>(DIFF_CALLBACK) {

    inner class TvViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvDto) {

            with(binding) {
                val circularProgressDrawable = createImagePlaceholderDrawable(root.context)
                circularProgressDrawable.start()

                ivPoster.load("${BuildConfig.BASE_IMAGE_URL}${data.posterPath}") {
                    transformations(RoundedCornersTransformation(dpToPx(16)))
                    placeholder(circularProgressDrawable)
                    error(R.drawable.ic_baseline_broken_image_24)
                }
                tvTitle.text = data.name
                tvDate.text = data.firstAirDate
                tvRating.text = data.voteAverage.toString()

                root.setOnClickListener {
                    onItemClick.invoke(data.id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        val data = getItem(position)
        data?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvDto>() {
            override fun areItemsTheSame(oldItem: TvDto, newItem: TvDto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TvDto, newItem: TvDto): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}