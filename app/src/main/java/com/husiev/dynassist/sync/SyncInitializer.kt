package com.husiev.dynassist.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
	// This method is initializes sync, the process that keeps the app's data current.
	// It is called from the app module's Application.onCreate() and should be only done once.
	fun initialize(context: Context) {
		WorkManager
			.getInstance(context)
			.enqueueUniquePeriodicWork(
				SyncWorkName,
				ExistingPeriodicWorkPolicy.KEEP,
				SyncWorker.startUpSyncWork(),
			)
//			.enqueueUniqueWork(
//				SyncWorkName,
//				ExistingWorkPolicy.KEEP,
//				SyncWorker.startUpSyncWork(),
//			)
	}
}

// This name should not be changed otherwise the app may have concurrent sync requests running
internal const val SyncWorkName = "SyncWorkName"