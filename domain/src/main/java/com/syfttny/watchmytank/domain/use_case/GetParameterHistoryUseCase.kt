package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the historical logs for a specific water parameter.
 * NOTE: This use case is likely deprecated in favor of GetParameterLogSetsUseCase.
 */
interface GetParameterHistoryUseCase {
    operator fun invoke(parameterType: ParameterType): Flow<List<ParameterLog>>
} 