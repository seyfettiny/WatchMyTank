package com.syfttny.watchmytank.domain.model

object ParameterRangeDefaults {

    

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
        
        idealMin = 0.0,
        idealMax = 0.0, 
        highWarningMin = 0.01, 
        highDangerMin = 0.25 
    )

    val NITRITE_PPM = ParameterRangeDefinition(
        parameterType = ParameterType.NITRITE,
        
        idealMin = 0.0,
        idealMax = 0.0,
        highWarningMin = 0.01,
        highDangerMin = 0.25 
    )

    val NITRATE_PPM = ParameterRangeDefinition(
        parameterType = ParameterType.NITRATE,
        
        idealMin = 0.0,
        idealMax = 20.0, 
        highWarningMin = 20.0,
        highDangerMin = 40.0 
    )

    val defaultsMap: Map<ParameterType, ParameterRangeDefinition> = ParameterType.values().associateWith {
        when (it) {
            ParameterType.TEMPERATURE -> TEMPERATURE_CELSIUS
            ParameterType.PH -> PH
            ParameterType.AMMONIA -> AMMONIA_PPM
            ParameterType.NITRITE -> NITRITE_PPM
            ParameterType.NITRATE -> NITRATE_PPM
            
            else -> {
                
                
                
                ParameterRangeDefinition(it, idealMin = 0.0, idealMax = 100.0) 
            }
        }
    }

    fun getRangeDefinition(parameterType: ParameterType): ParameterRangeDefinition? {
        return defaultsMap[parameterType]
    }
} 