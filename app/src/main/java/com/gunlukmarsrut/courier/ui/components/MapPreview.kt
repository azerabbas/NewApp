package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.ui.theme.AppColorTokens
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.appColors

/**
 * Stylized, self-drawn route map: no Maps SDK key required. Draws a route
 * line through every stop's [Stop.mapPosition] and a numbered pin per stop,
 * colored by status. Stands in for the real map integration.
 */
@Composable
fun RouteMapPreview(
    stops: List<Stop>,
    modifier: Modifier = Modifier,
    currentStopId: String? = null,
    clip: Boolean = false,
) {
    val colors = MaterialTheme.appColors
    val textMeasurer = rememberTextMeasurer()
    val roadColor = colors.surface2
    val routeColor = colors.primary.copy(alpha = 0.55f)

    Box(
        modifier = modifier
            .then(if (clip) Modifier.clip(CardShape) else Modifier)
            .background(colors.surface2),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawMapBackdrop(roadColor)

            if (stops.size >= 2) {
                val path = Path()
                stops.forEachIndexed { index, stop ->
                    val p = Offset(stop.mapPosition.x * size.width, stop.mapPosition.y * size.height)
                    if (index == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
                }
                drawPath(
                    path = path,
                    color = routeColor,
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.cornerPathEffect(24f),
                    ),
                )
            }

            stops.forEach { stop ->
                val center = Offset(stop.mapPosition.x * size.width, stop.mapPosition.y * size.height)
                val isCurrent = stop.id == currentStopId
                val (bg, fg) = pinColors(stop.status, isCurrent, colors)
                val radius = if (isCurrent) 20.dp.toPx() else 14.dp.toPx()

                if (isCurrent) {
                    drawCircle(color = colors.primary.copy(alpha = 0.18f), radius = radius + 10.dp.toPx(), center = center)
                }
                drawCircle(color = Color.White, radius = radius + 2.dp.toPx(), center = center)
                drawCircle(color = bg, radius = radius, center = center)

                val label = when {
                    isCurrent -> stop.orderNumber.toString()
                    stop.status == StopStatus.DELIVERED -> "✓"
                    stop.status == StopStatus.X -> "✕"
                    stop.status == StopStatus.TRANSFERRED -> "→"
                    else -> stop.orderNumber.toString()
                }
                val measured = textMeasurer.measure(
                    text = label,
                    style = TextStyle(
                        fontSize = if (isCurrent) 15.sp else 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = fg,
                    ),
                )
                drawText(
                    textLayoutResult = measured,
                    topLeft = Offset(center.x - measured.size.width / 2f, center.y - measured.size.height / 2f),
                )
            }
        }

        if (stops.isEmpty()) {
            Text(
                "Xəritə üçün ünvan yoxdur",
                style = AppType.caption,
                color = colors.textMuted,
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
    }
}

private fun pinColors(status: StopStatus, isCurrent: Boolean, colors: AppColorTokens): Pair<Color, Color> {
    if (isCurrent) return colors.primary to colors.onPrimary
    return when (status) {
        StopStatus.DELIVERED -> colors.success to colors.onSuccess
        StopStatus.X -> colors.finished to colors.onFinished
        StopStatus.TRANSFERRED -> colors.transfer to colors.onTransfer
        StopStatus.PENDING -> colors.primary to colors.onPrimary
    }
}

private fun DrawScope.drawMapBackdrop(roadColor: Color) {
    val stepX = size.width / 6f
    var x = stepX / 2
    while (x < size.width) {
        drawLine(roadColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 10.dp.toPx())
        x += stepX
    }
    val stepY = size.height / 5f
    var y = stepY / 2
    while (y < size.height) {
        drawLine(roadColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 10.dp.toPx())
        y += stepY
    }
}
