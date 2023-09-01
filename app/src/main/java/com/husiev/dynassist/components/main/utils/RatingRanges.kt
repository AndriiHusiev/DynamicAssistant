package com.husiev.dynassist.components.main.utils

import androidx.compose.ui.graphics.Color

enum class RatingRanges(
	val max: Int,
	val betterThen: Float,
	val circleColor: Color,
	val backgroundColor: Color,
) {
	UNKNOWN(
		max = 0,
		betterThen = 0f,
		circleColor = Color(0xFF8A8A8A),
		backgroundColor = Color(0xFFE4E4E4),
	),
	VERY_BAD(
		max = 3320,
		betterThen = 0f,
		circleColor = Color(0xFFFE0E00),
		backgroundColor = Color(0xFFFFC5C2),
	),
	BAD(
		max = 5225,
		betterThen = 20f,
		circleColor = Color(0xFFFE7903),
		backgroundColor = Color(0xFFFFDFC2),
	),
	NORMAL(
		max = 7266,
		betterThen = 60f,
		circleColor = Color(0xFFF8F400),
		backgroundColor = Color(0xFFF5F4BD),
	),
	GOOD(
		max = 9525,
		betterThen = 90f,
		circleColor = Color(0xFF60FF00),
		backgroundColor = Color(0xFFD0F8B8),
	),
	RARE(
		max = 10959,
		betterThen = 99f,
		circleColor = Color(0xFF02C9B3),
		backgroundColor = Color(0xFFC5FFF9),
	),
	UNIQUE(
		max = 11840,
		betterThen = 99.9f,
		circleColor = Color(0xFFD042F3),
		backgroundColor = Color(0xFFF0C3FC),
	),
}

fun  Int?.getRating(): RatingRanges =
	if (this == null)
		RatingRanges.UNKNOWN
	else if (this < RatingRanges.VERY_BAD.max)
		RatingRanges.VERY_BAD
	else if (this < RatingRanges.BAD.max)
		RatingRanges.BAD
	else if (this < RatingRanges.NORMAL.max)
		RatingRanges.NORMAL
	else if (this < RatingRanges.GOOD.max)
		RatingRanges.GOOD
	else if (this < RatingRanges.RARE.max)
		RatingRanges.RARE
	else
		RatingRanges.UNIQUE