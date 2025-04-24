package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.WaterParameterLog

/**
 * Use case for logging a single water parameter reading.
 */
interface LogParameterUseCase {
    suspend operator fun invoke(log: WaterParameterLog)
} 