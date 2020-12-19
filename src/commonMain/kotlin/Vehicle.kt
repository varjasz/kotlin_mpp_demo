package hu.vanio.kotlin_mpp_demo

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val registrationNumber: String,
    val vinNumber: String,
    val make: String,
    val type: String
)