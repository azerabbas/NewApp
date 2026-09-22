package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.InputShape
import com.gunlukmarsrut.courier.ui.theme.PillShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** Fully-rounded selectable chip — quick-pick dates, mode toggles, filters. */
@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    val bg = if (selected) colors.primary else colors.surface
    val fg = if (selected) colors.onPrimary else colors.textPrimary
    val borderColor = if (selected) colors.primary else colors.border

    Text(
        text = text,
        style = AppType.label,
        color = fg,
        modifier = modifier
            .clip(PillShape)
            .background(bg)
            .border(BorderStroke(1.dp, borderColor), PillShape)
            .clickable { onClick() }
            .padding(horizontal = Spacing.md, vertical = Spacing.xs),
    )
}

/** Two/three-way segmented control — the Day screen's "Hər iki tərəf | Bir istiqamətli" switch. */
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(InputShape)
            .background(colors.surface2)
            .padding(3.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Text(
                text = label,
                style = AppType.label,
                color = if (selected) colors.primary else colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(InputShape)
                    .background(if (selected) colors.surface else androidx.compose.ui.graphics.Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(vertical = Spacing.xs),
            )
        }
    }
}
