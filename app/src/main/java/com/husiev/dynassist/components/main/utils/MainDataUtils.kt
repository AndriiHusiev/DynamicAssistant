package com.husiev.dynassist.components.main.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.husiev.dynassist.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.Locale
import javax.inject.Inject

//interface MainRoutesData {
//	val headers: List<String>
//	val listItems: List<List<String>>
//}

@ViewModelScoped
class MainRoutesData @Inject constructor(
	@ApplicationContext private val context: Context,
) {
	val headers = context.resources.getStringArray(R.array.summary_headers_array).toList()
	val items = listOf(
		context.resources.getStringArray(R.array.overall_array).toList(),
		context.resources.getStringArray(R.array.performance_array).toList(),
		context.resources.getStringArray(R.array.record_array).toList(),
		context.resources.getStringArray(R.array.average_array).toList(),
	)
}

//@InstallIn(ActivityComponent::class)
//@Module
//abstract class MainRoutesDataModule {
//
//	@Binds
//	abstract fun bindMainRoutesData(mainRoutesDataImpl: SummaryData): MainRoutesData
//}

data class ConvertedStatData(
	val currentValue: String,
	val diffValue: String?,
	val color: Color,
	val imageVector: ImageVector
)

fun Any?.convert(divider: Float): ConvertedStatData {
	val currentValue: String
	val diffValue: String?
	
	when(this) {
		is Int -> {
			currentValue = this.toString()
			diffValue = when (divider) {
				0f -> "--"
				this.toFloat() -> null
				else -> String.format(Locale.getDefault(), "%.2f%%", this / divider)
			}
		}
		is Float -> {
			currentValue = this.toString()
			diffValue = null
		}
		else -> {
			currentValue = "--"
			diffValue = null
		}
	}
	
	return ConvertedStatData(
		currentValue = currentValue,
		diffValue = diffValue,
		color = Color.Gray,
		imageVector = Icons.Filled.DragHandle
	)
}