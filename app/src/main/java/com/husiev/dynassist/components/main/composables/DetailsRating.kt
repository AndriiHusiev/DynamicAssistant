package com.husiev.dynassist.components.main.composables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.bigToString
import com.husiev.dynassist.components.main.utils.getRating

@Composable
fun DetailsRatingCard(
    globalRating: Int?,
    modifier: Modifier = Modifier,
) {
    val ratingData = globalRating.getRating()
    
    Box(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_big))
    ) {
        AnimatedCircle(
            circleColor = ratingData.circleColor,
            backgroundColor = ratingData.backgroundColor,
            modifier = Modifier
                .height(300.dp)
                .align(Alignment.Center)
                .fillMaxWidth()
        )
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = stringResource(R.string.global_rating),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = globalRating.bigToString(),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.better_than, ratingData.betterThen),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun AnimatedCircle(
    circleColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }
    val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }
    val transition = rememberTransition(currentState, label = "transition")
    val angleOffset by transition.animateFloat(
        label = "angle",
        transitionSpec = {
            tween(
                delayMillis = ANIM_START_DELAY,
                durationMillis = ANIM_DURATION,
                easing = LinearOutSlowInEasing
            )
        },
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            360f
        }
    }
    val shift by transition.animateFloat(
        label = "shift",
        transitionSpec = {
            tween(
                delayMillis = ANIM_START_DELAY,
                durationMillis = ANIM_DURATION,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        },
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            30f
        }
    }
    val bgColor by transition.animateColor(
        label = "color",
        transitionSpec = {
            tween(
                delayMillis = ANIM_START_DELAY,
                durationMillis = ANIM_DURATION,
                easing = LinearOutSlowInEasing
            )
        },
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            MaterialTheme.colorScheme.background
        } else {
            backgroundColor
        }
    }
    
    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shift - 90f
        drawArc(
            color = bgColor,
            startAngle = 0f,
            sweepAngle = 360f,
            topLeft = topLeft,
            size = size,
            useCenter = false,
        )
        drawArc(
            color = circleColor,
            startAngle = startAngle,
            sweepAngle = angleOffset,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
        startAngle += angleOffset
    }
}

private enum class AnimatedCircleProgress { START, END }
private const val ANIM_START_DELAY = 500
private const val ANIM_DURATION = 900