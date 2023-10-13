package com.husiev.dynassist.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.husiev.dynassist.components.start.utils.logDebugOut
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.network.NetworkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
	@Assisted private val appContext: Context,
	@Assisted workerParams: WorkerParameters,
	private val databaseRepository: DatabaseRepository,
	private val networkRepository: NetworkRepository,
	private val notifier: Notifier,
): CoroutineWorker(appContext, workerParams) {
	
	override suspend fun doWork(): Result {
		return withContext(Dispatchers.IO) {
			return@withContext try {
//				logDebugOut("SyncWorker", "Current time", Date().time.toInt().asStringDate())
				val nicknames = mutableListOf<String>()
				databaseRepository.listOfPlayers.first { list ->
					if (list.isNotEmpty()) {
						for (item in list) {
							val response = networkRepository.getAccountAllData(item.id)
							if (response != null) {
								response.data?.get(item.id.toString())?.let { networkData ->
									databaseRepository.getStatisticData(item.id).first { list ->
										if (list.isEmpty() || list.last().battles < networkData.statistics.all.battles) {
											logDebugOut("SyncWorker", item.nickname, "Has new data!")
											nicknames.add(item.nickname)
										} else
											logDebugOut("SyncWorker", item.nickname, "Don't have new data!")
										true
									}
								}
							}
						}
					}
					true
				}

				if (nicknames.isNotEmpty()) {
					val message = "New data in the next accounts: " + nicknames.joinToString(separator = ", ", postfix = ".")
					notifier.postNewsNotifications(message)
				}
				
				Result.success()
			} catch (throwable: Throwable) {
				logDebugOut("SyncWorker", "Failed to sync players data", throwable)
				Result.failure()
			}
		}
	}
	
	companion object {
		fun startUpSyncWork() = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
			.setConstraints(syncConstraints)
			.build()
	}
}

private val syncConstraints = Constraints.Builder()
	.setRequiredNetworkType(NetworkType.CONNECTED)
	.build()