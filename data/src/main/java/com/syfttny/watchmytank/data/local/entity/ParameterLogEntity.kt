package com.syfttny.watchmytank.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.syfttny.watchmytank.domain.model.ParameterType
import java.time.LocalDateTime

/**
 * Room Entity representing a single logged water parameter reading.
 * Includes indices on timestamp and parameter type for efficient querying.
 */
@Entity(
    tableName = "water_parameter_logs",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["parameterType"])
    ]
)
data class ParameterLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: LocalDateTime,
    val parameterType: ParameterType, // Stored as String via TypeConverter
    val value: Double,
    val notes: String? = null
) 