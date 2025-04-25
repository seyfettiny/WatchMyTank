package com.syfttny.watchmytank.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.syfttny.watchmytank.domain.model.ParameterType

/**
 * Room TypeConverter to convert a Map<ParameterType, Double> to/from a JSON String.
 */
class ParameterMapConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromParameterMap(map: Map<ParameterType, Double>?): String? {
        return map?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toParameterMap(json: String?): Map<ParameterType, Double>? {
        return json?.let {
            val type = object : TypeToken<Map<ParameterType, Double>>() {}.type
            gson.fromJson(it, type)
        }
    }
} 