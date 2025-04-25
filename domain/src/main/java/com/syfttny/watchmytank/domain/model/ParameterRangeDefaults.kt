package com.syfttny.watchmytank.domain.model

/**
 * Provides default range definitions for common water parameters,
 * primarily based on typical freshwater aquarium guidelines.
 *
 * These can be used as a starting point and potentially overridden by user settings
 * or tank-specific profiles in the future.
 */
object ParameterRangeDefaults {

    // Note: These are general guidelines. Specific fish/plants may have different needs.

    val TEMPERATURE_CELSIUS = ParameterRangeDefinition(
        parameterType = ParameterType.TEMPERATURE,
        lowDangerMax = 22.0,
        lowWarningMax = 24.0,
        idealMin = 24.0,
        idealMax = 27.0,
        highWarningMin = 27.0,
        highDangerMin = 29.0
    )

    val PH = ParameterRangeDefinition(
        parameterType = ParameterType.PH,
        lowDangerMax = 6.5,
        lowWarningMax = 6.8,
        idealMin = 6.8,
        idealMax = 7.8,
        highWarningMin = 7.8,
        highDangerMin = 8.2
    )

    val AMMONIA_PPM = ParameterRangeDefinition(
        parameterType = ParameterType.AMMONIA,
        // No low danger/warning for Ammonia, lower is always better
        idealMin = 0.0,
        idealMax = 0.0, // Ideally exactly 0
        highWarningMin = 0.01, // Technically any amount > 0 can be stressful
        highDangerMin = 0.25 // Generally considered toxic
    )

    val NITRITE_PPM = ParameterRangeDefinition(
        parameterType = ParameterType.NITRITE,
        // No low danger/warning for Nitrite
        idealMin = 0.0,
        idealMax = 0.0,
        highWarningMin = 0.01,
        highDangerMin = 0.25 // Toxic, especially during cycling
    )

    val NITRATE_PPM = ParameterRangeDefinition(
        parameterType = ParameterType.NITRATE,
        // Low nitrate isn't typically dangerous
        idealMin = 0.0,
        idealMax = 20.0, // Keep below 20-40ppm for most freshwater
        highWarningMin = 20.0,
        highDangerMin = 40.0 // Levels above 40ppm can become harmful long-term
    )

    // TODO: Add defaults for GH, KH, Phosphate, Salinity, Calcium, Magnesium etc.
    // These might need variants for Saltwater vs Freshwater.

    /**
     * A map to easily access the default range definition for a given parameter type.
     */
    val defaultsMap: Map<ParameterType, ParameterRangeDefinition> = ParameterType.values().associateWith {
        when (it) {
            ParameterType.TEMPERATURE -> TEMPERATURE_CELSIUS
            ParameterType.PH -> PH
            ParameterType.AMMONIA -> AMMONIA_PPM
            ParameterType.NITRITE -> NITRITE_PPM
            ParameterType.NITRATE -> NITRATE_PPM
            // Add cases for other parameters as their defaults are defined
            else -> {
                // Provide a fallback or throw an error for unhandled types
                // For now, creating a dummy ideal range to avoid nulls during development
                // Ideally, ensure all ParameterTypes listed in the enum have defaults.
                ParameterRangeDefinition(it, idealMin = 0.0, idealMax = 100.0) // Placeholder!
            }
        }
    }

    fun getRangeDefinition(parameterType: ParameterType): ParameterRangeDefinition? {
        return defaultsMap[parameterType]
    }
} 