package com.gunlukmarsrut.courier.data

import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

private val names = listOf(
    "Arzu Qurbanova", "Elvin Məmmədov", "Aygün Hüseynova", "Rəşad Əliyev", "Nərmin Cəfərova",
    "Tural Abbasov", "Günel Rzayeva", "Kamran Süleymanov", "Aytac Novruzova", "Orxan Quliyev",
    "Səbinə Hətəmova", "Vüqar İsmayılov", "Leyla Bayramova", "Elnur Sadıqov", "Nigar Əhmədova",
    "Fərid Talıbov", "Zeynəb Muradova", "Anar Vəliyev", "Ülviyyə Qasımova", "Ramin Nağıyev",
    "Şəbnəm Əkbərova", "Toğrul Hacıyev", "Günay Səfərova", "Murad Babayev", "Sevinc Yusifova",
    "Elgün Rəhimov", "Aynur Kərimova", "Cavid Məlikov", "Xəyalə Əsgərova", "Samir Orucov",
    "Nərgiz Fətullayeva", "Emin Bağırov", "Günəş Qədirova", "Vüsal Tağıyev", "Lalə Zeynalova",
    "Rasim Əliqulov", "Aysel Nəsirova", "Fuad Hümbətov", "Nazrin Mustafayeva", "Tofiq Cabbarov",
)

private val streets = listOf(
    "Nizami küçəsi 34", "Zərdabi prospekti 88", "28 May küçəsi 12", "Bakıxanov küçəsi 5",
    "Həsən bəy Zərdabi pr. 45", "Neftçilər prospekti 120", "Azadlıq prospekti 67",
    "Atatürk prospekti 23", "Cəfər Cabbarlı küçəsi 44", "Xocalı prospekti 33",
    "8-ci mikrorayon, bl. 12", "Sülh küçəsi 19", "Nobel prospekti 3", "Bülbül prospekti 15",
    "Rəsul Rza küçəsi 7", "Qara Qarayev prospekti 56", "20 Yanvar küçəsi 9",
    "Fətəli xan Xoyski küçəsi 21", "Görogli küçəsi 14", "Yeni Yasamal, 4-cü döngə",
)

private val notes = listOf(
    "Həyətdə mühafizəçiyə ver", "Qapını döyün, zəng işləmir", "2-ci mərtəbə, kod 45#",
    "Axşam saat 18-dən sonra", "Ofisə çatdırın, resepşnə ver", null, null, null,
)

private val xReasons = listOf(
    "Ünvanda kimsə yoxdur", "Nömrə cavab vermir", "Sifarişi ləğv etdi",
    "Yanlış ünvan göstərilib", "Müştəri imtina etdi",
)

private fun phoneFor(seed: Int): String {
    val r = Random(seed * 7919 + 13)
    val prefixes = listOf("50", "51", "55", "70", "77", "99")
    val prefix = prefixes[r.nextInt(prefixes.size)]
    val rest = (0 until 7).joinToString("") { r.nextInt(10).toString() }
    return "0$prefix$rest"
}

/** Deterministic snake-like route across the map preview so pins read as a real route. */
private fun routePositions(count: Int): List<Offset> {
    val rows = 6
    val positions = mutableListOf<Offset>()
    val r = Random(42)
    for (i in 0 until count) {
        val row = i % rows
        val progressInRow = (i / rows).toFloat()
        val perRow = (count / rows.toFloat()).coerceAtLeast(1f)
        val t = (progressInRow / perRow).coerceIn(0f, 1f)
        val x = if (row % 2 == 0) 0.08f + t * 0.84f else 0.92f - t * 0.84f
        val y = 0.08f + row * (0.84f / (rows - 1)) + (r.nextFloat() - 0.5f) * 0.03f
        positions += Offset(x.coerceIn(0.04f, 0.96f), y.coerceIn(0.04f, 0.96f))
    }
    return positions
}

private fun buildStops(
    count: Int,
    deliveredCount: Int = 0,
    transferredCount: Int = 0,
    transferredToDate: String? = null,
    xCount: Int = 0,
    incomingFromDate: String? = null,
    incomingCount: Int = 0,
): List<Stop> {
    val positions = routePositions(count)
    val stops = mutableListOf<Stop>()
    for (i in 0 until count) {
        val name = names[i % names.size]
        val street = streets[i % streets.size]
        val note = notes[(i * 3) % notes.size]
        val status = when {
            i < deliveredCount -> StopStatus.DELIVERED
            i < deliveredCount + transferredCount -> StopStatus.TRANSFERRED
            i < deliveredCount + transferredCount + xCount -> StopStatus.X
            else -> StopStatus.PENDING
        }
        stops += Stop(
            id = "stop-$count-$i",
            orderNumber = i + 1,
            fullName = name,
            phone = phoneFor(i + count),
            address = street,
            note = note,
            status = status,
            photoCount = if (status == StopStatus.DELIVERED || status == StopStatus.TRANSFERRED) 1 + (i % 3) else 0,
            transferredFromDate = if (incomingFromDate != null && i < incomingCount) incomingFromDate else null,
            transferredToDate = if (status == StopStatus.TRANSFERRED) transferredToDate else null,
            xReason = if (status == StopStatus.X) xReasons[i % xReasons.size] else null,
            mapPosition = positions[i],
        )
    }
    return stops
}

object SampleData {

    /** Active day: 58 stops, 12 delivered, 2 transferred onward, 3 pulled out to X. */
    val activeDay = Day(
        id = "day-2026-09-22",
        title = "22.09.26",
        state = DayState.ACTIVE,
        mode = DeliveryMode.BOTH_SIDES,
        stops = buildStops(
            count = 58,
            deliveredCount = 12,
            transferredCount = 2,
            transferredToDate = "23.09.26",
            xCount = 3,
        ),
    )

    /** Upcoming day: fully viewable, not yet active, 3 parcels carried over from 22.09.26. */
    val upcomingDay = Day(
        id = "day-2026-09-23",
        title = "23.09.26",
        state = DayState.UPCOMING,
        mode = DeliveryMode.BOTH_SIDES,
        stops = buildStops(
            count = 9,
            incomingFromDate = "22.09.26",
            incomingCount = 3,
        ),
    )

    /** Finished day: yesterday, activation moved everything else along; 1 X left behind. */
    val finishedDay = Day(
        id = "day-2026-09-20",
        title = "20.09.26",
        state = DayState.FINISHED,
        mode = DeliveryMode.ONE_DIRECTION,
        stops = buildStops(
            count = 22,
            deliveredCount = 20,
            transferredCount = 1,
            transferredToDate = "21.09.26",
            xCount = 1,
        ),
    )

    /** A second, quiet finished day further back, fully clean (no X). */
    val finishedDayClean = Day(
        id = "day-2026-09-19",
        title = "19.09.26",
        state = DayState.FINISHED,
        stops = buildStops(count = 14, deliveredCount = 14),
    )

    val allDays: List<Day> = listOf(activeDay, upcomingDay, finishedDay, finishedDayClean)

    val emptyDays: List<Day> = emptyList()

    /** A day with no addresses yet, right after creation. */
    val freshEmptyActiveDay = Day(
        id = "day-2026-09-24",
        title = "24.09.26",
        state = DayState.ACTIVE,
        stops = emptyList(),
    )
}
