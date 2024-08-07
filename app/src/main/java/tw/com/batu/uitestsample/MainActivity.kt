package tw.com.batu.uitestsample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource
import kotlinx.coroutines.runBlocking

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.com.batu.uitestsample.api.RetrofitManager
import tw.com.batu.uitestsample.databinding.ActivityMainBinding
import tw.com.batu.uitestsample.model.Persona

class MainActivity : ComponentActivity() {

    private val countingIdlingResource = CountingIdlingResource("network_call")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitManager.setup()

        with(binding) {
            button.setOnClickListener {
                apiRequest()
                textView.text = "button 1 clicked"
            }

            button2.setOnClickListener {
                textView.text = "button 2 clicked"
            }

            requestButton.setOnClickListener {
                postRequest1()
            }

            requestButton2.setOnClickListener {
                postRequest2()
            }
        }
        countingIdlingResource.increment()
        binding.root.postDelayed({
            countingIdlingResource.decrement()
        }, 5000)
    }

    @VisibleForTesting
    fun getIdlingResource(): CountingIdlingResource {
        return countingIdlingResource
    }

    private fun apiRequest(){
        RetrofitManager.getPersonaService().listRepos().enqueue(object : Callback<Persona> {
            override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                val personas = response.body()
                personas?.let {
                    Log.d("badu", "[MainActivity] Get Response : $it")
                }
            }

            override fun onFailure(call: Call<Persona>, t: Throwable) {
                Log.e("badu", "[MainActivity] Get Failure Response : ${t.message}")
            }
        })
    }

    private fun postRequest1(){
        Log.d("badu", "[MainActivity] post request 1")
        RetrofitManager.getPersonaService().updateUser(
            uid = "post_uid_from_activity",
            nickname = "post_nickname_from_activity"
        ).enqueue(object: Callback<Persona>{
            override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                val personas = response.body()
                personas?.let {
                    Log.d("badu", "[MainActivity] Get Response : $it")
                }
            }

            override fun onFailure(call: Call<Persona>, t: Throwable) {
                Log.e("badu", "[MainActivity] Get Failure Response : ${t.message}")
            }
        })
    }

    private fun postRequest2(){
        Log.d("badu", "[MainActivity] post request 2")
        RetrofitManager.getPersonaService().updateUser(
            uid = "post_uid_from_activity_2",
            nickname = "post_nickname_from_activity_2"
        ).enqueue(object: Callback<Persona>{
            override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                val personas = response.body()
                personas?.let {
                    Log.d("badu", it.toString())
                }
            }

            override fun onFailure(call: Call<Persona>, t: Throwable) {
                Log.e("badu", t.message.toString())
            }
        })
    }
}