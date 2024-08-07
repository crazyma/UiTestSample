package tw.com.batu.uitestsample

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tw.com.batu.uitestsample.api.RetrofitManager

@RunWith(AndroidJUnit4::class)
class AutoRetrofitTest {

    private var mockServerTakeRequestJob : Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private val mockServer = MockWebServer()
    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            Log.d("badu", "[AutoRetrofitTest] request.method: ${request.method}")

            return when (request.method) {
                "GET" -> handleGetRequest(request)
                "POST" -> handlePostRequest(request)
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    @Before
    fun setup(){
        Log.d("badu", "[AutoRetrofitTest] setup auto retrofit test")
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        setupMockServer()
        mockServerTakeRequestJob = coroutineScope.launch {
            while(true){

                Log.d("badu", "[AutoRetrofitTest] start an await request")
                try {
                    mockServer.takeRequest().let {
                        Log.d("badu", "[AutoRetrofitTest] body: ${it.body.readUtf8()}")
                        assertEquals("/user/edit/badu333", it.path)
                        assertEquals("POST", it.method)
                    }
                } catch (e: Exception) {
                    /* no-op */
                }

                Log.d("badu", "[AutoRetrofitTest] a take request done")

                if(!isActive){
                    break
                }
            }
        }
    }

    @After
    fun clear(){
        mockServer.shutdown()
        mockServerTakeRequestJob?.cancel()
        activityScenario.close()
    }

    @Test
    fun clickRequestButton(){
        onView(withId(R.id.requestButton)).perform(click())
        Thread.sleep(1_000)

        onView(withId(R.id.requestButton2)).perform(click())
        Thread.sleep(1_000)
    }

    private fun setupMockServer(){
        mockServer.dispatcher = dispatcher
        mockServer.start()
        val httpUrl = mockServer.url("/")
        RetrofitManager.setup(httpUrl)
    }

    private fun handleGetRequest(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/v2/personas/badu333" -> {
                MockResponse().setResponseCode(200).setBody(
                    "{\"uid\":\"badu333-TEST\",\"nickname\":\"Badu33-TEST\",\"gender\":\"M\"}"
                )
            }

            else -> {
                MockResponse().setResponseCode(404)
            }
        }
    }

    private fun handlePostRequest(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/user/edit/badu333" -> {
                MockResponse().setResponseCode(201).setBody(
                    "{\"uid\":\"badu333-EDIT\",\"nickname\":\"Badu33-EDIT\",\"gender\":\"M\"}"
                )
            }

            else -> {
                MockResponse().setResponseCode(404)
            }
        }
    }
}