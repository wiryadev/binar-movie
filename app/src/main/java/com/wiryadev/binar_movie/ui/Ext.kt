package com.wiryadev.binar_movie.ui

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

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