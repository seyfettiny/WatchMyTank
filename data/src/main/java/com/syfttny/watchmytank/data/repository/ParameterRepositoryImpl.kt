package com.syfttny.watchmytank.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.mapper.toEntity
import com.syfttny.watchmytank.data.mapper.toDomainModel
import com.syfttny.watchmytank.data.worker.SyncParameterLogSetWorker
import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParameterRepositoryImpl @Inject constructor(
    private val parameterDao: ParameterDao,
    @ApplicationContext private val context: Context
) : ParameterRepository {

    private val workManager = WorkManager.getInstance(context)

    private fun enqueueSyncWorker() {
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val syncRequest = OneTimeWorkRequestBuilder<SyncParameterLogSetWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            "syncParameterLogSets",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
        println("Enqueued SyncParameterLogSetWorker.")
    }

    override suspend fun saveParameterLog(parameterLog: ParameterLog) {
        val entity = parameterLog.toEntity()
        parameterDao.insertParameterLogSet(entity)
        println("ParameterRepositoryImpl: Saved ParameterLogSet to Room (ID: ${entity.id} - might be 0 if new)")
        enqueueSyncWorker()
    }

    override fun getParameterLogSets(tankId: String): Flow<List<ParameterLog>> {
        return parameterDao.getParameterLogSetsForTank(tankId)
            .map { entityList ->
                entityList.map { entity ->
                    entity.toDomainModel() 
                }
            }
    }
} 