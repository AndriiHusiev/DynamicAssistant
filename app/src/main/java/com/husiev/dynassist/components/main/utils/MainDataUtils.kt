package com.husiev.dynassist.components.main.utils

import android.content.Context
import com.husiev.dynassist.R
import dagger.hilt.android.qualifiers.ApplicationContext
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
	val item = context.resources.getStringArray(R.array.summary_items_array).toList()
}

//@InstallIn(ActivityComponent::class)
//@Module
//abstract class MainRoutesDataModule {
//
//	@Binds
//	abstract fun bindMainRoutesData(mainRoutesDataImpl: SummaryData): MainRoutesData
//}