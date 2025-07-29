package com.husiev.dynassist.components.main

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.husiev.dynassist.components.main.composables.FilterTechnics
import com.husiev.dynassist.components.main.composables.SortTechnics
import com.husiev.dynassist.components.main.utils.StatisticsUIMapper
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.dataclasses.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val ACCOUNT_ID = "account_id"
private const val NICKNAME = "nickname"

@HiltViewModel
class MainViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val networkRepository: NetworkRepository,
	private val databaseRepository: DatabaseRepository,
	private val uiMapper: StatisticsUIMapper,
	@param:ApplicationContext private val context: Context,
): ViewModel() {
	
	val accountId: Int = savedStateHandle.get<Int>(ACCOUNT_ID) ?: 0
	val nickname: String = savedStateHandle.get<String>(NICKNAME) ?: ""
	
	init {
		databaseRepository.accountId = savedStateHandle.get<Int>(ACCOUNT_ID) ?: 0
		databaseRepository.nickname = savedStateHandle.get<String>(NICKNAME) ?: ""
	}
	
	private val _sortTechnics = MutableStateFlow(SortTechnics.BATTLES)
	val sortTechnics: StateFlow<SortTechnics>
		get() = _sortTechnics.asStateFlow()
	
	fun changeSortTechnics(sort: SortTechnics) {
		_sortTechnics.value = sort
	}
	
	private val _filterTechnics = MutableStateFlow(FilterTechnics.ALL)
	val filterTechnics: StateFlow<FilterTechnics>
		get() = _filterTechnics.asStateFlow()
	
	fun changeFilterTechnics(filter: FilterTechnics) {
		_filterTechnics.value = filter
	}
	
	fun getParamTitles() = uiMapper.getTitles()
	
	fun switchNotification(state: Boolean) {
		viewModelScope.launch {
			val list = databaseRepository.getStatisticData().first()
			val battles = if (list.isEmpty()) 0 else list.last().battles
			databaseRepository.updateNotification(state.toInt(), battles)
		}
	}
	
	val notifyState: StateFlow<NotifyEnum> =
		databaseRepository.loadPlayer()
			.map { it.notification }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = NotifyEnum.UNCHECKED
			)
	
	private val _queryStatus = MutableStateFlow<DataState>(DataState.StandBy)
	val queryStatus = _queryStatus.asStateFlow()
	
	fun closeSnackbar() {
		_queryStatus.value = DataState.StandBy
	}
	
	fun getAccountAllData() {
		if (_queryStatus.value == DataState.Loading) return
		_queryStatus.value = DataState.Loading
		
		viewModelScope.launch(Dispatchers.IO) {
			// Download data from server
			val personalData = when (val response = networkRepository.getAccountAllData(accountId)) {
				is ResultWrapper.Success -> response.data
				is ResultWrapper.Error -> {
					_queryStatus.value = DataState.Error("Failed to get player personal data: " +
							response.error.toFormattedString())
					return@launch
				}
			}
			
			val clanData = when (val response = networkRepository.getClanShortInfo(accountId)) {
				is ResultWrapper.Success -> response.data
				is ResultWrapper.Error -> {
					_queryStatus.value = DataState.Error("Failed to get player clan data: " +
							response.error.toFormattedString())
					return@launch
				}
			}
			
			val vehicles = when(val response = networkRepository.getVehicleShortStat(accountId)) {
				is ResultWrapper.Success -> response.data.let {
					it.map { item -> item.asEntity(accountId, personalData.lastBattleTime) }
				}
				is ResultWrapper.Error -> {
					_queryStatus.value = DataState.Error("Failed to get details on player's vehicles: " +
							response.error.toFormattedString())
					return@launch
				}
			}
			
			val networkVehicleInfo = when(val response = retrieveVehicleInfo(vehicles)) {
				is ResultWrapper.Success -> response.data
				is ResultWrapper.Error -> {
					_queryStatus.value = DataState.Error("Failed to get list of available vehicles: " +
							response.error.toFormattedString())
					return@launch
				}
			}
			
			_queryStatus.value = DataState.Success("All data loaded successfully!")
			
			
			// Add all downloaded data to database
			savePersonalData(personalData, databaseRepository)
			saveClanData(context, clanData, databaseRepository)
			if (networkVehicleInfo.isNotEmpty()) databaseRepository.addVehiclesInfo(networkVehicleInfo)
			if (vehicles.isNotEmpty()) databaseRepository.addVehiclesStatData(vehicles)
		}
	}
	
	private suspend fun retrieveVehicleInfo(
		vehicles: List<VehicleStatDataEntity>,
	): ResultWrapper<List<NetworkVehicleInfoItem>> {
		val vehicleInfo = mutableListOf<NetworkVehicleInfoItem>()
		
		// First get list of missing entries
		val listOfNewVehicles = getListOfNewVehicles(vehicles, databaseRepository)
		if (listOfNewVehicles.isEmpty()) {
			return ResultWrapper.Success(emptyList())
		}
		
		// The list needs to be split into parts first, since the server can only process
		// a maximum of 100 items in each request.
		val vehicleChunks = listOfNewVehicles.chunked(100)
		
		// Then we can request missing vehicles info from server
		for (chunk in vehicleChunks) {
			val sublist = chunk.joinToString(separator = ",")
			
			when(val response = networkRepository.getVehicleInfo(sublist)) {
				is ResultWrapper.Error -> return response
				is ResultWrapper.Success -> {
					response.data.forEach { item ->
						item.let {
							preloadImage(context, item.images.bigIcon.secure())
							vehicleInfo.add(item)
						}
					}
				}
			}
		}
		
		return ResultWrapper.Success(vehicleInfo)
	}
}

