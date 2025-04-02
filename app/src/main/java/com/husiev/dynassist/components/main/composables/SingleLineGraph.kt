package com.husiev.dynassist.components.main.composables

import android.graphics.PointF
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.graphics.PathMeasure
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
import com.husiev.dynassist.components.main.utils.Range
import com.husiev.dynassist.components.main.utils.getPureExponent
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.ui.theme.DynamicAssistantTheme
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

private const val MAX_DOTS = 10
private const val GRAPH_RATIO = 5 / 4f

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
    var allRangeMode by rememberSaveable { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .aspectRatio(1.05f)
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
            var highlightedX by remember { mutableFloatStateOf(0f) }
            var highlightedOffset: Offset? = null
            val localView = LocalView.current
            val numDots = graphData.size
            val list = if (allRangeMode && numDots > MAX_DOTS) graphData
                else graphData.takeLast(minOf(MAX_DOTS, numDots))
            val dotsAmount = list.size
            val graphRange = getRange(list)
    
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
            val labelTextStyleM = MaterialTheme.typography.labelMedium
            
            Column(modifier = Modifier.fillMaxSize()) {
                
                Spacer(
                    modifier = Modifier
                        .aspectRatio(GRAPH_RATIO)
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { offset ->
                                    highlightedX = offset.x
                                    // bypassing unwanted behavior
                                    val num = if (allRangeMode) graphData.size else dotsAmount
                                    val sectionWidth = size.width / (num - 1)
                                    highlightedLine = ((offset.x + sectionWidth / 1.25f) / sectionWidth).toInt()
                                },
                                onDragEnd = { highlightedLine = null },
                                onDragCancel = { highlightedLine = null },
                                onDrag = { change, _ ->
                                    highlightedX = change.position.x
                                    val num = if (allRangeMode) graphData.size else dotsAmount
                                    val sectionWidth = size.width / (num - 1)
                                    highlightedLine = ((change.position.x + sectionWidth / 1.25f) / sectionWidth).toInt()
                                }
                            )
                        }
                        .drawWithCache {
                            val path = generateSmoothPath(list, size, graphRange)
                            val filledPath = Path()
                            filledPath.addPath(path)
                            filledPath.relativeLineTo(0f, size.height)
                            filledPath.lineTo(0f, size.height)
                            filledPath.close()
                            if (highlightedLine != null) {
                                highlightedOffset = getExactPathPos(path, highlightedX, size.width)
                            }
                            
                            onDrawBehind {
                                val barWidthPx = 1.dp.toPx()
                                val horizontalLines = 5
                                val sectionSize = size.height / horizontalLines
                                
                                repeat(horizontalLines) { i ->
                                    val startY = sectionSize * (i + 1)
                                    drawLine(
                                        barColor,
                                        start = Offset(0f, startY),
                                        end = Offset(size.width, startY),
                                        strokeWidth = barWidthPx,
                                        alpha = 0.25f,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f))
                                    )
                                    
                                    val range = graphRange.max - graphRange.min
                                    val lineLabel = graphRange.min + range * (horizontalLines - (i+1)) / horizontalLines
                                    val textLayoutResult = textMeasurer.measure("$lineLabel", labelTextStyleM)
                                    drawText(
                                        textLayoutResult,
                                        color = barColor,
                                        topLeft = Offset(4.dp.toPx(), startY - 16.dp.toPx()),
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
                                        highlightedLine =  maxOf(0, minOf(it, dotsAmount-1)),
                                        dotX = highlightedOffset?.x ?: 0f,
                                        dotY = highlightedOffset?.y ?: 0f,
                                        graphData = list,
                                        textMeasurer = textMeasurer,
                                        labelTextStyle = labelTextStyle
                                    )
                                }
                                
                            }
                        }
                )
                
                
                Row(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_big))
                        .wrapContentHeight()
                        .height(32.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val bgColorNotSelected = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                    val bgColorSelected = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff4153FF),
                        contentColor = Color.White
                    )
                    
                    FilledTonalButton(
                        onClick = { allRangeMode = false },
                        colors = if (allRangeMode) bgColorNotSelected else bgColorSelected,
                        contentPadding = PaddingValues(8.dp,0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.graph_10_dots),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                    
                    FilledTonalButton(
                        onClick = { allRangeMode = true },
                        colors = if (allRangeMode) bgColorSelected else bgColorNotSelected,
                        contentPadding = PaddingValues(8.dp,0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.graph_all_dots),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                }
            }
        }
    }
}

