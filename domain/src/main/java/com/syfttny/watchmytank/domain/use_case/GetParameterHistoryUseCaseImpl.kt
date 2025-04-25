package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetParameterHistoryUseCaseImpl @Inject constructor(
    private val repository: ParameterRepository
) : GetParameterHistoryUseCase {

    override fun invoke(parameterType: ParameterType): Flow<List<ParameterLog>> {
        throw UnsupportedOperationException(
            "GetParameterHistoryUseCaseImpl is likely deprecated and needs tankId. " +
            "Use GetParameterLogSetsUseCase instead."
        )
    }
} 