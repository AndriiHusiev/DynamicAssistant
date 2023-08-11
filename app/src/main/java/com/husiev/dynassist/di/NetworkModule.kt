package com.husiev.dynassist.di

import com.husiev.dynassist.network.NetworkApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	
	private const val baseUrl = "https://api.worldoftanks.eu/wot/"
	
	@Provides
	@Singleton
	fun provideNetworkService(): NetworkApiService {
		val retrofit: Retrofit = Retrofit.Builder()
			.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
			.baseUrl(baseUrl)
			.build()
		
		return retrofit.create(NetworkApiService::class.java)
	}
}