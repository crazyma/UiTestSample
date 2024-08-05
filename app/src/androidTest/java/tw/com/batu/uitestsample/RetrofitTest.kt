package tw.com.batu.uitestsample

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import tw.com.batu.uitestsample.api.RetrofitManager

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RetrofitTest {

    val mockServer = MockWebServer()

    @Before
    fun setUp() {

        val mockedResponse = MockResponse().setResponseCode(200).setBody(
            "{\"uid\":\"badu333-TEST\",\"nickname\":\"Badu33-TEST\",\"gender\":\"M\"}"
        )
        mockServer.enqueue(mockedResponse)
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
        Log.i("badu", "ZZZZZZ  ${recordedRequest.path}")

        assertEquals("/v2/personas/badu333", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}