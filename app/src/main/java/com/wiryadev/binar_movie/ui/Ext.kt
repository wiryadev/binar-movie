package com.wiryadev.binar_movie.ui

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar

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

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}