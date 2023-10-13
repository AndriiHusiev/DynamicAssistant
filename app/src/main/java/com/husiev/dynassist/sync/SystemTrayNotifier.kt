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
	
	override fun postNewsNotifications(message: String) = with(context) {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.POST_NOTIFICATIONS,
			) != PackageManager.PERMISSION_GRANTED
		) {
			return
		}
		
		// Create the notification
		val notification = createNotification {
			val title = getString(R.string.notification_title)
			setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(R.drawable.ic_small_notification)
				// Build info into InboxStyle template.
				.setStyle(newsNotificationStyle(message, title))
				.setGroup(UPDATES_NOTIFICATION_GROUP)
				.setGroupSummary(true)
				.setAutoCancel(true)
				.build()
		}
		
		// Send the notifications
		NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
	}
	
	/**
	 * Creates an inbox style summary notification for news updates
	 */
	private fun newsNotificationStyle(message: String, title: String) = InboxStyle()
		.addLine(message)
		.setBigContentTitle(title)
		.setSummaryText(title)
}

/**
 * Creates a notification for configured for updates
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