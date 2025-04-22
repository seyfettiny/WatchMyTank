package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.WaterParameterLog
import kotlinx.coroutines.flow.Flow

// TODO: Consider adding Result<Unit, Error> or similar for logParameter for better error handling.
interface ParameterRepository {
    /**
     * Logs a new water parameter reading.
     * @param log The parameter log entry to save.
     */
    suspend fun logParameter(log: WaterParameterLog)

    /**
     * Gets a stream of historical data for a specific parameter type.
     * @param parameterType The string key of the parameter (e.g., "pH", "temperature").
     *                      // TODO: Replace String with ParameterType enum once defined.
     * @return A Flow emitting a list of logs for the requested parameter, ordered by timestamp.
     */
    fun getParameterHistory(parameterType: String): Flow<List<WaterParameterLog>>

    // TODO: Add methods for deleting or updating logs if needed.
    // suspend fun deleteParameterLog(logId: String)
} 