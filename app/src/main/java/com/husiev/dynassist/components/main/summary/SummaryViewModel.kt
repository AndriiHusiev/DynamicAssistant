package com.husiev.dynassist.components.main.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.ReducedAccStatData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import com.husiev.dynassist.components.main.utils.SingleParamData
import com.husiev.dynassist.components.main.utils.StatisticsUIMapper
import com.husiev.dynassist.components.main.utils.SummaryGroup
import com.husiev.dynassist.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
//	private val mrd: MainRoutesData,
	private val databaseRepository: DatabaseRepository,
	private val uiMapper: StatisticsUIMapper,
): ViewModel() {
	
/*	val statisticData: StateFlow<Map<String, List<AccountStatisticsData>>> =
		databaseRepository.getStatisticData()
			.map {
				it.lastOrNull()?.let { stat ->
					val list = databaseRepository.getExactVehiclesShortData(stat.getMaxFields()).first()
					stat.fillMaxFields(list)
				}
				it.asExternalModel(mrd)
			}
			.flowOn(Dispatchers.Default)*/
	val statisticData: StateFlow<Map<SummaryGroup, List<ReducedAccStatData>>> =
		databaseRepository.getNewStatisticData()
			.map { list ->
//				val statsEntities = list.map { it.statistics }
//				uiMapper.mapToUIModel(statsEntities)
//				var map: Map<SummaryGroup, List<ReducedAccStatData>> = emptyMap()
				withContext(Dispatchers.Default) {
					val reducedList = uiMapper.mapToUIModel(list)
//					map = reducedList.groupBy { it.group }
					reducedList.groupBy { it.group }
				}
//				map
			}
/*			.map {
				it.groupBy { it.group }
			}*/
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyMap()
			)
	
/*	fun getSingleParam(singleTitle: String?): StateFlow<SingleParamData?> =
		statisticData
			.map { data ->
				val item = data
//					.firstOrNull { it.title == singleTitle }
					.firstNotNullOfOrNull { stat ->
						stat.value.firstOrNull { it.title == singleTitle }
					}
				
				val dates = emptyList<String>()//data
//					.firstNotNullOfOrNull { stat ->
//						stat.value.firstOrNull { it.title == "Last Battle Time" }
//					}?.values?.map { it.toInt().asStringDate("shortest") }
				
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
			)*/
}
