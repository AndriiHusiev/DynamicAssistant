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
import com.husiev.dynassist.components.main.utils.AccountClanInfo
import com.husiev.dynassist.components.main.utils.AccountPersonalData
import com.husiev.dynassist.components.main.utils.AccountStatisticsData
import com.husiev.dynassist.components.main.utils.MainRoutesData
import com.husiev.dynassist.components.main.utils.Result
import com.husiev.dynassist.components.main.utils.VehicleShortData
import com.husiev.dynassist.components.main.utils.asExternalModel
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.VehicleShortDataEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.database.entity.fillMaxFields
import com.husiev.dynassist.network.NetworkRepository
import com.husiev.dynassist.network.dataclasses.asEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

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
	
	val personalData: StateFlow<AccountPersonalData?> =
		databaseRepository.getPersonalData(accountId)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null
			)
	
	val statisticData: StateFlow<Map<String, List<AccountStatisticsData>>> =
		databaseRepository.getStatisticData(accountId)
			.combine(databaseRepository.getVehiclesShortData(accountId)) { stat, veh ->
				stat.lastOrNull()?.fillMaxFields(veh)
				stat
			}
			.map { it.asExternalModel(mrd) }
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
	
	val shortData: StateFlow<List<VehicleShortData>> =
		databaseRepository.getVehiclesShortData(accountId)
			.map { it.asExternalModel() }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = emptyList<VehicleShortDataEntity>().asExternalModel()
			)
	
	fun getAccountAllData() {
		if (networkRepository.queryStatus.value == Result.Loading) return
		networkRepository.setStatus(Result.Loading)
		
		viewModelScope.launch(Dispatchers.IO) {
			// Account personal and statistic data
			val lastBattleTimeAccount: Int? =
				retrievePersonalData(accountId, networkRepository, databaseRepository)
			if (lastBattleTimeAccount != null) networkRepository.queriesAmount += 2
			
			// Clan member short data
			retrieveClanData(accountId, context, networkRepository, databaseRepository)
			
			// Vehicles short and statistic data
			if (lastBattleTimeAccount != null) {
				val vehicles = retrieveVehicleShortData(accountId, networkRepository)
				
				retrieveVehicleInfo(lastBattleTimeAccount, context, vehicles, networkRepository, databaseRepository)
			}
		}
	}
}

private suspend fun retrievePersonalData(
	accountId: Int,
	networkRepository: NetworkRepository,
	databaseRepository: DatabaseRepository,
): Int? {
	var lastBattleTimeAccount: Int? = null
	when(val response = networkRepository.getAccountAllData(accountId)) {
		null -> return null
		else -> {
			response.data?.get(accountId.toString())?.let { networkData ->
				databaseRepository.getStatisticData(accountId).first { list ->
					if (list.isEmpty() || list.last().battles < networkData.statistics.all.battles) {
						lastBattleTimeAccount = networkData.lastBattleTime
						databaseRepository.updatePersonalData(networkData)
						databaseRepository.addStatisticData(
							accountId,
							networkData.statistics.all
						)
					}
					databaseRepository.updateTime(Date().time.toString(), accountId)
					true
				}
			}
		}
	}
	
	return lastBattleTimeAccount
}

private suspend fun retrieveClanData(
	accountId: Int,
	context: Context,
	networkRepository: NetworkRepository,
	databaseRepository: DatabaseRepository,
) {
	when(val response = networkRepository.getClanShortInfo(accountId)) {
		null -> return
		else -> {
			response.data?.get(accountId.toString())?.let { clanData ->
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
	}
}

private suspend fun retrieveVehicleShortData(
	accountId: Int,
	networkRepository: NetworkRepository,
): List<VehicleShortDataEntity> {
	return when(val response = networkRepository.getVehicleShortData(accountId)) {
		null -> emptyList()
		else -> response.data?.get(accountId.toString())?.let {
			it.map { item -> item.asEntity(accountId) }
		} ?: emptyList()
	}
}

private suspend fun retrieveVehicleInfo(
	lastBattleTimeAccount: Int,
	context: Context,
	vehicles: List<VehicleShortDataEntity>,
	networkRepository: NetworkRepository,
	databaseRepository: DatabaseRepository,
) {
	val limit = 100
	var index = 0
	networkRepository.queriesAmount += (vehicles.size + limit - 1) / limit - 1
	while (index < vehicles.size) {
		// The list needs to be split into parts first, since the server can only process
		// a maximum of 100 items in each request.
		val sublist = vehicles
			.subList(index, (index + limit).coerceAtMost(vehicles.size))
			.map { it.tankId }
			.joinToString(separator = ",")
		
		when(val response = networkRepository.getVehicleInfo(sublist)) {
			null -> return
			else -> {
				response.data?.let {  map ->
					map.forEach { (key, item) ->
						val vehicle = vehicles.singleOrNull { it.tankId.toString() == key }
						if (vehicle != null && item != null) {
							preloadImage(context, item.images.bigIcon.secure())
							vehicle.apply {
								lastBattleTime = lastBattleTimeAccount
								name = item.name
								type = item.type
								description = item.description
								nation = item.nation
								urlSmallIcon = item.images.smallIcon.secure()
								urlBigIcon = item.images.bigIcon.secure()
								tier = item.tier
								priceGold = item.priceGold
								priceCredit = item.priceCredit
								isPremium = item.isPremium
								isGift = item.isGift
								isWheeled = item.isWheeled
							}
						}
					}
				}
			}
		}
		index += limit
	}
	databaseRepository.addVehiclesShortData(vehicles)
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