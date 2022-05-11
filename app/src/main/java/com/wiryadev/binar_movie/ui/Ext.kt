package com.wiryadev.binar_movie.ui

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

inline fun EditText.addErrorListener(
    name: String,
    crossinline validation: (String) -> Boolean = { true },
) {
    val parentInputLayout = this.parent.parent as TextInputLayout

    this.addTextChangedListener(
        afterTextChanged = {
            val additionalValidation = validation.invoke(it.toString())
            parentInputLayout.error = when {
                text.toString().isBlank() -> {
                    "$name must be filled"
                }
                !additionalValidation -> {
                    "$name must be valid"
                }
                else -> {
                    null
                }
            }
        },
    )
}

val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ROOT)

fun String.formatDisplayDate(): String {
    val dateFormatToBeDisplayed = simpleDateFormat.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(dateFormatToBeDisplayed)
}

/**
 * Storage camera Util
 */

fun savePhotoToExternalStorage(contentResolver : ContentResolver?, name: String, bmp: Bitmap?): Uri? {
    val imageCollection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$name.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (bmp != null) {
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }
    }
    return try {
        val uri = contentResolver?.insert(imageCollection, contentValues)?.also {
            contentResolver.openOutputStream(it).use { outputStream ->
                if (bmp != null) {
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Failed to save Bitmap")
                    }
                }
            }
        } ?: throw IOException("Failed to create Media Store entry")
        uri
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }

}