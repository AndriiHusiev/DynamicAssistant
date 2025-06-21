package com.husiev.dynassist.components.main.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import com.husiev.dynassist.components.main.utils.SingleParamData
import com.husiev.dynassist.components.main.utils.asExternalModel
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.asStringDate
import com.husiev.dynassist.database.entity.fillMaxFields
import com.husiev.dynassist.database.entity.getMaxFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class SummaryViewModel @Inject constructor(
	private val mrd: MainRoutesData,
	private val databaseRepository: DatabaseRepository,
): ViewModel() {
	
	val statisticData: StateFlow<Map<String, List<AccountStatisticsData>>> =
		databaseRepository.getStatisticData()
			.map {
				it.lastOrNull()?.let {
					databaseRepository.getExactVehiclesShortData(it.getMaxFields()).first { list ->
						it.fillMaxFields(list)
						true
					}
				}
				it.asExternalModel(mrd)
			}
			.flowOn(Dispatchers.Default)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList<StatisticsEntity>().asExternalModel(mrd)
			)
	
	fun getSingleParam(singleTitle: String?): StateFlow<SingleParamData?> =
		statisticData
			.map { data ->
				val item = data
					.firstNotNullOfOrNull {
						it.value.firstOrNull { it.title == singleTitle }
					}
				
				val dates = data
					.firstNotNullOfOrNull { it.value.firstOrNull { it.title == "Last Battle Time" } }
					?.values?.map { it.toInt().asStringDate("shortest") }
				
				if (item != null && dates != null)
					SingleParamData(item, dates)
				else
					null
			}
			.flowOn(Dispatchers.Default)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
}
