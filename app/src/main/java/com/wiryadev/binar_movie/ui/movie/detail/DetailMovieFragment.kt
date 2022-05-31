package com.wiryadev.binar_movie.ui.movie.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.remote.movie.dto.DetailMovieResponse
import com.wiryadev.binar_movie.ui.MainActivity
import com.wiryadev.binar_movie.ui.components.GenericDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private val args: DetailMovieFragmentArgs by navArgs()
    private val viewModel: DetailMovieViewModel by viewModels()

    private var movieDetail: DetailMovieResponse? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).setupActionBarListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getDetail(args.movieId)
        viewModel.checkIsFavorite(args.movieId)

        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                val uiState by viewModel.uiState.collectAsState()

                Mdc3Theme {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        }
                        uiState.movie?.let { movie ->
                            movieDetail = movie

                            if (activity is MainActivity) {
                                (activity as MainActivity).supportActionBar?.title =
                                    movie.originalTitle
                            }

                            GenericDetailScreen(
                                title = movie.title,
                                posterPath = movie.posterPath,
                                genres = movie.genres.map { it.name },
                                rating = movie.voteAverage.toString(),
                                dateLabel = stringResource(id = R.string.release_date),
                                dateData = movie.releaseDate,
                                tagline = movie.tagline,
                                overview = movie.overview,
                                isFavorite = uiState.isFavorite,
                                onFabClicked = ::onFabClicked,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onFabClicked(isFavorite: Boolean) {
        if (isFavorite) {
            movieDetail?.let { movie -> viewModel.deleteFavoriteMovie(movie) }
        } else {
            movieDetail?.let { movie -> viewModel.addFavoriteMovie(movie) }
        }
    }
}