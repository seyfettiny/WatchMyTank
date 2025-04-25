package com.syfttny.watchmytank.data.mapper

import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.data.remote.dto.ParameterLogDto
import com.syfttny.watchmytank.domain.model.ParameterType

// Define Gson and TypeToken locally for this mapper
private val gson = Gson()
private val mapTypeToken = object : TypeToken<Map<ParameterType, Double>>() {}.type

/**
 * Converts a [ParameterLogEntity] (Room entity) to a [ParameterLogDto] (Firestore DTO).
 */
fun ParameterLogEntity.toDto(): ParameterLogDto {
    // Parse the JSON string back to the map
    val parametersMap: Map<ParameterType, Double> = try {
        gson.fromJson(this.parametersJson, mapTypeToken)
            ?: emptyMap()
    } catch (e: Exception) {
        println("Error parsing parametersJson for DTO conversion (entity id ${this.id}): ${e.message}")
        emptyMap()
    }

    // Convert Map<ParameterType, Double> to Map<String, Double>
    val parametersStringMap = parametersMap.mapKeys { it.key.name }

    return ParameterLogDto(
        tankId = this.tankId,
        userId = this.userId,
        // Let Firestore generate the timestamp (@ServerTimestamp handles this)
        timestamp = null,
        parameters = parametersStringMap,
        notes = this.notes
    )
}

// Note: We might need a mapper from ParameterLog directly to DTO as well,
// depending on where the conversion happens (e.g., if repository saves directly)
// fun ParameterLog.toDto(): ParameterLogDto { ... } 