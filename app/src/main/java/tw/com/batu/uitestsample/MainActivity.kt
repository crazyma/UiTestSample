package tw.com.batu.uitestsample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.com.batu.uitestsample.api.RetrofitManager
import tw.com.batu.uitestsample.model.Persona

class MainActivity: ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitManager.setup()

        findViewById<Button>(R.id.button).setOnClickListener {
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
}