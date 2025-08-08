package com.syfttny.watchmytank.data.mapper

import com.syfttny.watchmytank.data.local.entity.ParameterLogEntity
import com.syfttny.watchmytank.domain.model.WaterParameterLog

fun WaterParameterLog.toEntity(): ParameterLogEntity {
    throw IllegalStateException("WaterParameterLog.toEntity mapper is outdated due to ParameterLogEntity refactor.")
} 