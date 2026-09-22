package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Sizing
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** i) Send photos: thumbnail grid, remembered recipient number, WhatsApp / WhatsApp Business. */
@Composable
fun SendPhotosSheet(
    dayTitle: String,
    photoCount: Int,
    recipientNumber: String,
    onRecipientChange: (String) -> Unit,
    businessIndex: Int,
    onBusinessIndexChange: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onOpenWhatsApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors

    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text(dayTitle, style = AppType.title, color = colors.textPrimary)
        Spacer(Modifier.height(Spacing.xxs))
        Text("$photoCount şəkil göndəriləcək", style = AppType.body, color = colors.textSecondary)
        Spacer(Modifier.height(Spacing.md))

        PhotoThumbnailGrid(count = photoCount)

        Spacer(Modifier.height(Spacing.md))
        PhoneInputField(
            value = recipientNumber,
            onValueChange = onRecipientChange,
            label = "Nömrə",
        )
        Spacer(Modifier.height(Spacing.md))
        SegmentedControl(
            options = listOf("WhatsApp", "WhatsApp Business"),
            selectedIndex = businessIndex,
            onSelect = onBusinessIndexChange,
        )
        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton(
            text = "WhatsApp-da aç",
            onClick = onOpenWhatsApp,
            enabled = recipientNumber.length == 10,
            containerColor = colors.whatsapp,
            contentColor = colors.onWhatsapp,
        )
    }
}

@Composable
private fun PhotoThumbnailGrid(count: Int, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    val shown = count.coerceAtMost(15)
    val columns = 5
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        (0 until shown step columns).forEach { rowStart ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                for (i in rowStart until minOf(rowStart + columns, shown)) {
                    Box(
                        modifier = Modifier
                            .size(Sizing.photoThumb)
                            .clip(CardShape)
                            .background(colors.surface2),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Photo,
                            contentDescription = null,
                            tint = colors.textMuted,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }
        }
        if (count > shown) {
            Text("+${count - shown} daha", style = AppType.caption, color = colors.textMuted)
        }
    }
}
