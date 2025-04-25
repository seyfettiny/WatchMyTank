package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for accessing water parameter data.
 */
interface WaterParameterRepository {

    /**
     * Retrieves the historical logs for a specific parameter type.
     *
     * @param type The type of parameter to retrieve history for.
     * @return A Flow emitting a list of logs for the given type, ordered chronologically.
     */
    fun getParameterHistory(type: ParameterType): Flow<List<WaterParameterLog>>

    /**
     * Retrieves the count of water parameter logs that have not yet been synced
     * with a remote backend (if applicable).
     *
     * @return A Flow emitting the number of unsynced logs.
     */
    fun getUnsyncedLogCount(): Flow<Int>

    /**
     * Saves a new water parameter log entry.
     * This might save locally first and then attempt synchronization.
     *
     * @param log The WaterParameterLog object to save.
     */
    suspend fun logParameter(log: WaterParameterLog)

    // Add other methods as needed, e.g., for fetching all history, deleting logs, etc.
} 