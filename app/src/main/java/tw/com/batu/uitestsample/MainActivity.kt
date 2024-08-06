package tw.com.batu.uitestsample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource

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
                    Log.d("Persona", it.toString())
                }
            }

            override fun onFailure(call: Call<Persona>, t: Throwable) {
                Log.e("Persona", t.message.toString())
            }
        })
    }
}