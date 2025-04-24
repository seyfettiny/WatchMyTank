package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.model.WaterParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParameterHistoryUseCaseImpl @Inject constructor(
    private val repository: ParameterRepository
) : GetParameterHistoryUseCase {

    override fun invoke(parameterType: ParameterType): Flow<List<WaterParameterLog>> {
        return repository.getParameterHistory(parameterType)
    }
} 