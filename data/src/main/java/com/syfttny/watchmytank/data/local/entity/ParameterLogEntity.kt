package com.syfttny.watchmytank.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



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
@Entity(tableName = "parameter_log_sets") 
data class ParameterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) 
    val tankId: String,
    val userId: String,
    val timestamp: Instant,
    val parametersJson: String, 
    val notes: String?,
    val isSynced: Boolean = false 
) 