package com.gunlukmarsrut.courier.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.DayState
import com.gunlukmarsrut.courier.data.SampleData
import com.gunlukmarsrut.courier.data.StopStatus
import com.gunlukmarsrut.courier.ui.components.CompactSolidButton
import com.gunlukmarsrut.courier.ui.components.CompletedStopRow
import com.gunlukmarsrut.courier.ui.components.DangerButton
import com.gunlukmarsrut.courier.ui.components.DangerOutlinedButton
import com.gunlukmarsrut.courier.ui.components.DayCard
import com.gunlukmarsrut.courier.ui.components.DayStateBadge
import com.gunlukmarsrut.courier.ui.components.DayXLinkCard
import com.gunlukmarsrut.courier.ui.components.EmptyState
import com.gunlukmarsrut.courier.ui.components.ErrorState
import com.gunlukmarsrut.courier.ui.components.GhostButton
import com.gunlukmarsrut.courier.ui.components.LoadingState
import com.gunlukmarsrut.courier.ui.components.NoteBanner
import com.gunlukmarsrut.courier.ui.components.NoteField
import com.gunlukmarsrut.courier.ui.components.AppTextField
import com.gunlukmarsrut.courier.ui.components.PhoneInputField
import com.gunlukmarsrut.courier.ui.components.PrimaryButton
import com.gunlukmarsrut.courier.ui.components.RoundIconButton
import com.gunlukmarsrut.courier.ui.components.RouteMapPreview
import com.gunlukmarsrut.courier.ui.components.SecondaryButton
import com.gunlukmarsrut.courier.ui.components.SegmentedControl
import com.gunlukmarsrut.courier.ui.components.SelectableChip
import com.gunlukmarsrut.courier.ui.components.StopCard
import com.gunlukmarsrut.courier.ui.components.TransferButton
import com.gunlukmarsrut.courier.ui.components.TransferredFromChip
import com.gunlukmarsrut.courier.ui.components.XCard
import com.gunlukmarsrut.courier.ui.components.XCountBadge
import com.gunlukmarsrut.courier.ui.theme.AppColorTokens
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.DarkAppColors
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme
import com.gunlukmarsrut.courier.ui.theme.LightAppColors
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import androidx.compose.ui.tooling.preview.Preview

/**
 * The design-system reference page: tokens and every component variant,
 * rendered once per theme so light/dark can be compared side by side.
 */
@Composable
fun ComponentsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        GunlukMarsrutTheme(darkTheme = false) {
            Box(modifier = Modifier.background(MaterialTheme.appColors.background)) {
                DesignSystemContent(themeLabel = "İşıqlı tema (Light)")
            }
        }
        GunlukMarsrutTheme(darkTheme = true) {
            Box(modifier = Modifier.background(MaterialTheme.appColors.background)) {
                DesignSystemContent(themeLabel = "Qaranlıq tema (Dark)")
            }
        }
    }
}

