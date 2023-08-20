package com.husiev.dynassist.components.main.utils

import android.content.Context
import com.husiev.dynassist.R
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
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
	val listItems = listOf(
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
