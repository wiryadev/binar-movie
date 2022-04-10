package com.wiryadev.binar_movie.ui.tv.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.MainActivity
import com.wiryadev.binar_movie.databinding.FragmentDetailBinding
import com.wiryadev.binar_movie.ui.createImagePlaceholderDrawable
import com.wiryadev.binar_movie.ui.dpToPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTvFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailTvFragmentArgs by navArgs()
    private val viewModel: DetailTvViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetail(args.tvId)

        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                progressBar.isVisible = uiState.isLoading
                uiState.movie?.let { tv ->
                    tvLabelDate.text = "First Air Date"
                    (requireActivity() as MainActivity).supportActionBar?.title = tv.name
                    ivDetailPoster.load("${BuildConfig.BASE_IMAGE_URL}${tv.posterPath}") {
                        transformations(RoundedCornersTransformation(dpToPx(16)))
                        placeholder(createImagePlaceholderDrawable(requireContext()))
                    }
                    tvDetailScore.text = tv.voteAverage.toString()
                    tvDetailTitle.text = tv.name
                    val genres = mutableListOf<String>()
                    for (genre in tv.genres) {
                        genres.add(genre.name)
                    }
                    tvDetailGenre.text = genres.joinToString(separator = ", ")
                    tvDetailTagline.text = tv.tagline
                    tvDetailOverview.text = tv.overview
                    tvDetailDate.text = tv.firstAirDate
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}