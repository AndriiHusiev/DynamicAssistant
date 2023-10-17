package com.husiev.dynassist.sync

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import com.husiev.dynassist.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_ID = 1
private const val NOTIFICATION_CHANNEL_ID = "VERBOSE_NOTIFICATION"
private const val UPDATES_NOTIFICATION_GROUP = "UPDATES_NOTIFICATIONS"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
class SystemTrayNotifier @Inject constructor(
	@ApplicationContext private val context: Context,
) : Notifier {
	
	override fun postNewsNotifications(lines: List<String>) = with(context) {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.POST_NOTIFICATIONS,
			) != PackageManager.PERMISSION_GRANTED
		) {
			return
		}
		
		// Create the notification
		val notification = createNotification {
			setContentTitle(getString(R.string.notification_title))
				.setContentText(setNotifyText(lines, getString(R.string.notification_more)))
				.setSmallIcon(R.drawable.ic_small_notification)
				// Build info into InboxStyle template.
				.setStyle(setNotificationStyle(lines))
				.setGroup(UPDATES_NOTIFICATION_GROUP)
				.setGroupSummary(true)
				.setAutoCancel(true)
				.build()
		}
		
		// Send the notifications
		NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
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