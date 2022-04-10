package com.wiryadev.binar_movie.ui

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun createImagePlaceholderDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 12f
        centerRadius = 48f
    }
}

fun dpToPx(dp: Number) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    Resources.getSystem().displayMetrics
)