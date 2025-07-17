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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
	@ApplicationContext context: Context,
	private val networkService: NetworkApiService,
	private val json: Json,
) {
	/**
	 * It is an application identification key used to send requests to API.
	 * The access to Wargaming.net content is granted only if your application has Application ID.
	 * It can be generated at https://developers.wargaming.net/
	 */
	private val appId = context.resources.getString(R.string.application_id)
	
	private suspend fun <R> safeApiCall(
		apiCall: suspend () -> BaseResponse,
		transform: (JsonElement) -> R
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
			logDebugOut("NetworkRepository", "NetworkError. Error retrieving data", e)
			ResultWrapper.Error(AppError.NetworkError(e))
		}
	}
	
	suspend fun getList(search: String): ResultWrapper<List<NetworkAccountInfo>> {
		return safeApiCall(
			apiCall = { networkService.getPlayers(appId, search) },
			transform = { responseData ->
				if (responseData is JsonObject && responseData["0"]?.jsonPrimitive == null) {
					throw IllegalStateException("Unexpected data format for list of players")
				} else
					json.decodeFromJsonElement<List<NetworkAccountInfo>>(responseData)
			}
		)
	}
	
	suspend fun getAccountAllData(accountId: Int): ResultWrapper<NetworkAccountPersonalData> {
		return safeApiCall(
			apiCall = { networkService.getPersonalData(appId, accountId) },
			transform = { responseData ->
				if (responseData is JsonObject) {
					val accountDataElement = responseData[accountId.toString()]
					if (accountDataElement == null || accountDataElement is JsonNull) {
						throw IllegalStateException("Unexpected data format for list of players")
					} else {
						if (accountDataElement is JsonObject) {
							json.decodeFromJsonElement(
								NetworkAccountPersonalData.serializer(),
								accountDataElement
							)
						} else {
							throw IllegalStateException("Unexpected data format for list of players")
						}
					}
				} else {
					throw IllegalStateException("Unexpected data format for account personal data")
				}
			}
		)
	}
	
	suspend fun getClanShortInfo(accountId: Int): ResultWrapper<NetworkAccountClanData?> {
		return safeApiCall(
			apiCall = { networkService.getClanMemberInfo(appId, accountId) },
			transform = { responseData ->
				if (responseData is JsonObject) {
					val clanInfo = responseData[accountId.toString()]
					if (clanInfo == null || clanInfo is JsonNull) {
						null
					} else {
						if (clanInfo is JsonObject) {
							json.decodeFromJsonElement(
								NetworkAccountClanData.serializer(),
								clanInfo
							)
						} else
							null
					}
				} else {
					throw IllegalStateException("Unexpected data format for clan data")
				}
			}
		)
	}
	
	suspend fun getVehicleShortStat(accountId: Int): ResultWrapper<List<NetworkVehicleShortItem>> {
		return safeApiCall(
			apiCall = { networkService.getVehicleShortStat(appId, accountId) },
			transform = { responseData ->
				if (responseData is JsonObject) {
					val vehicleStat = responseData[accountId.toString()]!!
					json.decodeFromJsonElement(
						serializer<List<NetworkVehicleShortItem>>(),
						vehicleStat
					)
				} else {
					throw IllegalStateException("Unexpected data format for vehicle stat data")
				}
			}
		)
	}
	
	suspend fun getVehicleInfo(tankId: String): ResultWrapper<List<NetworkVehicleInfoItem>> {
		return safeApiCall(
			apiCall = { networkService.getVehicleInfo(appId, tankId) },
			transform = { responseData -> //responseData.values.filterNotNull() }
				if (responseData is JsonObject) {
					responseData.values.mapNotNull { element ->
						if (element !is JsonNull && element is JsonObject) {
							try {
								json.decodeFromJsonElement(NetworkVehicleInfoItem.serializer(), element)
							} catch (e: Exception) {
								logDebugOut("NetworkRepository", "Error deserializing vehicle info item", "${e.message}")
								null
							}
						} else {
							null
						}
					}
				} else
					throw IllegalStateException("Unexpected data format for vehicle info")
			}
		)
	}
}