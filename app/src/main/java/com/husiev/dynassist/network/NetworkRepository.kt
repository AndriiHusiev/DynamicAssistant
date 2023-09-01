package com.husiev.dynassist.network

import android.content.Context
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.Result
import com.husiev.dynassist.components.start.utils.logDebugOut
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
	@ApplicationContext context: Context,
	private val networkService: NetworkApiService,
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
	
	suspend fun getAccountAllData(accountId: Int) =
		try {
			noConnection.emit(Result.Loading)
			networkService.getPersonalData(appId, accountId)
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get player personal data", exception)
			noConnection.emit(Result.Error(exception))
			null
		}
	
	suspend fun getClanShortInfo(accountId: Int) =
		try {
			val response = networkService.getClanMemberInfo(appId, accountId)
			noConnection.emit(Result.Success(response.status))
			response
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get player clan data", exception)
			noConnection.emit(Result.Error(exception))
			null
		}
	
	val noConnection = MutableStateFlow<Result<String>>(Result.StandBy)
}