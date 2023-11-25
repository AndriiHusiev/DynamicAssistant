package com.husiev.dynassist.sync

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
	fun postNewsNotifications(lines: List<Pair<Int, String>>)
}