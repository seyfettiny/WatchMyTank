package com.syfttny.watchmytank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for parameter logs.
 * Handles interaction with the `parameter_log_sets` table.
 */
@Dao
interface ParameterDao {

    /**
     * Inserts a new set of parameter readings into the local database.
     * If a log set with the same primary key already exists, it will be replaced.
     *
     * @param logSet The [ParameterLogEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParameterLogSet(logSet: ParameterLogEntity)

    // --- Methods for retrieving parameter log sets (New Structure) ---

    /**
     * Gets a Flow emitting the list of all parameter log sets for a specific tank,
     * ordered by timestamp descending.
     *
     * @param tankId The ID of the tank to retrieve logs for.
     * @return A Flow emitting the list of [ParameterLogEntity].
     */
    @Query("SELECT * FROM parameter_log_sets WHERE tankId = :tankId ORDER BY timestamp DESC")
    fun getParameterLogSetsForTank(tankId: String): Flow<List<ParameterLogEntity>>

    /**
     * Retrieves all parameter log sets that have not yet been synced.
     * Used by the sync worker.
     *
     * @return A list of unsynced [ParameterLogEntity].
     */
    @Query("SELECT * FROM parameter_log_sets WHERE isSynced = 0")
    suspend fun getUnsyncedLogSets(): List<ParameterLogEntity>

    /**
     * Marks a specific parameter log set as synced.
     *
     * @param logSetId The ID of the log set to mark as synced.
     */
    @Query("UPDATE parameter_log_sets SET isSynced = 1 WHERE id = :logSetId")
    suspend fun markLogSetAsSynced(logSetId: Long)

    // TODO: Add methods for deleting or updating log sets if needed.

    // TODO: Consider if getUnsyncedLogCount() is still needed for the new model, and query the correct table.
    // @Query("SELECT COUNT(*) FROM parameter_log_sets WHERE isSynced = 0")
    // fun getUnsyncedLogSetCount(): Flow<Int>

    // --- Deprecated / Old Methods --- //
    // These methods likely operated on the old table structure (e.g., water_parameter_logs)
    // and should be removed or migrated.
    // @Insert(onConflict = OnConflictStrategy.REPLACE)
    // suspend fun insertLog(log: ParameterLogEntity)
    // @Query("SELECT * FROM water_parameter_logs WHERE parameterType = :parameterType ORDER BY timestamp DESC")
    // fun getParameterHistory(parameterType: ParameterType): Flow<List<ParameterLogEntity>>
    // @Query("SELECT * FROM water_parameter_logs WHERE isSynced = 0")
    // suspend fun getUnsyncedLogs(): List<ParameterLogEntity>
    // @Query("UPDATE water_parameter_logs SET isSynced = 1 WHERE id = :logId")
    // suspend fun markLogAsSynced(logId: Long)
    // @Query("SELECT COUNT(*) FROM water_parameter_logs WHERE isSynced = 0")
    // fun getUnsyncedLogCount(): Flow<Int>
} 