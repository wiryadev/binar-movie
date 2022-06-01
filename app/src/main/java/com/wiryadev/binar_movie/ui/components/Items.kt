package com.wiryadev.binar_movie.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto

@ExperimentalMaterial3Api
@Composable
fun MovieCard(
    movie: MovieDto,
    isLoading: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    GenericCard(
        id = movie.id,
        posterUrl = "${BuildConfig.BASE_IMAGE_URL}${movie.posterPath}",
        title = movie.title,
        date = movie.releaseDate,
        rating = movie.voteAverage.toString(),
        isLoading = isLoading,
        onClick = onClick,
        modifier = modifier,
    )
}

@ExperimentalMaterial3Api
@Composable
fun TvCard(
    tv: TvDto,
    isLoading: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    GenericCard(
        id = tv.id,
        posterUrl = "${BuildConfig.BASE_IMAGE_URL}${tv.posterPath}",
        title = tv.name,
        date = tv.firstAirDate,
        rating = tv.voteAverage.toString(),
        isLoading = isLoading,
        onClick = onClick,
        modifier = modifier,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun GenericCard(
    id: Int,
    posterUrl: String,
    title: String,
    date: String,
    rating: String,
    isLoading: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth(),
        onClick = {
            onClick.invoke(id)
        },
        colors = CardDefaults.outlinedCardColors(),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        val painter = rememberAsyncImagePainter(model = posterUrl)
        val isImageLoading = painter.state is AsyncImagePainter.State.Loading

        Row {
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.poster),
                modifier = Modifier
                    .width(100.dp)
                    .height(160.dp)
                    .placeholder(
                        visible = isLoading || isImageLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                contentScale = ContentScale.FillBounds,
            )
            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    modifier = Modifier
                        .placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer()
                        ),
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                )
                SuggestionChip(
                    onClick = { /* do nothing */ },
                    label = {
                        Text(text = rating)
                    }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ErrorCard(
    message: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.outlinedCardColors(),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClick) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}