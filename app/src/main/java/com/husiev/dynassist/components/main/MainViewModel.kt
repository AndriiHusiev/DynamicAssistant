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
import com.husiev.dynassist.components.main.utils.asExternalModel
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.StatisticsEntity
import com.husiev.dynassist.database.entity.asExternalModel
import com.husiev.dynassist.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
	
	fun getAccountAllData() {
		viewModelScope.launch(Dispatchers.IO) {
			
			when(val response = networkRepository.getAccountAllData(accountId)) {
				null -> return@launch
				else -> {
					response.data?.get(accountId.toString())?.let { networkData ->
						databaseRepository.getStatisticData(accountId).first { list ->
							if (list.isEmpty() || list.last().battles < networkData.statistics.all.battles) {
								databaseRepository.updatePersonalData(networkData)
								databaseRepository.addStatisticData(accountId, networkData.statistics.all)
							}
							databaseRepository.updateTime(Date().time.toString(), accountId)
							true
						}
					}
				}
			}
			
			when(val response = networkRepository.getClanShortInfo(accountId)) {
				null -> return@launch
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
	}
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