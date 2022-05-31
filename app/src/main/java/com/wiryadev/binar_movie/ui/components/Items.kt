package com.wiryadev.binar_movie.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.data.remote.movie.dto.MovieDto
import com.wiryadev.binar_movie.data.remote.tv.dto.TvDto

@ExperimentalMaterial3Api
@Composable
fun MovieCard(
    movie: MovieDto,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    GenericCard(
        id = movie.id,
        posterUrl = "${BuildConfig.BASE_IMAGE_URL}${movie.posterPath}",
        title = movie.title,
        date = movie.releaseDate,
        rating = movie.voteAverage.toString(),
        onClick = onClick,
        modifier = modifier,
    )
}

@ExperimentalMaterial3Api
@Composable
fun TvCard(
    tv: TvDto,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    GenericCard(
        id = tv.id,
        posterUrl = "${BuildConfig.BASE_IMAGE_URL}${tv.posterPath}",
        title = tv.name,
        date = tv.firstAirDate,
        rating = tv.voteAverage.toString(),
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
        Row {
            Image(
                painter = rememberAsyncImagePainter(model = posterUrl),
                contentDescription = "",
                modifier = Modifier
                    .width(100.dp)
                    .height(160.dp),
                contentScale = ContentScale.FillBounds,
            )
            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
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