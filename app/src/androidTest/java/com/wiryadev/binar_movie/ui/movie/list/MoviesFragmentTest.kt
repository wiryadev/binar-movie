package com.wiryadev.binar_movie.ui.movie.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.ui.MainActivity
import com.wiryadev.binar_movie.utils.JsonConverter
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
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

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
    fun getMovie_isSuccess() {
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(JsonConverter.readStringFromFile("success_response_movie.json"))
        }
        mockWebServer.enqueue(mockResponse)

        // check FAB
        onView(withId(R.id.fab_fav_page))
            .check(matches(isDisplayed()))

        // check recyclerView
        onView(withId(R.id.rv_movies))
            .check(matches(isDisplayed()))

        // check first visible data
        onView(withText("The Lost City"))
            .check(matches(isDisplayed()))


        onView(withId(R.id.rv_movies))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10)
            )
        onView(withText("The Contractor"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkFavoriteMovie_isEmpty() {
        // check FAB
        onView(withId(R.id.fab_fav_page))
            .perform(click())

        // check empty text message
        onView(withText("Your bookmark is empty"))
            .check(matches(isDisplayed()))
    }

}