package com.gunlukmarsrut.courier.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.gunlukmarsrut.courier.data.Day
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.SampleData
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.data.xStops
import com.gunlukmarsrut.courier.ui.components.AddressFormResult
import com.gunlukmarsrut.courier.ui.components.AddressFormSheet
import com.gunlukmarsrut.courier.ui.components.AppSnackbarHost
import com.gunlukmarsrut.courier.ui.components.EmptyState
import com.gunlukmarsrut.courier.ui.components.XCard
import com.gunlukmarsrut.courier.ui.components.showAppSnackbar
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import com.gunlukmarsrut.courier.ui.util.LightDarkPreview
import kotlinx.coroutines.launch

/** "22.09.26 X" — pulled-out addresses for one day. "Geri qaytar" only while that day is active. */
@Composable
fun XListScreen(
    initialDay: Day,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var day by remember(initialDay.id) { mutableStateOf(initialDay) }
    var editingStopId by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.appColors

    val canReturn = day.state == DayState.ACTIVE

    Scaffold(
        modifier = modifier,
        containerColor = colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface)
                    .padding(horizontal = Spacing.xs, vertical = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = colors.textPrimary)
                }
                Text("${day.title} X", style = AppType.title, color = colors.textPrimary)
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) { padding ->
        val xStops = day.xStops
        if (xStops.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState(title = "X siyahısı boşdur")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = Spacing.screenMargin, vertical = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                items(xStops, key = { it.id }) { stop ->
                    XCard(
                        stop = stop,
                        canReturn = canReturn,
                        onEdit = { editingStopId = stop.id },
                        onReturn = {
                            day = day.copy(stops = day.stops.map {
                                if (it.id == stop.id) it.copy(status = StopStatus.PENDING, xReason = null) else it
                            })
                            scope.launch { snackbarHostState.showAppSnackbar("${stop.fullName} günə qaytarıldı") }
                        },
                    )
                }
            }
        }
    }

    val editingStop = day.stops.find { it.id == editingStopId }
    if (editingStop != null) {
        AddressFormSheet(
            onDismissRequest = { editingStopId = null },
            isEdit = true,
            initialName = editingStop.fullName,
            initialPhone = editingStop.phone,
            initialNote = editingStop.note.orEmpty(),
            onSubmit = { result: AddressFormResult ->
                day = day.copy(stops = day.stops.map {
                    if (it.id == editingStop.id) it.copy(fullName = result.fullName, phone = result.phone, note = result.note) else it
                })
                editingStopId = null
            },
        )
    }
}

@LightDarkPreview
@Composable
private fun XListScreenPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        XListScreen(initialDay = SampleData.activeDay, onBack = {})
    }
}

@LightDarkPreview
@Composable
private fun XListScreenEmptyPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        XListScreen(initialDay = SampleData.finishedDayClean, onBack = {})
    }
}
