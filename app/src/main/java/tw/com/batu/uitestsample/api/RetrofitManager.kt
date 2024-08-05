package tw.com.batu.uitestsample.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RetrofitManager {

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://stage.dcard.io/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(httpJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun getPersonaService(): PersonaService {
        return retrofit.create(PersonaService::class.java)
    }

}

val httpJson = Json {
    allowSpecialFloatingPointValues = true
    encodeDefaults = true
    explicitNulls = false
    ignoreUnknownKeys = true
    isLenient = true
    useArrayPolymorphism = true
    coerceInputValues = true
}

