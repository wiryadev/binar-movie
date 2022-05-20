package com.wiryadev.binar_movie.ui.movie.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.wiryadev.binar_movie.R
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
import org.junit.rules.RuleChain

@HiltAndroidTest
class MoviesFragmentTest {

    private val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)

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
    fun discoverMovie_isSuccess() {
        launchFragmentInHiltContainer<MoviesFragment>()

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("discover_movie_success_response.json"))
        }
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(250) // should use EspressoIdlingResource

        // check recyclerView
        onView(withId(R.id.rv_movies))
            .check(matches(isDisplayed()))

        // check first visible data
        onView(withText("The Lost City"))
            .check(matches(isDisplayed()))

        // scroll to position 10 and check
        onView(withId(R.id.rv_movies))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10)
            )
        onView(withText("The Contractor"))
            .check(matches(isDisplayed()))
    }

}