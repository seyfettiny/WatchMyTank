package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog

/**
 * Use case for logging a new set of water parameter readings.
 */
interface LogParameterUseCase {
    suspend operator fun invoke(log: ParameterLog)
} 