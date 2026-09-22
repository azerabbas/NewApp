package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/**
 * c) New-address notifications: neutral "7-ci sıraya əlavə olundu" and the
 * amber "Geri qayıdış — +3.4 km" variant with an "X et" undo action.
 * Both auto-dismiss after 4 seconds (SnackbarDuration.Short).
 */
data class AppSnackbarVisuals(
    override val message: String,
    val isWarning: Boolean = false,
    override val actionLabel: String? = null,
) : SnackbarVisuals {
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

suspend fun SnackbarHostState.showAppSnackbar(
    message: String,
    isWarning: Boolean = false,
    actionLabel: String? = null,
) = showSnackbar(AppSnackbarVisuals(message = message, isWarning = isWarning, actionLabel = actionLabel))

@Composable
fun AppSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.appColors
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        val isWarning = (data.visuals as? AppSnackbarVisuals)?.isWarning == true
        Snackbar(
            modifier = Modifier.padding(Spacing.md),
            shape = RoundedCornerShape(12.dp),
            containerColor = if (isWarning) colors.warning else colors.textPrimary,
            contentColor = if (isWarning) colors.onWarning else colors.surface,
            action = data.visuals.actionLabel?.let { label ->
                {
                    TextButton(onClick = { data.performAction() }) {
                        Text(label, style = AppType.label, color = if (isWarning) colors.onWarning else colors.surface)
                    }
                }
            },
        ) {
            Text(data.visuals.message, style = AppType.body)
        }
    }
}
