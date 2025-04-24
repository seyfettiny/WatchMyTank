package com.syfttny.watchmytank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ParameterLogEntity)

    /**
     * Gets a stream of historical data for a specific parameter type, ordered by timestamp descending.
     * Uses the TypeConverter for ParameterType automatically.
     */
    @Query("SELECT * FROM water_parameter_logs WHERE parameterType = :parameterType ORDER BY timestamp DESC")
    fun getParameterHistory(parameterType: ParameterType): Flow<List<ParameterLogEntity>>

    // TODO: Add delete/update methods if required by the repository interface later.
    // @Query(\"DELETE FROM water_parameter_logs WHERE id = :logId\")
    // suspend fun deleteLogById(logId: Long)

    // @Query(\"DELETE FROM water_parameter_logs WHERE parameterType = :parameterType\")
    // suspend fun deleteLogsByType(parameterType: ParameterType)
} 