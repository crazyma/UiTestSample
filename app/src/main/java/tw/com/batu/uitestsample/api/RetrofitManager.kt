package tw.com.batu.uitestsample.api

import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitManager {

    lateinit var retrofit: Retrofit

    fun setup(baseUrl: String = "https://stage.dcard.io/") {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(httpJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun setup(baseHttpUrl: HttpUrl) {
        retrofit = Retrofit.Builder()
            .baseUrl(baseHttpUrl)
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

