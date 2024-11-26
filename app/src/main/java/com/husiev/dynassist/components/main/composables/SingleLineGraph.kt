package com.husiev.dynassist.components.main.composables

import android.graphics.PointF
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import com.husiev.dynassist.R
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import kotlin.math.roundToInt

/*
* Copyright 2023 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
@Composable
fun SmoothLineGraph(
    graphData: List<Float>?,
    modifier: Modifier = Modifier,
) {
    val barColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f)
    val chartColor = Color(0xFF00897B)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .aspectRatio(3 / 2f)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (graphData == null || graphData.size < 2) {
            Spacer(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .aspectRatio(3 / 2f)
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawBehind {
                            drawRect(barColor, style = Stroke(1.dp.toPx()))
                        }
                    }
            )
            Text(
                text = stringResource(R.string.chart_no_data),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_big)),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        } else {
            val animationProgress = remember { Animatable(0f) }
            var highlightedLine by remember { mutableStateOf<Int?>(null) }
            val localView = LocalView.current
            val dotsAmount = minOf(10, graphData.size)
            val list = graphData.takeLast(dotsAmount)
    
            LaunchedEffect(highlightedLine) {
                if (highlightedLine != null) {
                    localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                }
            }
    
            LaunchedEffect(key1 = list, block = {
                animationProgress.animateTo(1f, tween(3000))
            })
    
            val textMeasurer = rememberTextMeasurer()
            val labelTextStyle = MaterialTheme.typography.labelSmall
    
            Spacer(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .aspectRatio(3 / 2f)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                highlightedLine =
                                    (offset.x / (size.width / (list.size - 1))).roundToInt()
                            },
                            onDragEnd = { highlightedLine = null },
                            onDragCancel = { highlightedLine = null },
                            onDrag = { change, _ ->
                                highlightedLine =
                                    (change.position.x / (size.width / (list.size - 1))).roundToInt()
                            }
                        )
                    }
                    .drawWithCache {
                        val path = generateSmoothPath(list, size)
                        val filledPath = Path()
                        filledPath.addPath(path)
                        filledPath.relativeLineTo(0f, size.height)
                        filledPath.lineTo(0f, size.height)
                        filledPath.close()
        
                        onDrawBehind {
                            val barWidthPx = 1.dp.toPx()
                            drawRect(barColor, style = Stroke(barWidthPx))
            
                            val verticalLines = dotsAmount - 2
                            val verticalSize = size.width / (verticalLines + 1)
                            repeat(verticalLines) { i ->
                                val startX = verticalSize * (i + 1)
                                drawLine(
                                    barColor,
                                    start = Offset(startX, 0f),
                                    end = Offset(startX, size.height),
                                    strokeWidth = barWidthPx
                                )
                            }
                            val horizontalLines = 3
                            val sectionSize = size.height / (horizontalLines + 1)
                            repeat(horizontalLines) { i ->
                                val startY = sectionSize * (i + 1)
                                drawLine(
                                    barColor,
                                    start = Offset(0f, startY),
                                    end = Offset(size.width, startY),
                                    strokeWidth = barWidthPx
                                )
                            }
            
                            // draw line
                            clipRect(right = size.width * animationProgress.value) {
                                drawPath(path, chartColor, style = Stroke(2.dp.toPx()))
                
                                drawPath(
                                    filledPath,
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            chartColor.copy(alpha = 0.4f),
                                            Color.Transparent
                                        )
                                    ),
                                    style = Fill
                                )
                            }
            
                            // draw highlight if user is dragging
                            highlightedLine?.let {
                                this.drawHighlight(
                                    circleColor = chartColor,
                                    highlightColor = barColor,
                                    highlightedLine = it,
                                    graphData = list,
                                    textMeasurer = textMeasurer,
                                    labelTextStyle = labelTextStyle
                                )
                            }
            
                        }
                    }
            )
        }
    }
}

fun generateSmoothPath(data: List<Float>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val lineWidth = size.width / numberEntries

    val max = data.maxBy { it }
    val min = data.minBy { it } // will map to x= 0, y = height
    val range = max - min
    val heightPxPerAmount = size.height / range

    var previousBalanceX = 0f
    var previousBalanceY = size.height
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (balance - min) *
                        heightPxPerAmount
            )

        }

        val balanceX = i * lineWidth
        val balanceY = size.height - (balance - min) *
                heightPxPerAmount
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

fun DrawScope.drawHighlight(
    circleColor: Color,
    highlightColor: Color,
    highlightedLine: Int,
    graphData: List<Float>,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle
) {
        val amount = graphData[highlightedLine]
        val minAmount = graphData.minBy { it }
        val range = graphData.maxBy { it } - minAmount
        val percentageHeight = ((amount - minAmount) / range)
        val pointY = size.height - (size.height * percentageHeight)
        // draw vertical line
        val x = highlightedLine * (size.width / (graphData.size - 1))
        drawLine(
            highlightColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
        )

        // draw hit circle on graph
        drawCircle(
            circleColor,
            radius = 4.dp.toPx(),
            center = Offset(x, pointY)
        )

        // draw info box
        val textLayoutResult = textMeasurer.measure("$amount", style = labelTextStyle)
        val highlightContainerSize = (textLayoutResult.size)
            .toIntRect()
            .inflate(4.dp.roundToPx())
            .size
        val boxTopLeft = ( x - (highlightContainerSize.width / 2f))
            .coerceIn(0f, size.width - highlightContainerSize.width)
        drawRoundRect(
            Color.White,
            topLeft = Offset(boxTopLeft, 0f),
            size = highlightContainerSize.toSize(),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawText(
            textLayoutResult,
            color = Color.Black,
            topLeft = Offset(boxTopLeft + 4.dp.toPx(), 4.dp.toPx())
        )
}

@Preview(showBackground = true)
@Composable
fun SmoothLineGraphPreview() {
    val previewData = listOf(
        65631f, 65931f, 65851f, 65931f, 66484f, 67684f, 66684f,
        66984f, 70600f, 71600f, 72600f, 72526f, 72976f, 73589f,
    )
    DynamicAssistantTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SmoothLineGraph(previewData)
                SmoothLineGraph(null)
            }
        }
    }
}