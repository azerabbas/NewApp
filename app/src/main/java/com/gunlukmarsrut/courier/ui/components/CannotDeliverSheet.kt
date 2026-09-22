package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** f) Could not deliver / X: "Niyə çıxarılır?" with a required reason note. */
@Composable
fun CannotDeliverSheet(
    customerName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (reason: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    var reason by remember { mutableStateOf("") }

    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text("Niyə çıxarılır?", style = AppType.title, color = colors.textPrimary)
        Spacer(Modifier.height(Spacing.xxs))
        Text(customerName, style = AppType.body, color = colors.textSecondary)
        Spacer(Modifier.height(Spacing.md))
        NoteField(
            value = reason,
            onValueChange = { reason = it },
            label = "Səbəb",
            placeholder = "Məs. ünvanda kimsə yoxdur",
        )
        Spacer(Modifier.height(Spacing.lg))
        DangerButton(
            text = "X et",
            onClick = { onConfirm(reason.trim()) },
            enabled = reason.isNotBlank(),
        )
    }
}
