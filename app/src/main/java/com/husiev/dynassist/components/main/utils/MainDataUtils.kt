package com.husiev.dynassist.components.main.utils

import android.content.Context
import com.husiev.dynassist.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainRoutesData @Inject constructor(
	@ApplicationContext private val context: Context,
) {
	val headers = context.resources.getStringArray(R.array.summary_headers_array).toList()
	val items = converterToMap(
		context.resources.getStringArray(R.array.summary_items_array).toList()
	)
}

private fun converterToMap(items: List<String>): Map<String, String> {
	val map = mutableMapOf<String, String>()
	
	items.forEach {
		val (variable, caption) = it.split(":")
		map[variable] = caption
	}
	
	return map
}

data class Range(
	val min: Float,
	val max: Float,
	val dots: List<String>,
	val numLines: Int = 5,
	val range: Float = max - min,
)