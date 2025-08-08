package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import kotlinx.coroutines.flow.Flow

interface WaterParameterRepository {

    fun getParameterHistory(type: ParameterType): Flow<List<WaterParameterLog>>
    fun getUnsyncedLogCount(): Flow<Int>
    suspend fun logParameter(log: WaterParameterLog)
} 