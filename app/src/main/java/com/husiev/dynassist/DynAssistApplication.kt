package com.husiev.dynassist

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.husiev.dynassist.sync.Sync
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class DynAssistApplication: Application(), ImageLoaderFactory, Configuration.Provider {
	@Inject
	lateinit var imageLoader: Provider<ImageLoader>
	
	@EntryPoint
	@InstallIn(SingletonComponent::class)
	interface HiltWorkerFactoryEntryPoint {
		fun workerFactory(): HiltWorkerFactory
	}

	override val workManagerConfiguration =
		Configuration.Builder()
			.setMinimumLoggingLevel(android.util.Log.INFO)
			.setWorkerFactory(EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory())
			.build()
	
	override fun onCreate() {
		super.onCreate()
		// Initialize Sync; the system responsible for keeping data in the app up to date.
		Sync.initialize(context = this)
	}
	
	override fun newImageLoader(): ImageLoader = imageLoader.get()
}