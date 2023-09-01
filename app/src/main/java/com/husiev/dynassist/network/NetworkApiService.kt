package com.husiev.dynassist.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApiService {
	
	/**
	 * Returns partial list of players. The list is filtered by initial characters of user name and sorted alphabetically.
	 * @param appId Application ID
	 * @param search Player name search string. Parameter "type" defines minimum length and type
	 * of search. Using the exact search type, you can enter several names, separated with commas.
	 * Maximum length: 24.
	 * @param type Search type. Default is "startswith". Valid values:
	 *  - "startswith" — Search by initial characters of player name. Minimum length: 3 characters. Maximum length: 24 characters. (by default)
	 *  - "exact" — Search by exact match of player name. Case insensitive. You can enter several names, separated with commas (up to 100).
	 */
	@GET("account/list/")
	suspend fun getPlayers(
		@Query("application_id") appId: String,
		@Query("search") search: String,
		@Query("type") type: String = "startswith",
	): NetworkStartSearchInfo
	
	/**
	 * Returns player details.
	 * @param appId Application ID
	 * @param accountId Player account ID
	 * @param fields Response field. The fields are separated with commas. Embedded fields are
	 * separated with dots. To exclude a field, use “-” in front of its name. In case the parameter
	 * is not defined, the method returns all fields. Maximum limit: 100.
	 */
	@GET("account/info/")
	suspend fun getPersonalData(
		@Query("application_id") appId: String,
		@Query("account_id") accountId: Int,
		@Query("fields") fields: String = "-client_language,-private,-statistics.clan," +
				"-statistics.stronghold_skirmish,-statistics.regular_team," +
				"-statistics.company,-statistics.stronghold_defense,-statistics.historical," +
				"-statistics.team,-statistics.frags,-statistics.trees_cut"
	): NetworkAccountAllData
	
	/**
	 * Returns detailed clan member information and brief clan details.
	 * @param appId Application ID
	 * @param accountId Player account ID
	 * @param fields Response field. The fields are separated with commas. Embedded fields are
	 * separated with dots. To exclude a field, use “-” in front of its name. In case the parameter
	 * is not defined, the method returns all fields. Maximum limit: 100.
	 */
	@GET("clans/accountinfo/")
	suspend fun getClanMemberInfo(
		@Query("application_id") appId: String,
		@Query("account_id") accountId: Int,
		@Query("fields") fields: String = ""
	): NetworkClanMemberInfo
	
}