package com.syfttny.watchmytank.domain.use_case

import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a continuous flow of the count of unsynced parameter logs.
 */
interface GetUnsyncedLogCountUseCase {
    operator fun invoke(): Flow<Int>
} 