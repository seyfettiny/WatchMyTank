package com.syfttny.watchmytank.domain.model

data class ParameterRangeDefinition(
    val parameterType: ParameterType,
    val lowDangerMax: Double? = null, 
    val lowWarningMax: Double? = null,
    val idealMin: Double,
    val idealMax: Double,
    val highWarningMin: Double? = null,
    val highDangerMin: Double? = null 
) 