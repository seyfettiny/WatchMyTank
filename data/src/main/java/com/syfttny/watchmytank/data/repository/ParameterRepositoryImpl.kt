package com.syfttny.watchmytank.data.repository

import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.mapper.toDomainModel
import com.syfttny.watchmytank.data.mapper.toEntity
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Assuming repository should be a singleton
class ParameterRepositoryImpl @Inject constructor(
    private val parameterDao: ParameterDao
) : ParameterRepository {

    override suspend fun logParameter(log: WaterParameterLog) {
        parameterDao.insertLog(log.toEntity())
    }

    override fun getParameterHistory(parameterType: ParameterType): Flow<List<WaterParameterLog>> {
        // Get the flow of entities directly from the DAO using the specific type
        val entityFlow = parameterDao.getParameterHistory(parameterType)

        // Map the Flow<List<Entity>> to Flow<List<DomainModel>>
        return entityFlow.map { entityList ->
            entityList.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    // Implementation for delete/update would go here if added to the interface
} 