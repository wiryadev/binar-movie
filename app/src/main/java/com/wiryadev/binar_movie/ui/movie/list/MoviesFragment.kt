package com.wiryadev.binar_movie.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.ui.components.ErrorCard
import com.wiryadev.binar_movie.ui.components.GenericErrorScreen
import com.wiryadev.binar_movie.ui.components.MovieCard
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                val movies = viewModel.movies.collectAsLazyPagingItems()
                val errorState = movies.loadState.refresh as? LoadState.Error
                    ?: movies.loadState.append as? LoadState.Error

                Mdc3Theme {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxSize(),
                        ) {
                            items(
                                items = movies,
                                key = { movie -> movie.id }
                            ) { movie ->
                                movie?.let {
                                    MovieCard(
                                        movie = it,
                                        isLoading = false,
                                        onClick = ::goToDetailMovie,
                                    )
                                }
                            }

                            movies.apply {
                                when {
                                    loadState.refresh is LoadState.Loading -> {
                                        items(10) {
                                            MovieCard(
                                                movie = dummyMovie,
                                                isLoading = true,
                                                onClick = {}
                                            )
                                        }
                                    }
                                    loadState.append is LoadState.Loading -> {
                                        item {
                                            MovieCard(
                                                movie = dummyMovie,
                                                isLoading = true,
                                                onClick = {}
                                            )
                                        }
                                    }
                                    loadState.refresh is LoadState.Error -> {
                                        item {
                                            GenericErrorScreen(
                                                message = errorState?.error?.message.toString(),
                                                onClick = ::retry,
                                            )
                                        }
                                    }
                                    loadState.append is LoadState.Error -> {
                                        item {
                                            ErrorCard(
                                                message = errorState?.error?.message.toString(),
                                                onClick = ::retry,
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                findNavController().navigate(
                                    MoviesFragmentDirections.actionNavigationMoviesToNavigationMovieFavorite()
                                )
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.BottomEnd),
                        ) {
                            Image(
                                imageVector = Icons.Rounded.Bookmark,
                                contentDescription = stringResource(
                                    id = R.string.go_to_favorite_movie
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun goToDetailMovie(id: Int) {
        findNavController().navigate(
            MoviesFragmentDirections.actionNavigationMoviesToNavigationDetailMovie(movieId = id)
        )
    }

}

private val dummyMovie = MovieDto(
    adult = false,
    backdropPath = "/backdropMovie",
    genreIds = listOf(),
    id = 1,
    originalLanguage = "",
    originalTitle = "Spider Man: No Way Home",
    overview = "",
    popularity = 0.0,
    posterPath = "/posterMovie",
    releaseDate = "",
    title = "Spider Man: No Way Home",
    video = false,
    voteAverage = 0.0,
    voteCount = 0
)