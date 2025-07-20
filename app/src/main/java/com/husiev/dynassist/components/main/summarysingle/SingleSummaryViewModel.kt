package com.husiev.dynassist.components.main.summarysingle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.husiev.dynassist.components.main.utils.SingleParamData
import com.husiev.dynassist.components.main.utils.StatisticsUIMapper
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
class SingleSummaryViewModel @Inject constructor(
	databaseRepository: DatabaseRepository,
	private val uiMapper: StatisticsUIMapper,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	
	private val selectedId: Int = savedStateHandle.get<Int>(SUMMARY_SINGLE_ARG) ?: -1
	
	val statisticData: StateFlow<SingleParamData?> =
		databaseRepository.getStatisticData()
			.map { list ->
				withContext(Dispatchers.Default) {
					uiMapper.mapSingleToUIModel(list, selectedId)
				}
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
}