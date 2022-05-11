package com.wiryadev.binar_movie.ui

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
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
 * Camera Util
 */
private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}