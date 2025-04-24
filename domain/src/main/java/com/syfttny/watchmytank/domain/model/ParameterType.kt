package com.syfttny.watchmytank.domain.model

enum class ParameterType(val displayName: String, val unit: String) {
    TEMPERATURE( "Temperature ",  "°C "),
    PH( "pH ",  " "),
    AMMONIA( "Ammonia ",  "ppm "),
    NITRITE( "Nitrite ",  "ppm "),
    NITRATE( "Nitrate ",  "ppm "),
    GENERAL_HARDNESS( "General Hardness (GH) ",  "dGH "),
    CARBONATE_HARDNESS( "Carbonate Hardness (KH) ",  "dKH "),
    PHOSPHATE( "Phosphate ",  "ppm "),
    SALINITY( "Salinity ",  "ppt "),
    CALCIUM( "Calcium ",  "ppm "),
    MAGNESIUM( "Magnesium ",  "ppm "),
    // Add other potential parameters users might want to track
    // COPPER( "Copper ",  "ppm "),
    // IODINE( "Iodine ",  "ppm "),
    // ALKALINITY( "Alkalinity ",  "meq/L "), // Can overlap with KH, consider how to handle
    // ORP( "Oxidation-Reduction Potential ",  "mV "),
    // TDS( "Total Dissolved Solids ",  "ppm "),
} 