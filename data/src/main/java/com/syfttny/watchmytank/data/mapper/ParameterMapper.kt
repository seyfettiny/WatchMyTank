package com.syfttny.watchmytank.data.mapper

import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.WaterParameterLog

// Maps Database Entity to Domain Model
fun ParameterLogEntity.toDomainModel(): WaterParameterLog {
    return WaterParameterLog(
        id = this.id,
        timestamp = this.timestamp,
        parameterType = this.parameterType,
        value = this.value,
        notes = this.notes
    )
}

// Maps Domain Model to Database Entity (needed for saving/updating)
fun WaterParameterLog.toEntity(): ParameterLogEntity {
    return ParameterLogEntity(
        id = this.id, // Use existing ID for updates, 0 for inserts (Room handles autoGenerate)
        timestamp = this.timestamp,
        parameterType = this.parameterType,
        value = this.value,
        notes = this.notes
    )
} 