package com.husiev.dynassist

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.husiev.dynassist.sync.Sync
import com.husiev.dynassist.sync.SyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class DynAssistApplication: Application(), ImageLoaderFactory, Configuration.Provider {
	@Inject
	lateinit var imageLoader: Provider<ImageLoader>
	
	@Inject
	lateinit var workerFactory: SyncWorkerFactory

	override fun getWorkManagerConfiguration() =
		Configuration.Builder()
			.setMinimumLoggingLevel(android.util.Log.INFO)
			.setWorkerFactory(workerFactory)
			.build()
	
	override fun onCreate() {
		super.onCreate()
		// Initialize Sync; the system responsible for keeping data in the app up to date.
		Sync.initialize(context = this)
	}
	
	override fun newImageLoader(): ImageLoader = imageLoader.get()
}