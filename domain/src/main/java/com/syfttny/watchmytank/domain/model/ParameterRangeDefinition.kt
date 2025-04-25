package com.syfttny.watchmytank.domain.model

/**
 * Defines the acceptable, warning, and danger ranges for a specific water parameter type.
 * Thresholds define the *upper bound* of the lower ranges and the *lower bound*
 * of the upper ranges.
 *
 * Example for pH (hypothetical):
 * lowDangerMax = 6.0 (Values <= 6.0 are low danger)
 * lowWarningMax = 6.5 (Values > 6.0 and <= 6.5 are low warning)
 * idealMin = 6.6
 * idealMax = 7.8 (Values >= 6.6 and <= 7.8 are ideal)
 * highWarningMin = 7.9 (Values >= 7.9 and < 8.5 are high warning)
 * highDangerMin = 8.5 (Values >= 8.5 are high danger)
 *
 * @param parameterType The parameter these ranges apply to.
 * @param lowDangerMax The upper limit of the low danger zone.
 * @param lowWarningMax The upper limit of the low warning zone.
 * @param idealMin The lower limit of the ideal zone.
 * @param idealMax The upper limit of the ideal zone.
 * @param highWarningMin The lower limit of the high warning zone.
 * @param highDangerMin The lower limit of the high danger zone.
 */
data class ParameterRangeDefinition(
    val parameterType: ParameterType,
    val lowDangerMax: Double? = null, // Nullable if no lower danger zone exists
    val lowWarningMax: Double? = null,
    val idealMin: Double,
    val idealMax: Double,
    val highWarningMin: Double? = null,
    val highDangerMin: Double? = null // Nullable if no upper danger zone exists
) 