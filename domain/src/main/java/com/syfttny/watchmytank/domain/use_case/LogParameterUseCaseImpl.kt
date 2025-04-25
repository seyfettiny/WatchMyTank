package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import javax.inject.Inject

class LogParameterUseCaseImpl @Inject constructor(
    private val repository: ParameterRepository
) : LogParameterUseCase {

    override suspend fun invoke(log: ParameterLog) {
        // TODO: Add validation logic here if needed (e.g., check value ranges for type)
        repository.saveParameterLog(log)
    }
} 