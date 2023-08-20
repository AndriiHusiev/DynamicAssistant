package com.husiev.dynassist.network

import android.content.Context
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.logDebugOut
import dagger.hilt.android.qualifiers.ApplicationContext
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
	
	suspend fun getAccountAllData(accountId: Int) = flow {
		emit(MainNetworkState.Loading)
		try {
			val response = networkService.getPersonalData(appId, accountId)
			emit(MainNetworkState.Success(response))
		} catch (exception: IOException) {
			logDebugOut("NetworkRepository", "Failed to get player personal data", exception)
			emit(MainNetworkState.LoadFailed)
		}
	}
}