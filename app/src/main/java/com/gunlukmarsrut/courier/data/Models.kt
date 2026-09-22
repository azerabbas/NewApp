package com.gunlukmarsrut.courier.data

import androidx.compose.ui.geometry.Offset

enum class DayState { ACTIVE, UPCOMING, FINISHED }

enum class DeliveryMode { BOTH_SIDES, ONE_DIRECTION }

enum class StopStatus { PENDING, DELIVERED, TRANSFERRED, X }

data class Stop(
    val id: String,
    val orderNumber: Int,
    val fullName: String,
    val phone: String,
    val address: String,
    val note: String? = null,
    val status: StopStatus = StopStatus.PENDING,
    val photoCount: Int = 0,
    /** Set when this parcel was carried over from an earlier day, e.g. "22.09.26". */
    val transferredFromDate: String? = null,
    /** Set when status == TRANSFERRED: the day it now lives on, e.g. "23.09.26". */
    val transferredToDate: String? = null,
    /** Set when status == X: why it was pulled out. */
    val xReason: String? = null,
    /** Fractional position (0..1) inside the map preview canvas. */
    val mapPosition: Offset = Offset(0.5f, 0.5f),
)

data class Day(
    val id: String,
    /** Display title, e.g. "22.09.26". */
    val title: String,
    val state: DayState,
    val mode: DeliveryMode = DeliveryMode.BOTH_SIDES,
    val stops: List<Stop> = emptyList(),
)

val Day.nonXStops: List<Stop> get() = stops.filter { it.status != StopStatus.X }
val Day.xStops: List<Stop> get() = stops.filter { it.status == StopStatus.X }
val Day.pendingStops: List<Stop> get() = nonXStops.filter { it.status == StopStatus.PENDING }.sortedBy { it.orderNumber }
val Day.deliveredStops: List<Stop> get() = nonXStops.filter { it.status == StopStatus.DELIVERED }.sortedBy { it.orderNumber }
val Day.transferredStops: List<Stop> get() = nonXStops.filter { it.status == StopStatus.TRANSFERRED }.sortedBy { it.orderNumber }

val Day.totalCount: Int get() = nonXStops.size
val Day.completedCount: Int get() = deliveredStops.size + transferredStops.size
val Day.progress: Float get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()

/** Stops carried in from an earlier day — drives the "3 keçirilmiş bağlama" line on Upcoming day cards. */
val Day.incomingTransferCount: Int get() = nonXStops.count { it.transferredFromDate != null }

/** For actual tel:/wa.me intents only — the UI itself never displays +994. */
fun azPhoneToE164Digits(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    val national = if (digits.startsWith("0")) digits.drop(1) else digits
    return "994$national"
}

/** Formats a raw 10-digit AZ phone as "050 123 45 67". Never shows +994. */
fun formatAzPhone(raw: String): String {
    val digits = raw.filter { it.isDigit() }.take(10)
    val sb = StringBuilder()
    for (i in digits.indices) {
        if (i == 3 || i == 6 || i == 8) sb.append(' ')
        sb.append(digits[i])
    }
    return sb.toString()
}
