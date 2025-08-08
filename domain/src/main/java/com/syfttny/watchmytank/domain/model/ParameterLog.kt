package com.syfttny.watchmytank.domain.model

import java.time.Instant

data class ParameterLog(
    val id: String? = null, 
    val tankId: String, 
    val userId: String, 
    val timestamp: Instant,
    val parameters: Map<ParameterType, Double>,
    val notes: String? = null
) 