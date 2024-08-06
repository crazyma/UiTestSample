package tw.com.batu.uitestsample.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tw.com.batu.uitestsample.model.Persona

interface PersonaService {
    // https://stage.dcard.io/v2/personas/badu333
    @GET("v2/personas/{user}")
    fun listRepos(@Path("user") user: String = "badu333"): Call<Persona>

    @FormUrlEncoded
    @POST("user/edit/{user}")
    fun updateUser(
        @Path("user") user: String = "badu333",
        @Field("uid") uid: String?,
        @Field("nickname") nickname: String?,
    ): Call<Persona>
}