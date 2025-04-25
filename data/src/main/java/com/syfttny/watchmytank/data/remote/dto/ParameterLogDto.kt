package com.syfttny.watchmytank.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Data Transfer Object (DTO) for representing a ParameterLog set in Firestore.
 *
 * Uses Firestore's Timestamp and allows for server-side timestamp generation.
 * Parameter names are stored as strings in the map keys.
 */
data class ParameterLogDto(
    // Firestore typically generates the document ID, so we don't include 'id' here usually
    // unless we want to map it back from Firestore.
    val tankId: String = "",
    val userId: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null, // Let Firestore set the timestamp on creation
    val parameters: Map<String, Double> = emptyMap(), // Map ParameterType.name -> Value
    val notes: String? = null
    // We don't store 'isSynced' in Firestore, as its existence implies synced.
) 