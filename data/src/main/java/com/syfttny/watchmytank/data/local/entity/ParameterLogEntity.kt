package com.syfttny.watchmytank.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
// No specific converter imports needed here if handled globally by @TypeConverters on AppDatabase
// import com.syfttny.watchmytank.data.local.converter.InstantConverter 
// import com.syfttny.watchmytank.data.local.converter.ParameterMapConverter 
import java.time.Instant

/**
 * Room Entity representing a set of parameter readings logged at a specific time.
 *
 * @param id Auto-generated primary key.
 * @param tankId Foreign key / identifier for the tank.
 * @param userId Identifier for the user.
 * @param timestamp The time the log was created.
 * @param parametersJson JSON String representation of the Map<ParameterType, Double>.
 * @param notes Optional user notes.
 * @param isSynced Flag indicating if this log set has been synced with the remote backend.
 */
@Entity(tableName = "parameter_log_sets") // Use a descriptive table name
data class ParameterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) // Index for faster lookups by tank
    val tankId: String,
    val userId: String,
    val timestamp: Instant,
    val parametersJson: String, // Store the map as JSON
    val notes: String?,
    val isSynced: Boolean = false // Default to not synced when created locally
) 