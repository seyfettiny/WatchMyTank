package com.syfttny.watchmytank.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType


private val gson = Gson()
private val mapTypeToken = object : TypeToken<Map<ParameterType, Double>>() {}.type

fun ParameterLog.toEntity(): ParameterLogEntity {
    
    val parametersJsonString = gson.toJson(this.parameters, mapTypeToken)
    return ParameterLogEntity(
        
        
        tankId = this.tankId,
        userId = this.userId,
        timestamp = this.timestamp,
        parametersJson = parametersJsonString,
        notes = this.notes,
        isSynced = false 
    )
}

fun ParameterLogEntity.toDomainModel(): ParameterLog {
    
    val parametersMap: Map<ParameterType, Double> = try {
        gson.fromJson(this.parametersJson, mapTypeToken)
            ?: emptyMap() 
    } catch (e: Exception) {
        
        println("Error parsing parametersJson for entity id ${this.id}: ${e.message}")
        emptyMap()
    }
    return ParameterLog(
        id = this.id.toString(), 
        tankId = this.tankId,
        userId = this.userId,
        timestamp = this.timestamp,
        parameters = parametersMap,
        notes = this.notes
        
        
    )
} 