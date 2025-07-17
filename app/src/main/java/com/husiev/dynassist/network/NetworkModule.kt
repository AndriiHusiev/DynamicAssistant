package com.husiev.dynassist.network

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	
	private const val BASE_URL = "https://api.worldoftanks.eu/wot/"
	
	@Provides
	@Singleton
	fun provideJson(): Json {
		return Json {
			ignoreUnknownKeys = true
			isLenient = true
		}
	}
	
	@Provides
	@Singleton
	fun provideNetworkService(json: Json): NetworkApiService {
		val retrofit: Retrofit = Retrofit.Builder()
			.addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
			.baseUrl(BASE_URL)
			.build()
		
		return retrofit.create(NetworkApiService::class.java)
	}
	
	@Provides
	@Singleton
	fun imageLoader(
		@ApplicationContext context: Context,
	): ImageLoader = ImageLoader.Builder(context)
		.diskCache {
			DiskCache.Builder()
				.directory(context.cacheDir.resolve("image_cache"))
				.maxSizePercent(0.02)
				.build()
		}
		.respectCacheHeaders(false)
		.build()
	
	@Singleton
	@Provides
	fun providesCoroutineScope(): CoroutineScope =
		CoroutineScope(SupervisorJob() + Dispatchers.IO)
}