private suspend fun savePersonalData(
	networkAccountPersonalData: NetworkAccountPersonalData?,
	databaseRepository: DatabaseRepository,
) {
	networkAccountPersonalData?.let {
		val list = databaseRepository.getStatisticData().first()
		if (list.isEmpty() || list.last().battles < it.statistics.all.battles) {
			databaseRepository.updatePersonalData(it)
			databaseRepository.addStatisticData(
				it.statistics.all,
				it.globalRating,
				it.lastBattleTime
			)
		}
		databaseRepository.updateTime(Date().time.toString())
	}
}

private suspend fun saveClanData(
	context: Context,
	networkAccountClanData: NetworkAccountClanData?,
	databaseRepository: DatabaseRepository,
) {
	networkAccountClanData?.let { clanData ->
		databaseRepository.updatePlayerClanInfo(clanData)
		clanData.clan.emblems.x195.portal?.let {
			preloadImage(context, it)
			databaseRepository.updateClan(
				clan = clanData.clan.tag,
				emblem = it,
			)
		}
	}
}

/**
 * Get list of missing entries
 */
private suspend fun getListOfNewVehicles(
	vehicles: List<VehicleStatDataEntity>,
	databaseRepository: DatabaseRepository,
) :List<Int> {
	val idsFromNW = vehicles.map { it.tankId }
	val listOfNewVehicles = mutableListOf<Int>()
	
	val idsFromDB = databaseRepository.getVehiclesIds(idsFromNW).first().toSet()
	listOfNewVehicles.addAll( idsFromNW.filter { it !in idsFromDB } )
	
	return listOfNewVehicles
}

fun preloadImage(context: Context, imageUrl: String?) =
	context.imageLoader
		.enqueue(
			ImageRequest.Builder(context)
			.data(imageUrl)
			.memoryCachePolicy(CachePolicy.DISABLED)
			// Set a custom `Decoder.Factory` that skips the decoding step.
			.decoderFactory { _, _, _ ->
				Decoder { DecodeResult(Color.BLACK.toDrawable(), false) }
			}
			.build()
		)

fun String.secure() = this.replace("http:", "https:")

fun Boolean.toInt() = if (this) 1 else 0