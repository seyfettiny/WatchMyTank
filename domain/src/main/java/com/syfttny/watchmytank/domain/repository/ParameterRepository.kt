package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.ParameterLog
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for managing water parameter log sets.
 * This interface focuses on the new model where multiple parameters are logged together.
 */
interface ParameterRepository {

    /**
     * Saves a new set of parameter readings.
     *
     * @param parameterLog The log entry set to save.
     * @throws Exception if saving fails (e.g., database error, validation error handled by use case).
     */
    suspend fun saveParameterLog(parameterLog: ParameterLog)

    /**
     * Gets a Flow emitting the list of all parameter log sets for a specific tank,
     * ordered by timestamp descending.
     *
     * @param tankId The ID of the tank to retrieve logs for.
     * @return A Flow emitting a list of [ParameterLog].
     */
    fun getParameterLogSets(tankId: String): Flow<List<ParameterLog>>

    // TODO: Add methods for deleting or updating log sets if needed.
    // suspend fun deleteParameterLogSet(logSetId: String)

    // TODO: Define methods for interacting with the sync status/worker if needed at this level.
    // For example, getting unsynced sets might be an internal detail of the data layer implementation
    // used by a sync worker, rather than part of the public repository interface.
} 