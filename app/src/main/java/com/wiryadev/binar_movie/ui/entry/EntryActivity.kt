package com.wiryadev.binar_movie.ui.entry

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.ui.MainActivity
import com.wiryadev.binar_movie.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryActivity : AppCompatActivity() {

    private val viewModel: EntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_entry)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        Log.d("LoginState", "onCreate: executed")

        viewModel.isLoggedIn.observe(this) { isLoggedIn ->
            Log.d("LoginState", "onCreate: $isLoggedIn")
            startActivity(
                Intent(
                    this,
                    if (isLoggedIn) {
                        MainActivity::class.java
                    } else {
                        AuthActivity::class.java
                    }
                )
            )
            finish()
        }
    }
}