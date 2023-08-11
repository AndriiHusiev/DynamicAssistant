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
	): StartSearchInfo
}