package com.syfttny.watchmytank.domain.model

import java.time.Instant

/**
 * Represents a single set of water parameter readings taken at a specific time.
 *
 * @param id A unique identifier for this log entry (can be assigned by the database).
 * @param tankId The identifier of the tank these parameters belong to.
 * @param userId The identifier of the user who logged these parameters.
 * @param timestamp The exact date and time when the parameters were measured.
 * @param parameters A map where the key is the type of parameter measured and the value is the reading.
 *                   Only includes parameters that were actually measured in this session.
 * @param notes Optional user notes associated with this set of readings.
 */
data class ParameterLog(
    val id: String? = null, // Nullable for creation before DB assigns ID
    val tankId: String, // Assuming tank IDs are strings
    val userId: String, // Assuming user IDs are strings
    val timestamp: Instant,
    val parameters: Map<ParameterType, Double>,
    val notes: String? = null
) 