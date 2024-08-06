package tw.com.batu.uitestsample

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    /**
     * 用 ActivityScenario，並且使用 Intent 啟動
     */
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    @get:Rule
//    val activityRule = ActivityScenarioRule<MainActivity>(Intent(context, MainActivity::class.java))
//
//    @Before
//    fun setUp() {
//        Log.d("badu", "setUp")
//    }
//
//    @After
//    fun clear(){
//        Log.d("badu", "clear")
//    }
//
//    @Test
//    fun clickButton1(){
//        onView(withId(R.id.textView)).check(matches(withText("XD")))
//        onView(withId(R.id.button)).perform(click())
//        onView(withId(R.id.textView)).check(matches(withText("button 1 clicked")))
//    }
//
//    @Test
//    fun clickButton2(){
//        onView(withId(R.id.textView)).check(matches(withText("XD")))
//        onView(withId(R.id.button2)).perform(click())
//        onView(withId(R.id.textView)).check(matches(withText("button 2 clicked")))
//    }

/***************************************************************************************/
    /**
     * 用 ActivityScenario 並且使用 IdlingResource
     */
    private lateinit var mIdlingResource: IdlingResource
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Log.d("badu", "setUp")
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            mIdlingResource = activity.getIdlingResource()
            // To prove that the test fails, omit this call:
        }
    }

    @After
    fun clear(){
        Log.d("badu", "clear")
        activityScenario.close()
    }

    @Test
    fun clickButton1(){
        onView(withId(R.id.textView)).check(matches(withText("XD")))
        onView(withId(R.id.button)).perform(click())
        IdlingRegistry.getInstance().register(mIdlingResource)
        onView(withId(R.id.textView)).check(matches(withText("button 1 clicked")))
        IdlingRegistry.getInstance().unregister(mIdlingResource)
    }

    @Test
    fun clickButton2(){
        onView(withId(R.id.textView)).check(matches(withText("XD")))
        onView(withId(R.id.button2)).perform(click())
        IdlingRegistry.getInstance().register(mIdlingResource)
        onView(withId(R.id.textView)).check(matches(withText("button 2 clicked")))
        IdlingRegistry.getInstance().unregister(mIdlingResource)
    }
}