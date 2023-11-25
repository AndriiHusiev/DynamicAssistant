package com.husiev.dynassist.sync

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import com.husiev.dynassist.R
import com.husiev.dynassist.components.start.utils.logDebugOut
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_CHANNEL_ID = "VERBOSE_NOTIFICATION"
private const val UPDATES_NOTIFICATION_GROUP = "UPDATES_NOTIFICATIONS"
private const val TARGET_ACTIVITY_NAME = "com.husiev.dynassist.components.main.MainActivity"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
class SystemTrayNotifier @Inject constructor(
	@ApplicationContext private val context: Context,
) : Notifier {
	
	override fun postNewsNotifications(lines: List<Pair<Int, String>>) = with(context) {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.POST_NOTIFICATIONS,
			) != PackageManager.PERMISSION_GRANTED
		) {
			return
		}
		
		for (line in lines) {
			// Create the notification
			val notification = createNotification {
				setContentTitle(getString(R.string.notification_title))
//					.setContentText(setNotifyText(lines, getString(R.string.notification_more)))
					.setContentText(line.second)
					.setSmallIcon(R.drawable.ic_small_notification)
					// Build info into InboxStyle template.
//					.setStyle(setNotificationStyle(lines))
					.setContentIntent(pendingIntent(line))
					.setGroup(UPDATES_NOTIFICATION_GROUP)
					.setGroupSummary(true)
					.setAutoCancel(true)
					.build()
			}
			
			// Send the notifications
			NotificationManagerCompat.from(this).notify(line.first, notification)
		}
	}
	
	private fun setNotifyText(lines: List<String>, postfix: String) =
		lines[0] + if (lines.size > 1)
			", ... + ${lines.size - 1} $postfix"
		else
			""
	
	/**
	 * Creates an inbox style summary notification for news updates
	 */
	private fun setNotificationStyle(
		lines: List<String>,
		title: String? = null,
		sum: String? = null,
	) = if (lines.size > 1) {
		lines.fold(InboxStyle()) { inboxStyle, line ->
			inboxStyle.addLine(line)
		}
			.setBigContentTitle(title)
			.setSummaryText(sum)
	} else null
}

/**
 * Creates a notification for configured updates
 */
private fun Context.createNotification(
	block: NotificationCompat.Builder.() -> Unit,
): Notification {
	ensureNotificationChannelExists()
	return NotificationCompat.Builder(
		this,
		NOTIFICATION_CHANNEL_ID,
	)
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.apply(block)
		.build()
}

/**
 * Ensures the a notification channel is present if applicable
 */
private fun Context.ensureNotificationChannelExists() {
	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
	
	val channel = NotificationChannel(
		NOTIFICATION_CHANNEL_ID,
		getString(R.string.notification_channel_name),
		NotificationManager.IMPORTANCE_HIGH,
	).apply {
		description = getString(R.string.notification_channel_description)
	}
	// Register the channel with the system
	NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

/**
 * Opens MainActivity and puts proper extras
 */
private fun Context.pendingIntent(
	account: Pair<Int, String>,
): PendingIntent? = TaskStackBuilder.create(this).run {
	// Add the intent, which inflates the back stack.
	addNextIntentWithParentStack(Intent().apply {
		action = Intent.ACTION_VIEW
		component = ComponentName(
			packageName,
			TARGET_ACTIVITY_NAME,
		)
		putExtra("nickname", account.second)
		putExtra("account_id", account.first)
	})
	// Get the PendingIntent containing the entire back stack.
	getPendingIntent(account.first,
		PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}
