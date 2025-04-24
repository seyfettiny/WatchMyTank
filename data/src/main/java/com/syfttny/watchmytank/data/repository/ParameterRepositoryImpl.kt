package com.syfttny.watchmytank.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.mapper.toDomainModel
import com.syfttny.watchmytank.data.mapper.toEntity
import com.syfttny.watchmytank.data.worker.SyncParameterLogWorker
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Assuming repository should be a singleton
class ParameterRepositoryImpl @Inject constructor(
    private val parameterDao: ParameterDao,
    @ApplicationContext private val context: Context // Inject application context
) : ParameterRepository {

    // Get WorkManager instance lazily or directly from context
    private val workManager = WorkManager.getInstance(context)

    override suspend fun logParameter(log: WaterParameterLog) {
        // 1. Convert domain model to entity, ensuring isSynced is false
        val entity = log.toEntity().copy(isSynced = false) // Explicitly set isSynced

        // 2. Insert into local Room database
        parameterDao.insertLog(entity)

        // 3. Enqueue background sync worker
        enqueueSyncWorker()
    }

    private fun enqueueSyncWorker() {
        // Define constraints (e.g., network connectivity)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create a work request for the SyncParameterLogWorker
        val syncRequest = OneTimeWorkRequestBuilder<SyncParameterLogWorker>()
            .setConstraints(constraints)
            // Optional: Add tags, backoff policy, etc.
            // .addTag("parameter_sync")
            // .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()

        // Enqueue the work uniquely to avoid duplicate syncs if one is already running/pending
        workManager.enqueueUniqueWork(
            "syncParameterLogs", // Unique work name
            ExistingWorkPolicy.KEEP, // Keep existing work if it's running or enqueued
            syncRequest
        )
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

    override fun getUnsyncedLogCount(): Flow<Int> {
        return parameterDao.getUnsyncedLogCount()
    }

    // Implementation for delete/update would go here if added to the interface
} 