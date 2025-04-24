package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.ParameterType
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
     * @param parameterType The type of parameter to retrieve history for.
     * @return A Flow emitting a list of logs for the requested parameter, ordered by timestamp descending.
     */
    fun getParameterHistory(parameterType: ParameterType): Flow<List<WaterParameterLog>>

    // TODO: Add methods for deleting or updating logs if needed.
    // suspend fun deleteParameterLog(logId: Long)
} 