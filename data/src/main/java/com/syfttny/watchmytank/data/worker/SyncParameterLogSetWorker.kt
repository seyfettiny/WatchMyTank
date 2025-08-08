package com.syfttny.watchmytank.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.syfttny.watchmytank.data.local.dao.ParameterDao
import com.syfttny.watchmytank.data.mapper.toDto
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class SyncParameterLogSetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val parameterDao: ParameterDao,
    private val firestore: FirebaseFirestore
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "SyncParameterLogSetWorker"
        
        private const val FIRESTORE_COLLECTION = "parameter_log_sets"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sync work for parameter log sets...")

        try {
            val unsyncedLogSets = parameterDao.getUnsyncedLogSets()
            if (unsyncedLogSets.isEmpty()) {
                Log.d(TAG, "No unsynced log sets found. Work finished successfully.")
                return Result.success()
            }

            Log.d(TAG, "Found ${unsyncedLogSets.size} unsynced log sets. Attempting upload...")

            val collectionRef = firestore.collection(FIRESTORE_COLLECTION)

            
            for (entity in unsyncedLogSets) {
                val logSetDto = entity.toDto() 
                try {
                    
                    collectionRef.add(logSetDto).await()

                    
                    parameterDao.markLogSetAsSynced(entity.id)
                    Log.d(TAG, "Successfully synced log set ID: ${entity.id}")

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Error syncing log set ID: ${entity.id} to Firestore. Error: ${e.message}",
                        e
                    )
                    
                    
                    return Result.retry()
                }
            }

            Log.d(TAG, "All unsynced log sets processed successfully.")
            return Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching unsynced log sets from DAO: ${e.message}", e)
            
            return Result.failure()
        }
    }
} 