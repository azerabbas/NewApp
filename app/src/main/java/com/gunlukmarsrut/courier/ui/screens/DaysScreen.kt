package com.gunlukmarsrut.courier.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Day
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.SampleData
import com.gunlukmarsrut.courier.data.completedCount
import com.gunlukmarsrut.courier.data.totalCount
import com.gunlukmarsrut.courier.data.xStops
import com.gunlukmarsrut.courier.ui.components.ActivateDayDialog
import com.gunlukmarsrut.courier.ui.components.AppBottomSheet
import com.gunlukmarsrut.courier.ui.components.AppTextField
import com.gunlukmarsrut.courier.ui.components.DayCard
import com.gunlukmarsrut.courier.ui.components.DayXLinkCard
import com.gunlukmarsrut.courier.ui.components.EmptyState
import com.gunlukmarsrut.courier.ui.components.PrimaryButton
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import com.gunlukmarsrut.courier.ui.util.LightDarkPreview
import java.text.SimpleDateFormat
import java.util.Locale

/** Home screen: the day list, newest first, with the "+" create-day flow. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaysScreen(
    onOpenDay: (Day) -> Unit,
    onOpenXList: (Day) -> Unit,
    modifier: Modifier = Modifier,
    username: String = "Elvin Məmmədov",
    initialDays: List<Day> = SampleData.allDays,
    onOpenComponents: () -> Unit = {},
) {
    var days by remember { mutableStateOf(initialDays) }
    var showCreateSheet by remember { mutableStateOf(false) }
    var pendingTitle by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val currentActive = days.find { it.state == DayState.ACTIVE }

    fun finalizeCreate(title: String) {
        days = days.map { if (it.state == DayState.ACTIVE) it.copy(state = DayState.FINISHED) else it } +
            Day(id = "day-${System.currentTimeMillis()}", title = title, state = DayState.ACTIVE)
        pendingTitle = null
    }

    fun requestCreate(rawTitle: String) {
        val title = rawTitle.trim()
        if (title.isBlank()) return
        showCreateSheet = false
        val existing = days.find { it.title.equals(title, ignoreCase = true) }
        if (existing != null) {
            onOpenDay(existing)
            return
        }
        if (currentActive != null) {
            pendingTitle = title
        } else {
            finalizeCreate(title)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.appColors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = MaterialTheme.appColors.primary,
                contentColor = MaterialTheme.appColors.onPrimary,
                shape = CircleShape,
                modifier = Modifier.size(64.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Yeni gün", modifier = Modifier.size(32.dp))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenMargin, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(username)
                Spacer(Modifier.size(Spacing.xs))
                Text(username, style = AppType.label, color = MaterialTheme.appColors.textSecondary)
            }

            Text(
                "Günlər",
                style = AppType.display,
                color = MaterialTheme.appColors.textPrimary,
                modifier = Modifier
                    .padding(horizontal = Spacing.screenMargin)
                    .combinedClickable(onClick = {}, onLongClick = onOpenComponents),
            )

            Spacer(Modifier.height(Spacing.md))

            if (days.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    EmptyState(title = "Hələ gün yoxdur. + ilə ilk günü yaradın.")
                }
            } else {
                val sorted = days.sortedByDescending { parseAzDate(it.title) }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = Spacing.screenMargin, vertical = Spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
                ) {
                    items(sorted, key = { it.id }) { day ->
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            DayCard(day = day, onClick = { onOpenDay(day) })
                            if (day.xStops.isNotEmpty()) {
                                DayXLinkCard(
                                    dayTitle = day.title,
                                    xCount = day.xStops.size,
                                    onClick = { onOpenXList(day) },
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showCreateSheet) {
        NewDaySheet(
            onDismissRequest = { showCreateSheet = false },
            onCreate = { requestCreate(it) },
        )
    }

    if (pendingTitle != null && currentActive != null) {
        val unfinished = currentActive.totalCount - currentActive.completedCount
        ActivateDayDialog(
            newDayTitle = pendingTitle!!,
            currentActiveDayTitle = currentActive.title,
            unfinishedCount = unfinished,
            onDismiss = { pendingTitle = null },
            onConfirm = { finalizeCreate(pendingTitle!!) },
            onLookFirst = { onOpenDay(currentActive) },
        )
    }
}

@Composable
private fun UserAvatar(name: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = modifier
            .size(32.dp)
            .background(colors.primaryContainer, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(initial, style = AppType.captionMedium, color = colors.onPrimaryContainer)
    }
}

@Composable
private fun NewDaySheet(
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit,
) {
    var title by remember { androidx.compose.runtime.mutableStateOf("") }
    AppBottomSheet(onDismissRequest = onDismissRequest) {
        Text("Yeni gün", style = AppType.title, color = MaterialTheme.appColors.textPrimary)
        Spacer(Modifier.height(Spacing.md))
        AppTextField(
            value = title,
            onValueChange = { title = it },
            label = "Gün adı",
            placeholder = "Məs. 22.09.26",
        )
        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton(text = "Yarat", onClick = { onCreate(title) }, enabled = title.isNotBlank())
    }
}

private fun parseAzDate(title: String): Long = try {
    SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(title)?.time ?: 0L
} catch (e: Exception) {
    0L
}

@LightDarkPreview
@Composable
private fun DaysScreenPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DaysScreen(onOpenDay = {}, onOpenXList = {})
    }
}

@LightDarkPreview
@Composable
private fun DaysScreenEmptyPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        DaysScreen(onOpenDay = {}, onOpenXList = {}, initialDays = SampleData.emptyDays)
    }
}
