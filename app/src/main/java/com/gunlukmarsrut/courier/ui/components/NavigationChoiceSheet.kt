package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** d) Navigation choice: customer name, then Waze / Google Maps. */
@Composable
fun NavigationChoiceSheet(
    customerName: String,
    address: String?,
    onDismissRequest: () -> Unit,
    onWaze: () -> Unit,
    onGoogleMaps: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text(customerName, style = AppType.title, color = colors.textPrimary)
        if (address != null) {
            Spacer(Modifier.height(Spacing.xxs))
            Text(address, style = AppType.body, color = colors.textSecondary)
        }
        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton(text = "Waze", onClick = onWaze)
        Spacer(Modifier.height(Spacing.sm))
        SecondaryButton(text = "Google Maps", onClick = onGoogleMaps)
    }
}
