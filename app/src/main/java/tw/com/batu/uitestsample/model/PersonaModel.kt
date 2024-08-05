package tw.com.batu.uitestsample.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Persona(
    @SerialName("uid") var uid: String,
    @SerialName("nickname") var nickname: String,
    @SerialName("gender") var gender: String? = null,
)