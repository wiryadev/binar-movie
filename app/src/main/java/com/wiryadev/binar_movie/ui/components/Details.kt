package com.wiryadev.binar_movie.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wiryadev.binar_movie.BuildConfig
import com.wiryadev.binar_movie.R

@ExperimentalMaterial3Api
@Composable
fun GenericDetailScreen(
    title: String,
    posterPath: String,
    genres: List<String>,
    rating: String,
    dateLabel: String,
    dateData: String,
    tagline: String?,
    overview: String?,
    isFavorite: Boolean,
    onFabClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PosterWithRating(posterPath = posterPath, rating = rating)
                DetailHeader(
                    title = title,
                    genres = genres,
                    dateLabel = dateLabel,
                    dateData = dateData
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TaglineAndOverview(tagline = tagline, overview = overview)
        }
        FloatingActionButton(
            onClick = { onFabClicked.invoke(isFavorite) },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Image(
                imageVector = if (isFavorite) {
                    Icons.Rounded.BookmarkAdded
                } else {
                    Icons.Rounded.BookmarkBorder
                },
                contentDescription = stringResource(id = R.string.favorite_button)
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PosterWithRating(
    posterPath: String,
    rating: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(
                width = 128.dp,
                height = 192.dp,
            )
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = "${BuildConfig.BASE_IMAGE_URL}${posterPath}"),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp)),
        )
        SuggestionChip(
            onClick = { /* do nothing */ },
            label = {
                Text(
                    text = rating,
                    style = MaterialTheme.typography.titleSmall,
                )
            },
            colors = SuggestionChipDefaults.elevatedSuggestionChipColors(),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun DetailHeader(
    title: String,
    genres: List<String>,
    dateLabel: String,
    dateData: String,
) {
    Column {
        Text(
            text = title,
            maxLines = 3,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.genre),
            fontSize = 12.sp,
        )
        Text(text = genres.joinToString(separator = ", "))

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dateLabel,
            fontSize = 12.sp,
        )
        Text(text = dateData)
    }
}

@Composable
private fun TaglineAndOverview(
    tagline: String?,
    overview: String?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = tagline ?: "",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = overview ?: "",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}