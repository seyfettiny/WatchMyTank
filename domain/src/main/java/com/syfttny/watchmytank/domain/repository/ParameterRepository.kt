package com.syfttny.watchmytank.domain.repository

import com.syfttny.watchmytank.domain.model.ParameterLog
import kotlinx.coroutines.flow.Flow

interface ParameterRepository {
    suspend fun saveParameterLog(parameterLog: ParameterLog)
    fun getParameterLogSets(tankId: String): Flow<List<ParameterLog>>

    
    

    
    
    
} 