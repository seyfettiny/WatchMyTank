package com.syfttny.watchmytank.data.mapper

import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.data.remote.dto.ParameterLogDto
import com.syfttny.watchmytank.domain.model.ParameterType


private val gson = Gson()
private val mapTypeToken = object : TypeToken<Map<ParameterType, Double>>() {}.type

fun ParameterLogEntity.toDto(): ParameterLogDto {
    
    val parametersMap: Map<ParameterType, Double> = try {
        gson.fromJson(this.parametersJson, mapTypeToken)
            ?: emptyMap()
    } catch (e: Exception) {
        println("Error parsing parametersJson for DTO conversion (entity id ${this.id}): ${e.message}")
        emptyMap()
    }

    
    val parametersStringMap = parametersMap.mapKeys { it.key.name }

    return ParameterLogDto(
        tankId = this.tankId,
        userId = this.userId,
        
        timestamp = null,
        parameters = parametersStringMap,
        notes = this.notes
    )
}



