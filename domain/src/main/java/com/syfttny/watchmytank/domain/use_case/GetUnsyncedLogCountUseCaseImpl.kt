package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnsyncedLogCountUseCaseImpl @Inject constructor(
    private val repository: ParameterRepository
) : GetUnsyncedLogCountUseCase {

    override fun invoke(): Flow<Int> {
        return repository.getUnsyncedLogCount()
    }
} 