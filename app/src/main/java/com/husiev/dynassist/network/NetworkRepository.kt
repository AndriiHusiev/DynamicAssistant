package com.husiev.dynassist.network

import android.content.Context
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.logDebugOut
import com.husiev.dynassist.network.dataclasses.AppError
import com.husiev.dynassist.network.dataclasses.BaseResponse
import com.husiev.dynassist.network.dataclasses.NetworkAccountClanData
import com.husiev.dynassist.network.dataclasses.NetworkAccountInfo
import com.husiev.dynassist.network.dataclasses.NetworkAccountPersonalData
import com.husiev.dynassist.network.dataclasses.NetworkVehicleInfoItem
import com.husiev.dynassist.network.dataclasses.NetworkVehicleShortItem
import com.husiev.dynassist.network.dataclasses.ResultWrapper
import com.husiev.dynassist.network.dataclasses.asApiError
import dagger.hilt.android.qualifiers.ApplicationContext
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
	
	private suspend fun <T, R> safeApiCall(
		apiCall: suspend () -> BaseResponse<T>,
		transform: (T) -> R
	): ResultWrapper<R> {
		return try {
			val response = apiCall()
			if (response.status == "ok" && response.data != null) {
				ResultWrapper.Success(transform(response.data))
			} else if (response.error != null) {
				logDebugOut("NetworkRepository", "Error retrieving data from server", response.error)
				ResultWrapper.Error(response.error.asApiError())
			} else {
				logDebugOut("NetworkRepository", "Error retrieving data", "Invalid server response format")
				ResultWrapper.Error(
					AppError.UnknownError(IllegalStateException("Invalid server response format")))
			}
		} catch (e: Exception) {
			logDebugOut("NetworkRepository", "Error retrieving data", e)
			ResultWrapper.Error(AppError.NetworkError(e))
		}
	}
	
	suspend fun getList(search: String): ResultWrapper<List<NetworkAccountInfo>> {
		return safeApiCall(
			apiCall = { networkService.getPlayers(appId, search) },
			transform = { responseData -> responseData }
		)
	}
	
	suspend fun getAccountAllData(accountId: Int): ResultWrapper<NetworkAccountPersonalData> {
		return safeApiCall(
			apiCall = { networkService.getPersonalData(appId, accountId) },
			transform = { responseData -> responseData[accountId.toString()]!! }
		)
	}
	
	suspend fun getClanShortInfo(accountId: Int): ResultWrapper<NetworkAccountClanData?> {
		return safeApiCall(
			apiCall = { networkService.getClanMemberInfo(appId, accountId) },
			transform = { responseData -> responseData[accountId.toString()] }
		)
	}
	
	suspend fun getVehicleShortStat(accountId: Int): ResultWrapper<List<NetworkVehicleShortItem>?> {
		return safeApiCall(
			apiCall = { networkService.getVehicleShortStat(appId, accountId) },
			transform = { responseData -> responseData[accountId.toString()] }
		)
	}
	
	suspend fun getVehicleInfo(tankId: String): ResultWrapper<List<NetworkVehicleInfoItem>> {
		return safeApiCall(
			apiCall = { networkService.getVehicleInfo(appId, tankId) },
			transform = { responseData -> responseData.values.filterNotNull() }
		)
	}
}