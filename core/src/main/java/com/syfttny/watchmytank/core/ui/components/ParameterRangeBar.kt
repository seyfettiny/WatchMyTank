package com.syfttny.watchmytank.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.syfttny.watchmytank.domain.model.ParameterRangeDefinition
import kotlin.math.max
import kotlin.math.min

private val DangerColor = Color(0xFFD32F2F) 
private val WarningColor = Color(0xFFFFA000) 
private val IdealColor = Color(0xFF388E3C) 
private val BarBackgroundColor = Color.LightGray 

@Composable
fun ParameterRangeBar(
    rangeDefinition: ParameterRangeDefinition,
    currentValue: Double?,
    modifier: Modifier = Modifier,
    barHeight: Dp = 16.dp,
    indicatorRadius: Dp = 6.dp
) {
    val density = LocalDensity.current
    val barHeightPx = with(density) { barHeight.toPx() }
    val indicatorRadiusPx = with(density) { indicatorRadius.toPx() }

    val (visualMin, visualMax) = remember(rangeDefinition) {
        calculateVisualBounds(rangeDefinition)
    }
    val totalRange = visualMax - visualMin

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight + indicatorRadius * 2) 
    ) {
        val canvasWidth = size.width
        val barTop = indicatorRadiusPx 

        
        drawRangeSegments(
            drawScope = this,
            rangeDef = rangeDefinition,
            visualMin = visualMin,
            visualMax = visualMax,
            totalRange = totalRange,
            canvasWidth = canvasWidth,
            barTop = barTop,
            barHeight = barHeightPx
        )

        
        if (currentValue != null && totalRange > 0) {
            val valueRatio = ((currentValue - visualMin) / totalRange).coerceIn(0.0, 1.0)
            val indicatorX = (valueRatio * canvasWidth).toFloat()
            val indicatorY = barTop + barHeightPx / 2 

            drawIndicator(
                drawScope = this,
                center = Offset(indicatorX, indicatorY),
                radius = indicatorRadiusPx
            )
        }
    }
}

private fun calculateVisualBounds(rangeDef: ParameterRangeDefinition): Pair<Double, Double> {
    
    val points = listOfNotNull(
        rangeDef.lowDangerMax, rangeDef.lowWarningMax, rangeDef.idealMin,
        rangeDef.idealMax, rangeDef.highWarningMin, rangeDef.highDangerMin
    )
    if (points.isEmpty()) return Pair(0.0, 1.0) 

    var minVal = points.minOrNull() ?: rangeDef.idealMin
    var maxVal = points.maxOrNull() ?: rangeDef.idealMax

    
    if (maxVal <= minVal) {
        maxVal = minVal + 1.0 
    }

    
    val coreRange = maxVal - minVal
    val padding = coreRange * 0.1
    minVal -= padding
    maxVal += padding

    
    if (rangeDef.parameterType == com.syfttny.watchmytank.domain.model.ParameterType.PH) {
        minVal = 0.0
        maxVal = 14.0
    }

    return Pair(minVal, maxVal)
}

private fun drawRangeSegments(
    drawScope: DrawScope,
    rangeDef: ParameterRangeDefinition,
    visualMin: Double,
    visualMax: Double,
    totalRange: Double,
    canvasWidth: Float,
    barTop: Float,
    barHeight: Float
) {
    if (totalRange <= 0) { 
        drawScope.drawRect(BarBackgroundColor, topLeft = Offset(0f, barTop), size = Size(canvasWidth, barHeight))
        return
    }

    val scaleValue: (Double) -> Float = { value ->
        (((value - visualMin) / totalRange).coerceIn(0.0, 1.0) * canvasWidth).toFloat()
    }

    
    val segments = mutableListOf<Pair<ClosedFloatingPointRange<Float>, Color>>()

    
    rangeDef.lowDangerMax?.let {
        segments.add(Pair(0f..scaleValue(it), DangerColor))
    } ?: segments.add(Pair(0f..0f, BarBackgroundColor)) 

    
    val lowWarnStart = rangeDef.lowDangerMax ?: visualMin
    rangeDef.lowWarningMax?.let {
        segments.add(Pair(scaleValue(lowWarnStart)..scaleValue(it), WarningColor))
    }

    
    val idealStart = rangeDef.lowWarningMax ?: rangeDef.lowDangerMax ?: visualMin
    segments.add(Pair(scaleValue(idealStart)..scaleValue(rangeDef.idealMax), IdealColor))

    
    val highWarnStart = rangeDef.idealMax
    rangeDef.highWarningMin?.let {
        segments.add(Pair(scaleValue(highWarnStart)..scaleValue(it), WarningColor))
    }

    
    val highDangerStart = rangeDef.highWarningMin ?: rangeDef.idealMax
    rangeDef.highDangerMin?.let {
        segments.add(Pair(scaleValue(highDangerStart)..scaleValue(it), DangerColor))
        
         if (scaleValue(it) < canvasWidth) {
             segments.add(Pair(scaleValue(it)..canvasWidth, DangerColor))
         }

    } ?: 
        if (rangeDef.highWarningMin == null) {
             segments.add(Pair(scaleValue(highWarnStart)..canvasWidth, IdealColor))
        } else {
             segments.add(Pair(scaleValue(highDangerStart)..canvasWidth, WarningColor))
        }


    
    segments.forEach { (range, color) ->
        val startX = range.start
        val width = range.endInclusive - startX
        if (width > 0) {
            drawScope.drawRect(
                color = color,
                topLeft = Offset(startX, barTop),
                size = Size(width, barHeight)
            )
        }
    }
}

private fun drawIndicator(
    drawScope: DrawScope,
    center: Offset,
    radius: Float
) {
    drawScope.drawCircle(
        color = Color.Black, 
        radius = radius,
        center = center
    )
    drawScope.drawCircle(
        color = Color.White, 
        radius = radius * 0.8f, 
        center = center
    )
} 