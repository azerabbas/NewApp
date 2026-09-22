package com.gunlukmarsrut.courier.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Day
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.DeliveryMode
import com.gunlukmarsrut.courier.data.SampleData
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.data.addDaysToAzDate
import com.gunlukmarsrut.courier.data.azPhoneToE164Digits
import com.gunlukmarsrut.courier.data.completedCount
import com.gunlukmarsrut.courier.data.deliveredStops
import com.gunlukmarsrut.courier.data.nonXStops
import com.gunlukmarsrut.courier.data.pendingStops
import com.gunlukmarsrut.courier.data.totalCount
import com.gunlukmarsrut.courier.data.transferredStops
import com.gunlukmarsrut.courier.data.xStops
import com.gunlukmarsrut.courier.ui.components.AddPhotoSheet
import com.gunlukmarsrut.courier.ui.components.AddressFormResult
import com.gunlukmarsrut.courier.ui.components.AddressFormSheet
import com.gunlukmarsrut.courier.ui.components.AppSnackbarHost
import com.gunlukmarsrut.courier.ui.components.CannotDeliverSheet
import com.gunlukmarsrut.courier.ui.components.CompletedStopRow
import com.gunlukmarsrut.courier.ui.components.ConfirmDialog
import com.gunlukmarsrut.courier.ui.components.DayStateBadge
import com.gunlukmarsrut.courier.ui.components.MoveToAnotherDaySheet
import com.gunlukmarsrut.courier.ui.components.NavigationChoiceSheet
import com.gunlukmarsrut.courier.ui.components.PrimaryButton
import com.gunlukmarsrut.courier.ui.components.RouteMapPreview
import com.gunlukmarsrut.courier.ui.components.SegmentedControl
import com.gunlukmarsrut.courier.ui.components.SendPhotosSheet
import com.gunlukmarsrut.courier.ui.components.StopCard
import com.gunlukmarsrut.courier.ui.components.XCountBadge
import com.gunlukmarsrut.courier.ui.components.showAppSnackbar
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme
import com.gunlukmarsrut.courier.ui.theme.Sizing
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import com.gunlukmarsrut.courier.ui.util.LightDarkPreview
import kotlinx.coroutines.launch

private sealed class DaySheet {
    data class Navigation(val stopId: String) : DaySheet()
    data class AddPhoto(val stopId: String) : DaySheet()
    data class CannotDeliver(val stopId: String) : DaySheet()
    data class Edit(val stopId: String) : DaySheet()
    data class Transfer(val stopId: String) : DaySheet()
    object SendPhotos : DaySheet()
}

