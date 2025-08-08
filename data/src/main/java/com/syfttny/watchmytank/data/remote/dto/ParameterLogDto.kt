package com.syfttny.watchmytank.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ParameterLogDto(
    
    
    val tankId: String = "",
    val userId: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null, 
    val parameters: Map<String, Double> = emptyMap(), 
    val notes: String? = null
    
) 