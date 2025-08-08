


package com.syfttny.watchmytank.domain.model

import java.time.LocalDateTime

data class WaterParameterLog(
    val id: Long = 0, 
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val parameterType: ParameterType, 
    val value: Double, 
    val notes: String? = null,
    
    
)