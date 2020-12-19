package hu.vanio.kotlin_mpp_demo

import kotlinx.serialization.Serializable

/**
 * Common business model (used both on the client and on the server)
 */
@Serializable
data class Vehicle(
    val registrationNumber: String,
    val vinNumber: String,
    val make: String,
    val type: String
)