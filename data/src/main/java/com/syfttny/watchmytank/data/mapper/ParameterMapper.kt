package com.syfttny.watchmytank.data.mapper

import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.WaterParameterLog

// REMOVED conflicting extension function that mapped the (now different) ParameterLogEntity to the OLD WaterParameterLog model.
// // Maps Database Entity to Domain Model
// fun ParameterLogEntity.toDomainModel(): WaterParameterLog {
//     return WaterParameterLog(
//         id = this.id,
//         timestamp = this.timestamp,
//         parameterType = this.parameterType,
//         value = this.value,
//         notes = this.notes
//     )
// }

// Maps Domain Model (OLD) to Database Entity (OLD structure, currently named ParameterLogEntity)
// TODO: Clean this up when removing old parameter logic entirely. The return type ParameterLogEntity no longer matches the structure expected by WaterParameterLog.
fun WaterParameterLog.toEntity(): ParameterLogEntity {
    // This implementation is now likely incorrect as ParameterLogEntity structure has changed.
    // Keeping it temporarily to avoid breaking other potential old usages.
    // A proper fix would involve renaming the old entity or removing this mapper entirely.
    throw IllegalStateException("WaterParameterLog.toEntity mapper is outdated due to ParameterLogEntity refactor.")
    /*return ParameterLogEntity(
        id = this.id, // Use existing ID for updates, 0 for inserts (Room handles autoGenerate)
        // ... other fields are now incompatible ...
        // timestamp = this.timestamp,
        // parameterType = this.parameterType,
        // value = this.value,
        // notes = this.notes
    )*/
} 