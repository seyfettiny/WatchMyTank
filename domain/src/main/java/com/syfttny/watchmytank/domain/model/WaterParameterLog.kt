package com.syfttny.watchmytank.domain.model

import java.util.Date

// TODO: Consider using a more robust Timestamp type if needed (e.g., from Firestore or java.time)
// TODO: Add validation for parameter values (e.g., reasonable ranges for pH, temp)
data class WaterParameterLog(
    val id: String = "", // Document ID from Firestore, generated automatically or explicitly set
    val timestamp: Date = Date(), // Consider Instant for better time zone handling
    val temperature: Double? = null, // Celsius or Fahrenheit? Need unit consistency.
    val pH: Double? = null,
    val ammonia: Double? = null, // ppm?
    val nitrite: Double? = null, // ppm?
    val nitrate: Double? = null, // ppm?
    val notes: String? = null,
    // TODO: Add userId if implementing multi-user support later
    // val userId: String = ""
)

// TODO: Define an Enum for ParameterType (TEMP, PH, AMMONIA, etc.) for type safety
// enum class ParameterType { TEMPERATURE, PH, AMMONIA, NITRITE, NITRATE } 