package com.syfttny.watchmytank.domain.model

enum class ParameterType(val displayName: String, val unit: String) {
    TEMPERATURE("Temperature", "°C"), 
    PH("pH", ""),
    AMMONIA("Ammonia", "ppm"),
    NITRITE("Nitrite", "ppm"),
    NITRATE("Nitrate", "ppm"),
    GENERAL_HARDNESS("General Hardness (GH)", "dGH"),
    CARBONATE_HARDNESS("Carbonate Hardness (KH)", "dKH"),
    PHOSPHATE("Phosphate", "ppm"),
    SALINITY("Salinity", "ppt"), 
    CALCIUM("Calcium", "ppm"),
    MAGNESIUM("Magnesium", "ppm"),
    
    
    
    
    
    
} 