fun generateSmoothPath(data: List<Float>, size: Size, range: Range): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val lineWidth = size.width / numberEntries

    val heightPxPerAmount = size.height / range.range

    var previousBalanceX = 0f
    var previousBalanceY = size.height - (data[0] - range.min) * heightPxPerAmount
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (balance - range.min) * heightPxPerAmount
            )
        }

        val balanceX = i * lineWidth
        val balanceY = size.height - (balance - range.min) * heightPxPerAmount
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

fun getExactPathPos(path: Path, x: Float, width: Float): Offset {
    val pm = PathMeasure().apply {
        setPath(path, false)
    }
    val xx = x.coerceIn(0f, width)
    val length = pm.length
    val distance = xx / width
    val deviation = width / 500
    var posX = xx
    var curPos = pm.getPosition(distance * length)
    var diff = xx - curPos.x
    // in the loop decrease the difference between the finger pointer
    // and the position inside the path
    while (diff.absoluteValue > deviation) {
        posX = (posX + diff)
        curPos = pm.getPosition((posX / width) * length)
        diff = xx - curPos.x
    }
    return pm.getPosition((posX / width) * pm.length)
}

fun getRange(data: List<Float>?): Range {
    data?.let {
        val max = data.maxBy { it }
        val min = data.minBy { it }
        val range = max - min
        val mul = 10f.pow(getPureExponent(range))
        return Range(
            ((min / mul).roundToInt() - 1) * mul,
            ((max / mul).toInt() + 2) * mul
        )
    } ?: return Range(1f, 5f)
}

fun DrawScope.drawHighlight(
    circleColor: Color,
    highlightColor: Color,
    highlightedLine: Int,
    graphData: List<Float>,
    dotX: Float,
    dotY: Float,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle
) {
    val amount = graphData[highlightedLine]
    val impact = if (highlightedLine > 0)
        '\n' + (graphData[highlightedLine] - graphData[highlightedLine - 1]).toScreen(1f,"",true) else ""
    drawLine(
        highlightColor,
        start = Offset(dotX, 0f),
        end = Offset(dotX, size.height),
        strokeWidth = 2.dp.toPx(),
    )

    // draw hit circle on graph
    drawCircle(
        circleColor,
        radius = 4.dp.toPx(),
        center = Offset(dotX, dotY)
    )
    drawCircle(
        circleColor,
        radius = 8.dp.toPx(),
        center = Offset(dotX, dotY),
        alpha = 0.2f
    )
    // draw info box
    val textLayoutResult = textMeasurer.measure("$amount" + impact, style = labelTextStyle)
    val highlightContainerSize = (textLayoutResult.size)
        .toIntRect()
        .inflate(4.dp.roundToPx())
        .size
    var boxLeft = (dotX - (highlightContainerSize.width + 8.dp.toPx()))
    if (boxLeft < 0) boxLeft += highlightContainerSize.width + 16.dp.toPx()
    val boxTop = (dotY - (highlightContainerSize.height / 2f))
        .coerceIn(8.dp.toPx(), size.height - (highlightContainerSize.height + 8.dp.toPx()))
    drawRoundRect(
        Color.White,
        topLeft = Offset(boxLeft, boxTop),
        size = highlightContainerSize.toSize(),
        cornerRadius = CornerRadius(8.dp.toPx())
    )
    drawText(
        textLayoutResult,
        color = Color.Black,
        topLeft = Offset(boxLeft + 4.dp.toPx(), boxTop + 4.dp.toPx())
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