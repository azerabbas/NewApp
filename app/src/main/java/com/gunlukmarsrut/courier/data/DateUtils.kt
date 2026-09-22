package com.gunlukmarsrut.courier.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val azFormat get() = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

/** "22.09.26" -> "23.09.26" (offset +1), used for the "Sabah" / "Birigün" quick chips. */
fun addDaysToAzDate(title: String, days: Int): String = try {
    val cal = Calendar.getInstance()
    val parsed = azFormat.parse(title)
    if (parsed != null) cal.time = parsed
    cal.add(Calendar.DAY_OF_MONTH, days)
    azFormat.format(cal.time)
} catch (e: Exception) {
    title
}
