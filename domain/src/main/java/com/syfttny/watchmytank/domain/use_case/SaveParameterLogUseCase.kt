package com.syfttny.watchmytank.domain.use_case

import com.syfttny.watchmytank.domain.model.ParameterLog
import com.syfttny.watchmytank.domain.model.ParameterType
import com.syfttny.watchmytank.domain.repository.ParameterRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Use case for saving a set of parameter log readings.
 */
class SaveParameterLogUseCase @Inject constructor(
    private val parameterRepository: ParameterRepository
) {

    /**
     * Executes the use case.
     *
     * @param tankId The ID of the tank the log belongs to.
     * @param userId The ID of the user saving the log.
     * @param parameterValues A map of parameter types to their STRING representations from the UI.
     * @param notes Optional notes from the user.
     * @throws IllegalArgumentException if tankId or userId is blank.
     * @throws NumberFormatException if any parameter value cannot be parsed to Double.
     * @throws Exception if saving via the repository fails.
     */
    suspend operator fun invoke(
        tankId: String,
        userId: String, // Assuming we get this from auth state later
        parameterValues: Map<ParameterType, String>,
        notes: String?
    ) {
        if (tankId.isBlank()) throw IllegalArgumentException("Tank ID cannot be blank")
        if (userId.isBlank()) throw IllegalArgumentException("User ID cannot be blank")

        // Filter out empty values and parse valid ones to Double
        val measuredParameters = parameterValues
            .filter { it.value.isNotBlank() } // Only include non-empty strings
            .mapValues { (_, valueString) ->
                valueString.toDoubleOrNull()
                    ?: throw NumberFormatException("Invalid number format: $valueString")
            }

        // Do not save if no parameters were actually measured
        if (measuredParameters.isEmpty()) {
            // Optionally, throw an exception or return a specific result code
            // For now, just don't save anything.
            println("No parameters measured, skipping save.")
            return
        }

        val logEntry = ParameterLog(
            tankId = tankId,
            userId = userId,
            timestamp = Instant.now(), // Use current time
            parameters = measuredParameters,
            notes = notes?.takeIf { it.isNotBlank() } // Save notes only if not blank
        )

        parameterRepository.saveParameterLog(logEntry)
    }
} 