/** The main work screen — behaves differently for ACTIVE / UPCOMING / FINISHED days. */
@Composable
fun DayScreen(
    initialDay: Day,
    onBack: () -> Unit,
    onOpenXList: (Day) -> Unit,
    modifier: Modifier = Modifier,
) {
    var day by remember(initialDay.id) { mutableStateOf(initialDay) }
    var sheet by remember { mutableStateOf<DaySheet?>(null) }
    var showActivateConfirm by remember { mutableStateOf(false) }
    var deliveredExpanded by remember { mutableStateOf(false) }
    var transferredExpanded by remember { mutableStateOf(false) }
    var recipientNumber by remember { mutableStateOf("") }
    var businessIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun updateStop(id: String, transform: (Stop) -> Stop) {
        day = day.copy(stops = day.stops.map { if (it.id == id) transform(it) else it })
    }

    fun openMaps(stop: Stop, waze: Boolean) {
        try {
            val uri = if (waze) {
                Uri.parse("https://waze.com/ul?q=" + Uri.encode(stop.address) + "&navigate=yes")
            } else {
                Uri.parse("geo:0,0?q=" + Uri.encode(stop.address))
            }
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            scope.launch { snackbarHostState.showAppSnackbar("Naviqasiya tətbiqi tapılmadı") }
        }
        sheet = null
    }

    fun callStop(stop: Stop) {
        try {
            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+${azPhoneToE164Digits(stop.phone)}")))
        } catch (e: ActivityNotFoundException) {
            scope.launch { snackbarHostState.showAppSnackbar("Zəng tətbiqi tapılmadı") }
        }
    }

    fun whatsAppStop(stop: Stop) {
        try {
            val uri = Uri.parse("https://wa.me/" + azPhoneToE164Digits(stop.phone))
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            scope.launch { snackbarHostState.showAppSnackbar("WhatsApp tapılmadı") }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.appColors.background,
        topBar = { DayTopBar(day, onBack, onOpenXList) },
        bottomBar = {
            if (day.state != DayState.UPCOMING) {
                val totalPhotos = day.stops.sumOf { it.photoCount }
                Box(modifier = Modifier.background(MaterialTheme.appColors.surface).padding(Spacing.screenMargin)) {
                    PrimaryButton(
                        text = "Şəkilləri göndər · $totalPhotos",
                        onClick = { sheet = DaySheet.SendPhotos },
                        enabled = totalPhotos > 0,
                    )
                }
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (day.state) {
                DayState.UPCOMING -> UpcomingBanner(day.title) { showActivateConfirm = true }
                DayState.FINISHED -> FinishedBanner()
                DayState.ACTIVE -> {}
            }

            if (day.state != DayState.UPCOMING) {
                Box(modifier = Modifier.padding(horizontal = Spacing.screenMargin, vertical = Spacing.xs)) {
                    SegmentedControl(
                        options = listOf("Hər iki tərəf", "Bir istiqamətli"),
                        selectedIndex = if (day.mode == DeliveryMode.BOTH_SIDES) 0 else 1,
                        onSelect = { day = day.copy(mode = if (it == 0) DeliveryMode.BOTH_SIDES else DeliveryMode.ONE_DIRECTION) },
                    )
                }
            }

            val pending = day.pendingStops
            val delivered = day.deliveredStops
            val transferred = day.transferredStops

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = Spacing.screenMargin, vertical = Spacing.sm),
                verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                item {
                    RouteMapPreview(
                        stops = day.nonXStops,
                        currentStopId = pending.firstOrNull()?.id,
                        clip = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Sizing.mapPreviewHeight),
                    )
                }

                if (pending.isEmpty() && delivered.isEmpty() && transferred.isEmpty()) {
                    item {
                        Text(
                            "Bu gündə ünvan yoxdur",
                            style = AppType.body,
                            color = MaterialTheme.appColors.textMuted,
                            modifier = Modifier.padding(vertical = Spacing.lg),
                        )
                    }
                }

                itemsIndexed(pending) { index, stop ->
                    StopCard(
                        stop = stop,
                        dayState = day.state,
                        isNext = index == 0,
                        onCardClick = { sheet = DaySheet.Navigation(stop.id) },
                        onCall = { callStop(stop) },
                        onWhatsApp = { whatsAppStop(stop) },
                        onAddPhoto = { sheet = DaySheet.AddPhoto(stop.id) },
                        onDeliver = {
                            updateStop(stop.id) { it.copy(status = StopStatus.DELIVERED) }
                        },
                        onDeliverBlocked = {
                            scope.launch { snackbarHostState.showAppSnackbar("Əvvəlcə şəkil əlavə edin") }
                        },
                        onCannotDeliver = { sheet = DaySheet.CannotDeliver(stop.id) },
                        onEdit = { sheet = DaySheet.Edit(stop.id) },
                        onTransfer = { sheet = DaySheet.Transfer(stop.id) },
                    )
                }

                if (delivered.isNotEmpty()) {
                    item {
                        CollapsibleSectionHeader(
                            title = "Verildi (${delivered.size})",
                            expanded = deliveredExpanded,
                            onToggle = { deliveredExpanded = !deliveredExpanded },
                        )
                    }
                    if (deliveredExpanded) {
                        items(delivered, key = { "d-" + it.id }) { stop ->
                            CompletedStopRow(stop = stop, onClick = { sheet = DaySheet.Navigation(stop.id) })
                        }
                    }
                }

                if (transferred.isNotEmpty()) {
                    item {
                        CollapsibleSectionHeader(
                            title = "Keçirildi (${transferred.size})",
                            expanded = transferredExpanded,
                            onToggle = { transferredExpanded = !transferredExpanded },
                            tint = MaterialTheme.appColors.transfer,
                        )
                    }
                    if (transferredExpanded) {
                        items(transferred, key = { "t-" + it.id }) { stop ->
                            CompletedStopRow(stop = stop, onClick = { sheet = DaySheet.Navigation(stop.id) })
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }

    fun stopOrNull(id: String): Stop? = day.stops.find { it.id == id }

    when (val s = sheet) {
        is DaySheet.Navigation -> stopOrNull(s.stopId)?.let { stop ->
            NavigationChoiceSheet(
                customerName = stop.fullName,
                address = stop.address,
                onDismissRequest = { sheet = null },
                onWaze = { openMaps(stop, waze = true) },
                onGoogleMaps = { openMaps(stop, waze = false) },
            )
        }
        is DaySheet.AddPhoto -> AddPhotoSheet(
            onDismissRequest = { sheet = null },
            onCamera = {
                updateStop(s.stopId) { it.copy(photoCount = it.photoCount + 1) }
                sheet = null
                scope.launch { snackbarHostState.showAppSnackbar("Şəkil əlavə edildi") }
            },
            onGallery = {
                updateStop(s.stopId) { it.copy(photoCount = it.photoCount + 1) }
                sheet = null
                scope.launch { snackbarHostState.showAppSnackbar("Şəkil əlavə edildi") }
            },
        )
        is DaySheet.CannotDeliver -> stopOrNull(s.stopId)?.let { stop ->
            CannotDeliverSheet(
                customerName = stop.fullName,
                onDismissRequest = { sheet = null },
                onConfirm = { reason ->
                    updateStop(stop.id) { it.copy(status = StopStatus.X, xReason = reason) }
                    sheet = null
                },
            )
        }
        is DaySheet.Edit -> stopOrNull(s.stopId)?.let { stop ->
            AddressFormSheet(
                onDismissRequest = { sheet = null },
                isEdit = true,
                initialName = stop.fullName,
                initialPhone = stop.phone,
                initialNote = stop.note.orEmpty(),
                onSubmit = { result: AddressFormResult ->
                    updateStop(stop.id) { it.copy(fullName = result.fullName, phone = result.phone, note = result.note) }
                    sheet = null
                },
            )
        }
        is DaySheet.Transfer -> stopOrNull(s.stopId)?.let { stop ->
            val tomorrow = addDaysToAzDate(day.title, 1)
            val dayAfter = addDaysToAzDate(day.title, 2)
            MoveToAnotherDaySheet(
                customerName = stop.fullName,
                tomorrowDateLabel = tomorrow,
                dayAfterDateLabel = dayAfter,
                hasPhoto = stop.photoCount > 0 || stop.transferredFromDate != null,
                onDismissRequest = { sheet = null },
                onAddPhoto = {
                    updateStop(stop.id) { it.copy(photoCount = it.photoCount + 1) }
                },
                onConfirm = { target ->
                    updateStop(stop.id) { it.copy(status = StopStatus.TRANSFERRED, transferredToDate = target) }
                    sheet = null
                    scope.launch { snackbarHostState.showAppSnackbar("$target gününə keçirildi") }
                },
            )
        }
        DaySheet.SendPhotos -> SendPhotosSheet(
            dayTitle = day.title,
            photoCount = day.stops.sumOf { it.photoCount },
            recipientNumber = recipientNumber,
            onRecipientChange = { recipientNumber = it.filter(Char::isDigit).take(10) },
            businessIndex = businessIndex,
            onBusinessIndexChange = { businessIndex = it },
            onDismissRequest = { sheet = null },
            onOpenWhatsApp = {
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + azPhoneToE164Digits(recipientNumber))))
                } catch (e: ActivityNotFoundException) {
                    scope.launch { snackbarHostState.showAppSnackbar("WhatsApp tapılmadı") }
                }
                sheet = null
            },
        )
        null -> {}
    }

    if (showActivateConfirm) {
        ConfirmDialog(
            title = "${day.title} aktiv edilsin?",
            onDismiss = { showActivateConfirm = false },
            confirmText = "Aktiv et",
            onConfirm = {
                day = day.copy(state = DayState.ACTIVE)
                showActivateConfirm = false
            },
        )
    }
}