@Composable
private fun DesignSystemContent(themeLabel: String) {
    val colors = MaterialTheme.appColors
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.screenMargin)) {
        Text(themeLabel, style = AppType.display, color = colors.textPrimary)
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Rənglər")
        ColorSwatchGrid(colors)
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Tipoqrafiya")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text("Display 28/34 — ekran başlıqları", style = AppType.display, color = colors.textPrimary)
            Text("Title 20/26 — kart başlıqları", style = AppType.title, color = colors.textPrimary)
            Text("Body 16/24 — əsas mətn", style = AppType.body, color = colors.textPrimary)
            Text("Label 14/20 — düymələr, çiplər", style = AppType.label, color = colors.textPrimary)
            Text("Caption 12/16 — ikinci dərəcəli mətn", style = AppType.caption, color = colors.textSecondary)
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Düymələr — default / disabled")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            PrimaryButton(text = "Əsas düymə", onClick = {})
            PrimaryButton(text = "Əsas düymə (deaktiv)", onClick = {}, enabled = false)
            SecondaryButton(text = "İkinci dərəcəli düymə", onClick = {})
            DangerButton(text = "Təhlükəli düymə", onClick = {})
            GhostButton(text = "Ghost düymə", onClick = {})
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                CompactSolidButton(text = "Verildi", onClick = {}, modifier = Modifier.weight(1f))
                DangerOutlinedButton(text = "Verilə bilmədi", onClick = {}, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                RoundIconButton(icon = Icons.Filled.Call, contentDescription = "Zəng", onClick = {})
                RoundIconButton(icon = Icons.Filled.Call, contentDescription = "Zəng (deaktiv)", onClick = {}, enabled = false)
            }
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Inputlar")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            var text by remember { mutableStateOf("") }
            AppTextField(value = text, onValueChange = { text = it }, label = "Ad Soyad", placeholder = "Ad Soyad")
            AppTextField(value = "", onValueChange = {}, label = "Ad Soyad", errorText = "Bu sahə tələb olunur")
            var phone by remember { mutableStateOf("") }
            PhoneInputField(value = phone, onValueChange = { phone = it })
            var note by remember { mutableStateOf("") }
            NoteField(value = note, onValueChange = { note = it })
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Badge və çiplər")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                DayStateBadge(state = DayState.ACTIVE)
                DayStateBadge(state = DayState.UPCOMING)
                DayStateBadge(state = DayState.FINISHED)
            }
            TransferredFromChip(date = "22.09.26")
            XCountBadge(count = 4)
            NoteBanner(text = "Həyətdə mühafizəçiyə ver")
            var chipSelected by remember { mutableStateOf(0) }
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                SelectableChip(text = "Sabah (23.09.26)", selected = chipSelected == 0, onClick = { chipSelected = 0 })
                SelectableChip(text = "Birigün (24.09.26)", selected = chipSelected == 1, onClick = { chipSelected = 1 })
            }
            var segment by remember { mutableStateOf(0) }
            SegmentedControl(options = listOf("Hər iki tərəf", "Bir istiqamətli"), selectedIndex = segment, onSelect = { segment = it })
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Gün kartı")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            DayCard(day = SampleData.activeDay)
            DayXLinkCard(dayTitle = SampleData.activeDay.title, xCount = 3)
            DayCard(day = SampleData.upcomingDay)
            DayCard(day = SampleData.finishedDay)
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Dayanacaq kartı (Stop card)")
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            val sampleStop = SampleData.activeDay.stops.first()
            StopCard(stop = sampleStop, dayState = DayState.ACTIVE, isNext = true)
            StopCard(stop = sampleStop.copy(orderNumber = 12, photoCount = 2), dayState = DayState.ACTIVE)
            CompletedStopRow(stop = SampleData.activeDay.stops.first { it.status == StopStatus.DELIVERED })
            CompletedStopRow(stop = SampleData.activeDay.stops.first { it.status == StopStatus.TRANSFERRED })
        }
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("X kartı")
        XCard(stop = SampleData.activeDay.stops.first { it.status == StopStatus.X }, canReturn = true)
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Xəritə önizləməsi")
        RouteMapPreview(
            stops = SampleData.activeDay.stops.take(12),
            clip = true,
            modifier = Modifier.fillMaxWidth().height(220.dp),
        )
        Spacer(Modifier.height(Spacing.lg))

        SectionTitle("Vəziyyətlər — boş / yüklənir / xəta")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CardShape)
                .background(colors.surface),
        ) {
            EmptyState(title = "Hələ gün yoxdur. + ilə ilk günü yaradın.")
            HorizontalDivider(color = colors.border)
            LoadingState()
            HorizontalDivider(color = colors.border)
            ErrorState(title = "Yüklənmə alınmadı", onRetry = {})
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = AppType.title,
        color = MaterialTheme.appColors.textPrimary,
        modifier = Modifier.padding(bottom = Spacing.sm),
    )
}

@Composable
private fun ColorSwatchGrid(colors: AppColorTokens) {
    val entries = listOf(
        "primary" to colors.primary,
        "success" to colors.success,
        "danger" to colors.danger,
        "warning" to colors.warning,
        "transfer" to colors.transfer,
        "whatsapp" to colors.whatsapp,
        "surface" to colors.surface,
        "surface-2" to colors.surface2,
        "background" to colors.background,
        "border" to colors.border,
        "text-primary" to colors.textPrimary,
        "text-secondary" to colors.textSecondary,
        "text-muted" to colors.textMuted,
    )
    val rows = entries.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                row.forEach { (name, color) ->
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(name, style = AppType.caption, color = colors.textSecondary)
                    }
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Preview(name = "Design system", showBackground = true, widthDp = 390, heightDp = 3200)
@Composable
private fun ComponentsScreenPreview() {
    ComponentsScreen()
}
