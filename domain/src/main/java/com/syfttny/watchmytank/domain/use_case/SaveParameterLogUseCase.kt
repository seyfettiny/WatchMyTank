package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import java.time.Instant
import javax.inject.Inject

class SaveParameterLogUseCase @Inject constructor(
    private val parameterRepository: ParameterRepository
) {
    suspend operator fun invoke(
        tankId: String,
        userId: String, 
        parameterValues: Map<ParameterType, String>,
        notes: String?
    ) {
        if (tankId.isBlank()) throw IllegalArgumentException("Tank ID cannot be blank")
        if (userId.isBlank()) throw IllegalArgumentException("User ID cannot be blank")

        
        val measuredParameters = parameterValues
            .filter { it.value.isNotBlank() } 
            .mapValues { (_, valueString) ->
                valueString.toDoubleOrNull()
                    ?: throw NumberFormatException("Invalid number format: $valueString")
            }

        
        if (measuredParameters.isEmpty()) {
            
            
            println("No parameters measured, skipping save.")
            return
        }

        val logEntry = ParameterLog(
            tankId = tankId,
            userId = userId,
            timestamp = Instant.now(), 
            parameters = measuredParameters,
            notes = notes?.takeIf { it.isNotBlank() } 
        )

        parameterRepository.saveParameterLog(logEntry)
    }
} 