package com.husiev.dynassist.components.start.utils

import android.util.Log
import com.husiev.dynassist.database.entity.PlayersEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Date
import java.util.Locale

data class StartAccountInfo(
	val id: Int,
	val nickname: String,
	val clan: String? = null,
	val updateTime: String,
)

fun StartAccountInfo.asEntity() = PlayersEntity(
	accountId = id,
	nickname = nickname,
	clan = clan,
	updateTime = updateTime,
)

fun logDebugOut(obj: String, message: String, param: Any) {
	val compiledMessage = "$obj. $message: $param"
	val tag = "DynAssist"
	
	if (param is Throwable)
		Log.e(tag, message, param)
	else
		Log.d(tag, compiledMessage)
}

fun String.asDateTime(): String = Calendar.getInstance().let { calendar ->
		calendar.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH), 0, 0, 0)
		val neededTime = Calendar.getInstance().apply {
			time = Date(this@asDateTime.toLong())
		}
		val pattern = if (neededTime.after(calendar)) {
			"HH:mm"
		} else if (neededTime.before(calendar.add(DAY_OF_YEAR, -7))) {
			"dd.MM.yyyy"
		} else {
			"EEE"
		}
		val formatter = SimpleDateFormat(pattern, Locale.getDefault())
	
		return formatter.format(neededTime.time)
	}