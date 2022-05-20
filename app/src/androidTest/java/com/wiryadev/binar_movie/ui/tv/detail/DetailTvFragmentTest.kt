package com.wiryadev.binar_movie.ui.tv.detail

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
class DetailTvFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8888)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun detailTv_isSuccess() {
        val fragmentArgs = bundleOf("tvId" to 92749)
        launchFragmentInHiltContainer<DetailTvFragment>(fragmentArgs = fragmentArgs)

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("detail_tv_success_response.json"))
        }
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(250) // should use EspressoIdlingResource

        // check poster
        onView(withContentDescription("Poster"))
            .check(matches(isDisplayed()))

        // check title
        onView(withText("Moon Knight"))
            .check(matches(isDisplayed()))

        // check tagline
        onView(withText("Embrace the chaos."))
            .check(matches(isDisplayed()))

        // check synopsis
        onView(withText("When Steven Grant, a mild-mannered gift-shop employee, becomes plagued with blackouts and memories of another life, he discovers he has dissociative identity disorder and shares a body with mercenary Marc Spector. As Steven/Marc’s enemies converge upon them, they must navigate their complex identities while thrust into a deadly mystery among the powerful gods of Egypt."))
            .check(matches(isDisplayed()))

    }

}