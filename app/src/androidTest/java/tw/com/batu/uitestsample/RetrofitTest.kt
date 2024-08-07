package tw.com.batu.uitestsample

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tw.com.batu.uitestsample.api.RetrofitManager

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RetrofitTest {

    private val mockServer = MockWebServer()
    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            Log.d("badu", "request.method: ${request.method}")

            return when (request.method) {
                "GET" -> handleGetRequest(request)
                "POST" -> handlePostRequest(request)
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    @Before
    fun setUp() {
        mockServer.dispatcher = dispatcher
        mockServer.start()
        val httpUrl = mockServer.url("/")
        Log.d("badu", "!!! ${httpUrl.toUrl()}")
        RetrofitManager.setup(httpUrl)
    }

    @Test
    fun simpleRetrofitMockServerTest() {
        // Context of the app under test.

        RetrofitManager.getPersonaService().listRepos().execute().let {
            assertTrue(it.isSuccessful)
            assertEquals("badu333-TEST", it.body()?.uid)
            assertEquals("Badu33-TEST", it.body()?.nickname)
        }

        val recordedRequest = mockServer.takeRequest()

        assertEquals("/v2/personas/badu333", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)

        RetrofitManager.getPersonaService().updateUser(
            uid = "post_uid",
            nickname = "post_nickname"
        ).execute().let {
            assertTrue(it.isSuccessful)
            assertEquals("badu333-EDIT", it.body()?.uid)
            assertEquals("Badu33-EDIT", it.body()?.nickname)
        }
        mockServer.takeRequest().let {
            assertEquals("/user/edit/badu333", it.path)
            assertEquals("POST", it.method)
        }
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