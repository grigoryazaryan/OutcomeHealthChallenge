package com.outcomehealth.challenge


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.outcomehealth.challenge.data.GalleryItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Grigory Azaryan on 10/11/20.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun gallery_is_loaded_and_items_are_clickable() {

        runBlocking {
            delay(2_000)

            for (item in items) {

                // select video from the gallery list
                onView(allOf(withText(item.title), withId(R.id.title)))
                    .perform(click())

                // playing video title has to be changed
                onView(withId(R.id.video_title))
                    .check(matches(withText(item.title)))

                delay(1_000)
            }

        }
    }

    @Test
    fun next_video_auto_played() {
        runBlocking {
            delay(2_000)

            // this item's duration is 15 sec
            onView(withText("For Bigger Blazes"))
                .perform(click())

            val activity = activityRule.activity
            val player = (activity as MainActivity).video_view.player!!

            activity.runOnUiThread { player.seekTo(15_000) }

            delay(1_000)

            // playing video title has to be changed
            onView(withId(R.id.video_title))
                .check(matches(withText("For Bigger Escape")))
        }

    }


    val items: List<GalleryItem> = listOf(
        GalleryItem(
            "Big Buck Bunny",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        GalleryItem(
            "Elephant Dream",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ),
        GalleryItem(
            "For Bigger Blazes",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        ),
        GalleryItem(
            "For Bigger Escape",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        )
    )

}