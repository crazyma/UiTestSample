package tw.com.batu.uitestsample.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import tw.com.batu.uitestsample.model.Persona

interface PersonaService {
    // https://stage.dcard.io/v2/personas/badu333
    @GET("v2/personas/{user}")
    fun listRepos(@Path("user") user: String = "badu333"): Call<Persona>
}