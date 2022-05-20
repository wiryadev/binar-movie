package com.wiryadev.binar_movie.ui.tv.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class TvShowsFragmentTest {

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
    fun getTv_isSuccess() {
        launchFragmentInHiltContainer<TvShowsFragment>()

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("discover_tv_success_response.json"))
        }
        mockWebServer.enqueue(mockResponse)

        // check recyclerView
        onView(withId(R.id.rv_tv_shows))
            .check(matches(ViewMatchers.isDisplayed()))

        // check first visible data
        onView(ViewMatchers.withText("Halo"))
            .check(matches(ViewMatchers.isDisplayed()))

        // scroll to position 10 and check
        onView(withId(R.id.rv_tv_shows))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10)
            )
        onView(ViewMatchers.withText("Pantanal"))
            .check(matches(ViewMatchers.isDisplayed()))
    }

}