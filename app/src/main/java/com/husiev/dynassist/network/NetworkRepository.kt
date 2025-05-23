package com.husiev.dynassist.network

import android.content.Context
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.Result
import com.husiev.dynassist.components.start.utils.logDebugOut
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
	@ApplicationContext context: Context,
	private val networkService: NetworkApiService,
	private val coroutineScope: CoroutineScope,
) {
	/**
	 * It is an application identification key used to send requests to API.
	 * The access to Wargaming.net content is granted only if your application has Application ID.
	 * It can be generated at https://developers.wargaming.net/
	 */
	private val appId = context.resources.getString(R.string.application_id)
	
	suspend fun getList(search: String) = flow {
		emit(SearchResultUiState.Loading)
		try {
			val response = networkService.getPlayers(appId, search)
			emit(SearchResultUiState.Success(response))
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get list of players", exception)
			emit(SearchResultUiState.LoadFailed)
		}
	}
	
	suspend fun getAccountAllData(accountId: Int, chained: Boolean = true) =
		try {
			val response = networkService.getPersonalData(appId, accountId)
			if (chained)
				_queryStatus.emit(Result.Success("personal"))
			response
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get player personal data", exception)
			if (chained)
				_queryStatus.emit(Result.Error(exception))
			null
		}
	
	suspend fun getClanShortInfo(accountId: Int) =
		try {
			val response = networkService.getClanMemberInfo(appId, accountId)
			_queryStatus.emit(Result.Success("clan"))
			response
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get player clan data", exception)
			_queryStatus.emit(Result.Error(exception))
			null
		}
	
	suspend fun getVehicleShortStat(accountId: Int) =
		try {
			val response = networkService.getVehicleShortStat(appId, accountId)
			_queryStatus.emit(Result.Success("vehicles short stat"))
			response
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get details on player's vehicles", exception)
			_queryStatus.emit(Result.Error(exception))
			null
		}
	
	suspend fun getVehicleInfo(tankId: String? = null) =
		if (tankId == null) {
			_queryStatus.emit(Result.Success("all vehicles info is already downloaded"))
			null
		} else try {
			val response = networkService.getVehicleInfo(appId, tankId)
			_queryStatus.emit(Result.Success("vehicles info; _queryCounter = $_queryCounter"))
			response
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get list of available vehicles", exception)
			_queryStatus.emit(Result.Error(exception))
			null
		}
	
	private var _queryCounter = 0
	private val _queryStatus = MutableStateFlow<Result<String>>(Result.StandBy)
	val queryStatus = MutableStateFlow<Result<String>>(Result.StandBy)
	var queriesAmount = QUERIES_AMOUNT
	
	init {
		coroutineScope.launch {
			_queryStatus.collect {
				logDebugOut("NetworkRepository", "queryStatus", it)
				when(it) {
					is Result.Success -> {
						_queryCounter++
						if (_queryCounter < queriesAmount) {
							queryStatus.emit(Result.Loading)
						} else {
							_queryCounter = 0
							queriesAmount = QUERIES_AMOUNT
							queryStatus.emit(Result.Success("ok"))
						}
					}
					
					else -> queryStatus.emit(it)
				}
			}
		}
	}
	
	fun setStatus(status: Result<String>) = _queryStatus.tryEmit(status)
}

private const val QUERIES_AMOUNT = 2