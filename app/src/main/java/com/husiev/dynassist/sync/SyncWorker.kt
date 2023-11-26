package com.husiev.dynassist.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.husiev.dynassist.R
import com.husiev.dynassist.components.main.utils.getMainAvg
import com.husiev.dynassist.components.main.utils.toScreen
import com.husiev.dynassist.components.start.composables.NotifyEnum
import com.husiev.dynassist.components.start.utils.logDebugOut
import com.husiev.dynassist.database.DatabaseRepository
import com.husiev.dynassist.database.entity.asStringDate
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
				val nicknames = mutableListOf<AccountNotifierShortData>()
				databaseRepository.checkedPlayers.first { list ->
					if (list.isNotEmpty()) {
						for (item in list) {
							val response = networkRepository.getAccountAllData(item.id, false)
							if (response != null) {
								response.data?.get(item.id.toString())?.let { networkData ->
									databaseRepository.getStatisticData(item.id).first { list ->
										if (list.isEmpty() || list.last().battles < networkData.statistics.all.battles) {
											databaseRepository.updateNotification(NotifyEnum.UPDATES_AVAIL.ordinal, item.id)
											nicknames.add(setShortData(
												accountId = item.id,
												nickname = item.nickname,
												lastBattleTime = networkData.lastBattleTime,
												battles = networkData.statistics.all.battles - list.last().battles,
												wins = networkData.statistics.all.wins - list.last().wins
											))
										}
										true
									}
								}
							}
						}
					}
					true
				}

				if (nicknames.isNotEmpty()) {
					notifier.postNewsNotifications(nicknames)
				}
				
				Result.success()
			} catch (throwable: Throwable) {
				logDebugOut("SyncWorker", "Failed to sync players data", throwable)
				Result.failure()
			}
		}
	}
	
	private fun setShortData(
		accountId: Int,
		nickname: String,
		battles: Int,
		wins: Int,
		lastBattleTime: Int,
	) = AccountNotifierShortData(
		accountId = accountId,
		nickname = nickname,
		lastBattleTime = appContext.resources.getString(R.string.last_battle_time) + ": " + lastBattleTime.asStringDate(),
		battlesExpanded = appContext.resources.getString(R.string.vehicle_battles) + ": +$battles",
		battlesCollapsed = "+$battles " + appContext.resources.getString(R.string.battles),
		winRate = appContext.resources.getString(R.string.vehicle_win_rate) + ": " + getMainAvg(wins, battles).toScreen(100f, "%"),
	)
	
	companion object {
		fun startUpSyncWork() = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
			.setConstraints(syncConstraints)
			.build()
		fun startUpSyncTestOneWork() = OneTimeWorkRequestBuilder<SyncWorker>()
			.setConstraints(syncConstraints)
			.build()
	}
}

private val syncConstraints = Constraints.Builder()
	.setRequiredNetworkType(NetworkType.CONNECTED)
	.build()