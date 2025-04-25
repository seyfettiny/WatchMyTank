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

// Define default colors for the zones (consider moving to Theme later)
private val DangerColor = Color(0xFFD32F2F) // Red
private val WarningColor = Color(0xFFFFA000) // Orange
private val IdealColor = Color(0xFF388E3C) // Green
private val BarBackgroundColor = Color.LightGray // Fallback background

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

    // Determine the visual min/max for the bar scale
    val (visualMin, visualMax) = remember(rangeDefinition) {
        calculateVisualBounds(rangeDefinition)
    }
    val totalRange = visualMax - visualMin

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight + indicatorRadius * 2) // Height accommodates indicator above/below
    ) {
        val canvasWidth = size.width
        val barTop = indicatorRadiusPx // Position bar leaving space for indicator

        // Draw the segments
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

        // Draw the indicator if currentValue is provided
        if (currentValue != null && totalRange > 0) {
            val valueRatio = ((currentValue - visualMin) / totalRange).coerceIn(0.0, 1.0)
            val indicatorX = (valueRatio * canvasWidth).toFloat()
            val indicatorY = barTop + barHeightPx / 2 // Center indicator vertically on the bar

            drawIndicator(
                drawScope = this,
                center = Offset(indicatorX, indicatorY),
                radius = indicatorRadiusPx
            )
        }
    }
}

private fun calculateVisualBounds(rangeDef: ParameterRangeDefinition): Pair<Double, Double> {
    // Attempt to find reasonable bounds for the visual scale
    val points = listOfNotNull(
        rangeDef.lowDangerMax, rangeDef.lowWarningMax, rangeDef.idealMin,
        rangeDef.idealMax, rangeDef.highWarningMin, rangeDef.highDangerMin
    )
    if (points.isEmpty()) return Pair(0.0, 1.0) // Should not happen with valid def

    var minVal = points.minOrNull() ?: rangeDef.idealMin
    var maxVal = points.maxOrNull() ?: rangeDef.idealMax

    // Add some padding if min/max are too close or equal
    if (maxVal <= minVal) {
        maxVal = minVal + 1.0 // Avoid division by zero if totalRange is 0
    }

    // Extend bounds slightly for visual padding (e.g., 10% of the core range)
    val coreRange = maxVal - minVal
    val padding = coreRange * 0.1
    minVal -= padding
    maxVal += padding

    // Special case for pH - typically shown 0-14
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
    if (totalRange <= 0) { // Prevent division by zero
        drawScope.drawRect(BarBackgroundColor, topLeft = Offset(0f, barTop), size = Size(canvasWidth, barHeight))
        return
    }

    val scaleValue: (Double) -> Float = { value ->
        (((value - visualMin) / totalRange).coerceIn(0.0, 1.0) * canvasWidth).toFloat()
    }

    // Define segment boundaries and colors
    val segments = mutableListOf<Pair<ClosedFloatingPointRange<Float>, Color>>()

    // Low Danger
    rangeDef.lowDangerMax?.let {
        segments.add(Pair(0f..scaleValue(it), DangerColor))
    } ?: segments.add(Pair(0f..0f, BarBackgroundColor)) // Placeholder if no low danger

    // Low Warning
    val lowWarnStart = rangeDef.lowDangerMax ?: visualMin
    rangeDef.lowWarningMax?.let {
        segments.add(Pair(scaleValue(lowWarnStart)..scaleValue(it), WarningColor))
    }

    // Ideal
    val idealStart = rangeDef.lowWarningMax ?: rangeDef.lowDangerMax ?: visualMin
    segments.add(Pair(scaleValue(idealStart)..scaleValue(rangeDef.idealMax), IdealColor))

    // High Warning
    val highWarnStart = rangeDef.idealMax
    rangeDef.highWarningMin?.let {
        segments.add(Pair(scaleValue(highWarnStart)..scaleValue(it), WarningColor))
    }

    // High Danger
    val highDangerStart = rangeDef.highWarningMin ?: rangeDef.idealMax
    rangeDef.highDangerMin?.let {
        segments.add(Pair(scaleValue(highDangerStart)..scaleValue(it), DangerColor))
        // Fill remaining space if high danger doesn't reach visual max
         if (scaleValue(it) < canvasWidth) {
             segments.add(Pair(scaleValue(it)..canvasWidth, DangerColor))
         }

    } ?: // Fill remaining space with Ideal if no high warning/danger
        if (rangeDef.highWarningMin == null) {
             segments.add(Pair(scaleValue(highWarnStart)..canvasWidth, IdealColor))
        } else {
             segments.add(Pair(scaleValue(highDangerStart)..canvasWidth, WarningColor))
        }


    // Draw the segments
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
        color = Color.Black, // Outer border color
        radius = radius,
        center = center
    )
    drawScope.drawCircle(
        color = Color.White, // Inner color
        radius = radius * 0.8f, // Slightly smaller inner circle
        center = center
    )
} 