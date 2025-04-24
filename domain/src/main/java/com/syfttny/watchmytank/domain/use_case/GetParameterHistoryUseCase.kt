package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the historical data for a specific water parameter type.
 */
interface GetParameterHistoryUseCase {
    operator fun invoke(parameterType: ParameterType): Flow<List<WaterParameterLog>>
} 