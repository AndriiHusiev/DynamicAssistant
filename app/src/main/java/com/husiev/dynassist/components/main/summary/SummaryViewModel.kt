package com.husiev.dynassist.components.main.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.FullAccStatData
import com.husiev.dynassist.components.main.utils.StatisticsUIMapper
import com.husiev.dynassist.components.main.utils.SummaryGroup
import com.husiev.dynassist.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
	databaseRepository: DatabaseRepository,
	private val uiMapper: StatisticsUIMapper,
): ViewModel() {
	
	val statisticData: StateFlow<Map<SummaryGroup, List<FullAccStatData>>> =
		databaseRepository.getNewStatisticData()
			.map { list ->
				withContext(Dispatchers.Default) {
					val reducedList = uiMapper.mapToUIModel(list)
					reducedList.groupBy { it.group }
				}
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyMap()
			)
}
