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
    suspend fun insertParameterLogSet(logSet: ParameterLogEntity)

    @Query("SELECT * FROM parameter_log_sets WHERE tankId = :tankId ORDER BY timestamp DESC")
    fun getParameterLogSetsForTank(tankId: String): Flow<List<ParameterLogEntity>>

    @Query("SELECT * FROM parameter_log_sets WHERE isSynced = 0")
    suspend fun getUnsyncedLogSets(): List<ParameterLogEntity>

    @Query("UPDATE parameter_log_sets SET isSynced = 1 WHERE id = :logSetId")
    suspend fun markLogSetAsSynced(logSetId: Long)
} 