package com.wiryadev.binar_movie.ui.movie.detail

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.wiryadev.binar_movie.utils.EspressoIdlingResource
import com.wiryadev.binar_movie.utils.JsonConverter
import com.wiryadev.binar_movie.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DetailMovieFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8888)
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun detailMovie_isSuccess() {
        val fragmentArgs = bundleOf("movieId" to 284052)
        launchFragmentInHiltContainer<DetailMovieFragment>(fragmentArgs = fragmentArgs)

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("detail_movie_success_response.json"))
        }
        mockWebServer.enqueue(mockResponse)

        // check poster
        onView(withContentDescription("Poster"))
            .check(matches(ViewMatchers.isDisplayed()))

        // check title
        onView(withText("Doctor Strange"))
            .check(matches(ViewMatchers.isDisplayed()))

        // check tagline
        onView(withText("The impossibilities are endless."))
            .check(matches(ViewMatchers.isDisplayed()))

        // check synopsis
        onView(withText("After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under her wing and trains him to defend the world against evil."))
            .check(matches(ViewMatchers.isDisplayed()))

    }

}