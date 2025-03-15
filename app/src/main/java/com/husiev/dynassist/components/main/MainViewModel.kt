package com.husiev.dynassist.components.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import com.husiev.dynassist.components.main.utils.Result
import com.husiev.dynassist.components.main.utils.VehicleData
import com.husiev.dynassist.components.main.utils.asExternalModel
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.VehicleStatDataEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.database.entity.fillMaxFields
import com.husiev.dynassist.database.entity.getMaxFields
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.dataclasses.NetworkAccountClanData
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalData
import com.husiev.dynassist.network.dataclasses.NetworkVehicleInfoItem
import com.husiev.dynassist.network.dataclasses.asEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.collections.component2

private const val ACCOUNT_ID = "account_id"
private const val NICKNAME = "nickname"

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mrd: MainRoutesData,
	private val savedStateHandle: SavedStateHandle,
	private val networkRepository: NetworkRepository,
	private val databaseRepository: DatabaseRepository,
	@ApplicationContext private val context: Context,
): ViewModel() {
	val accountId: Int = savedStateHandle.get<Int>(ACCOUNT_ID) ?: 0
	
	val nickname: String = savedStateHandle.get<String>(NICKNAME) ?: ""
	
	init {
		getAccountAllData()
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
	
	fun switchNotification(state: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			databaseRepository.getStatisticData(accountId).first { list ->
				val battles = if (list.isEmpty()) 0 else list.last().battles
				databaseRepository.updateNotification(state.toInt(), battles, accountId)
				true
			}
		}
	}
	
	val notifyState: StateFlow<NotifyEnum> =
		databaseRepository.loadPlayer(accountId)
			.map { it.notification }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = NotifyEnum.UNCHECKED
			)
	
	val personalData: StateFlow<AccountPersonalData?> =
		databaseRepository.getPersonalData(accountId)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
	val statisticData: StateFlow<Map<String, List<AccountStatisticsData>>> =
		databaseRepository.getStatisticData(accountId)
			.map {
				it.lastOrNull()?.let {
					databaseRepository.getExactVehiclesShortData(it.getMaxFields()).first { list ->
						it.fillMaxFields(list)
						true
					}
				}
				it.asExternalModel(mrd)
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList<StatisticsEntity>().asExternalModel(mrd)
			)
	
	val clanData: StateFlow<AccountClanInfo?> =
		databaseRepository.getPlayerClanInfo(accountId)
			.map { it.asExternalModel() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
	val vehicleData: StateFlow<List<VehicleData>> =
		databaseRepository.getAllVehiclesStatData(accountId)
			.map { stat ->
				val onlyTankId = stat.map { it.tankId }.distinct()
				val veh = mutableListOf<VehicleData>()
				databaseRepository.getExactVehiclesShortData(onlyTankId).first { list ->
					list.forEach { item ->
						val lst = stat.filter { it.tankId == item.tankId }
						if (lst.isNotEmpty()) {
							veh.add(item.asExternalModel(lst))
						}
					}
					true
				}
				veh
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList<VehicleData>()
			)
	
	fun getAccountAllData() {
		if (networkRepository.queryStatus.value == Result.Loading) return
		networkRepository.setStatus(Result.Loading)
		
		viewModelScope.launch(Dispatchers.IO) {
			// Download data from server
			val networkAccountPersonalData = networkRepository.getAccountAllData(accountId)?.data?.get(accountId.toString())
			val lastBattleTimeAccount = networkAccountPersonalData?.lastBattleTime
			val networkAccountClanData = networkRepository.getClanShortInfo(accountId)?.data?.get(accountId.toString())
			var vehicles: List<VehicleStatDataEntity> = emptyList()
			var networkVehicleInfo: List<NetworkVehicleInfoItem> = emptyList()
			if (lastBattleTimeAccount != null) {
				vehicles = retrieveVehicleShortStat(accountId, lastBattleTimeAccount, networkRepository)
				networkVehicleInfo = retrieveVehicleInfo(context, vehicles, networkRepository, databaseRepository)
			}
			
			// Add all downloaded data to database
			if (networkRepository.queryStatus.value is Result.Success) {
				savePersonalData(accountId, networkAccountPersonalData, databaseRepository)
				saveClanData(accountId, context, networkAccountClanData, databaseRepository)
				if (networkVehicleInfo.isNotEmpty()) databaseRepository.addVehiclesShortData(networkVehicleInfo)
				if (vehicles.isNotEmpty()) databaseRepository.addVehiclesStatData(vehicles)
			}
		}
	}
}

private suspend fun savePersonalData(
	accountId: Int,
	networkAccountPersonalData: NetworkAccountPersonalData?,
	databaseRepository: DatabaseRepository,
) {
	networkAccountPersonalData?.let {
		databaseRepository.getStatisticData(accountId).first { list ->
			if (list.isEmpty() || list.last().battles < it.statistics.all.battles) {
				databaseRepository.updatePersonalData(it)
				databaseRepository.addStatisticData(
					accountId,
					it.statistics.all
				)
			}
			databaseRepository.updateTime(Date().time.toString(), accountId)
			true
		}
	}
}

private suspend fun saveClanData(
	accountId: Int,
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
				accountId = accountId
			)
		}
	}
}

private suspend fun retrieveVehicleShortStat(
	accountId: Int,
	lastBattleTime: Int,
	networkRepository: NetworkRepository,
): List<VehicleStatDataEntity> {
	return when(val response = networkRepository.getVehicleShortStat(accountId)) {
		null -> emptyList()
		else -> {
			response.data?.get(accountId.toString())?.let {
				val data = it.map { item -> item.asEntity(accountId, lastBattleTime) }
				data
			} ?: emptyList()
		}
	}
}

private suspend fun retrieveVehicleInfo(
	context: Context,
	vehicles: List<VehicleStatDataEntity>,
	networkRepository: NetworkRepository,
	databaseRepository: DatabaseRepository,
): List<NetworkVehicleInfoItem> {
	val limit = 100
	var index = 0
	val newVehShort = mutableListOf<NetworkVehicleInfoItem>()
	
	// First get list of missing entries
	val listOfNewVehicles = getListOfNewVehicles(vehicles, databaseRepository)
	if (listOfNewVehicles.isEmpty()) {
		networkRepository.getVehicleInfo()
		return emptyList()
	}
	
	// Then we can request missing vehicles info from server
	networkRepository.queriesAmount += (listOfNewVehicles.size + limit - 1) / limit - 1
	while (index < listOfNewVehicles.size) {
		// The list needs to be split into parts first, since the server can only process
		// a maximum of 100 items in each request.
		val sublist = listOfNewVehicles
			.subList(index, (index + limit).coerceAtMost(listOfNewVehicles.size))
			.joinToString(separator = ",")
		
		when(val response = networkRepository.getVehicleInfo(sublist)) {
			null -> return emptyList()
			else -> {
				response.data?.let { map ->
					map.forEach { (_, item) ->
						item?.let {
							preloadImage(context, item.images.bigIcon.secure())
							newVehShort.add(item)
						}
					}
				}
			}
		}
		index += limit
	}
	return newVehShort
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
	
	databaseRepository.getVehiclesIds(idsFromNW).first { idsFromDB ->
		listOfNewVehicles.addAll( idsFromNW.filter { it !in idsFromDB } )
		true
	}
	
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
				Decoder { DecodeResult(ColorDrawable(Color.BLACK), false) }
			}
			.build()
		)

fun String.secure() = this.replace("http:", "https:")

fun Boolean.toInt() = if (this) 1 else 0