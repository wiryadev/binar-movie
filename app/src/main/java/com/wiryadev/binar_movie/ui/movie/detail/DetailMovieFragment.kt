package com.wiryadev.binar_movie.ui.movie.detail

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
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailMovieFragmentArgs by navArgs()
    private val viewModel: DetailMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetail(args.movieId)

        with(binding) {
            viewModel.movie.observe(viewLifecycleOwner) { uiState ->
                progressBar.isVisible = uiState.isLoading
                uiState.movie?.let { movie ->
                    tvLabelDate.text = "Release Date"
                    (requireActivity() as MainActivity).supportActionBar?.title = movie.title
                    ivDetailPoster.load("${BuildConfig.BASE_IMAGE_URL}${movie.posterPath}") {
                        transformations(RoundedCornersTransformation(dpToPx(16)))
                        placeholder(createImagePlaceholderDrawable(requireContext()))
                    }
                    tvDetailScore.text = movie.voteAverage.toString()
                    tvDetailTitle.text = movie.title
                    val genres = mutableListOf<String>()
                    for (genre in movie.genres) {
                        genres.add(genre.name)
                    }
                    tvDetailGenre.text = genres.joinToString(separator = ", ")
                    tvDetailTagline.text = movie.tagline
                    tvDetailOverview.text = movie.overview
                    tvDetailDate.text = movie.releaseDate
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}