@Composable
private fun DayTopBar(day: Day, onBack: () -> Unit, onOpenXList: (Day) -> Unit) {
    val colors = MaterialTheme.appColors
    Column(modifier = Modifier.background(colors.surface).fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xs, vertical = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = colors.textPrimary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(day.title, style = AppType.title, color = colors.textPrimary)
                    Spacer(Modifier.width(Spacing.xs))
                    DayStateBadge(state = day.state)
                }
                Text(
                    "${day.completedCount} / ${day.totalCount}",
                    style = AppType.caption,
                    color = colors.textSecondary,
                )
            }
            if (day.xStops.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(end = Spacing.sm)
                        .clickable { onOpenXList(day) },
                ) {
                    XCountBadge(count = day.xStops.size)
                }
            }
        }
    }
}

@Composable
private fun UpcomingBanner(dayTitle: String, onActivate: () -> Unit) {
    val colors = MaterialTheme.appColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.transferContainer)
            .padding(Spacing.screenMargin),
    ) {
        Text("Bu gün hələ aktiv deyil", style = AppType.title, color = colors.onTransferContainer)
        Spacer(Modifier.height(Spacing.sm))
        PrimaryButton(
            text = "Aktiv et",
            onClick = onActivate,
            containerColor = colors.transfer,
            contentColor = colors.onTransfer,
        )
    }
}

@Composable
private fun FinishedBanner() {
    val colors = MaterialTheme.appColors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.finishedContainer)
            .padding(Spacing.screenMargin),
    ) {
        Text("Bu gün bitib", style = AppType.label, color = colors.onFinishedContainer)
    }
}

@Composable
private fun CollapsibleSectionHeader(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.appColors.textSecondary,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.material3.TextButton(onClick = onToggle) {
            Text(title, style = AppType.label, color = tint)
            Spacer(Modifier.width(Spacing.xxs))
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = tint,
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun DayScreenActivePreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DayScreen(initialDay = SampleData.activeDay, onBack = {}, onOpenXList = {})
    }
}

@LightDarkPreview
@Composable
private fun DayScreenUpcomingPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DayScreen(initialDay = SampleData.upcomingDay, onBack = {}, onOpenXList = {})
    }
}

@LightDarkPreview
@Composable
private fun DayScreenFinishedPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DayScreen(initialDay = SampleData.finishedDay, onBack = {}, onOpenXList = {})
    }
}

@LightDarkPreview
@Composable
private fun DayScreenEmptyPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DayScreen(initialDay = SampleData.freshEmptyActiveDay, onBack = {}, onOpenXList = {})
    }
}
