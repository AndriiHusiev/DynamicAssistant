package com.husiev.dynassist.sync

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
	fun postNewsNotifications(message: String)
}