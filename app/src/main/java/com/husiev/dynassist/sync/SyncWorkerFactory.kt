package com.husiev.dynassist.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.network.NetworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncWorkerFactory @Inject constructor(
	private val databaseRepository: DatabaseRepository,
	private val networkRepository: NetworkRepository,
	private val notifier: Notifier,
) : WorkerFactory() {
	
	override fun createWorker(
		appContext: Context,
		workerClassName: String,
		workerParameters: WorkerParameters
	): ListenableWorker? {
		
		return when (workerClassName) {
			SyncWorker::class.java.name ->
				SyncWorker(appContext, workerParameters, databaseRepository, networkRepository, notifier)
			else ->
				// Return null, so that the base class can delegate to the default WorkerFactory.
				null
		}
	}
}