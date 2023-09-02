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
}