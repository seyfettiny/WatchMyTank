package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the history of parameter log sets for a specific tank.
 */
class GetParameterLogSetsUseCase @Inject constructor(
    private val parameterRepository: ParameterRepository
) {

    /**
     * Executes the use case.
     *
     * @param tankId The ID of the tank to retrieve logs for.
     * @return A Flow emitting a list of [ParameterLog] ordered by timestamp descending.
     * @throws IllegalArgumentException if tankId is blank.
     */
    operator fun invoke(tankId: String): Flow<List<ParameterLog>> {
        if (tankId.isBlank()) {
            throw IllegalArgumentException("Tank ID cannot be blank.")
        }
        return parameterRepository.getParameterLogSets(tankId)
    }
} 