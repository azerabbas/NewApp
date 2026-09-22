package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button as M3Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton as M3TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.ButtonShape
import com.gunlukmarsrut.courier.ui.theme.Sizing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** 16/22 medium — a touch larger than the 14sp design-system label so primary CTAs stay legible in sunlight. */
private val ButtonLabelStyle = AppType.label.copy(fontSize = 16.sp, lineHeight = 22.sp)

/** 56dp — the screen's one dominant primary action. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.appColors.primary,
    contentColor: Color = MaterialTheme.appColors.onPrimary,
) {
    M3Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizing.primaryButton),
        enabled = enabled && !loading,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.appColors.surface2,
            disabledContentColor = MaterialTheme.appColors.textMuted,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(22.dp), color = contentColor, strokeWidth = 2.5.dp)
        } else {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(Sizing.icon))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, style = ButtonLabelStyle)
        }
    }
}

@Composable
fun DangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        containerColor = MaterialTheme.appColors.danger,
        contentColor = MaterialTheme.appColors.onDanger,
    )
}

/** Violet action button — e.g. "Keçir" / "Aktiv et" on transfer flows. */
@Composable
fun TransferButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        containerColor = MaterialTheme.appColors.transfer,
        contentColor = MaterialTheme.appColors.onTransfer,
    )
}

/** Same footprint as PrimaryButton but outlined — used next to a primary action. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizing.primaryButton),
        enabled = enabled,
        shape = ButtonShape,
        border = BorderStroke(1.5.dp, if (enabled) MaterialTheme.appColors.primary else MaterialTheme.appColors.border),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.appColors.primary,
            disabledContentColor = MaterialTheme.appColors.textMuted,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(Sizing.icon))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = ButtonLabelStyle)
    }
}

/** Outlined, danger-tinted, compact — e.g. "Verilə bilmədi" on the stop card. */
@Composable
fun DangerOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(Sizing.standardButton),
        enabled = enabled,
        shape = ButtonShape,
        border = BorderStroke(1.5.dp, if (enabled) MaterialTheme.appColors.danger else MaterialTheme.appColors.border),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.appColors.danger,
            disabledContentColor = MaterialTheme.appColors.textMuted,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Text(text, style = AppType.label)
    }
}

/** Solid, compact — e.g. "Verildi" on the stop card. */
@Composable
fun CompactSolidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.appColors.success,
    contentColor: Color = MaterialTheme.appColors.onSuccess,
) {
    M3Button(
        onClick = onClick,
        modifier = modifier.height(Sizing.standardButton),
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.appColors.surface2,
            disabledContentColor = MaterialTheme.appColors.textMuted,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Text(text, style = AppType.label)
    }
}

@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.appColors.textSecondary,
) {
    M3TextButton(
        onClick = onClick,
        modifier = modifier.height(Sizing.standardButton),
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.appColors.textMuted,
        ),
    ) {
        Text(text, style = AppType.label)
    }
}

/** Round icon button — Call, WhatsApp, overflow, back. Default 48dp minimum touch target. */
@Composable
fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.appColors.primaryContainer,
    contentColor: Color = MaterialTheme.appColors.primary,
    enabled: Boolean = true,
    size: Dp = Sizing.iconButton,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .size(size)
            .background(
                color = if (enabled) containerColor else MaterialTheme.appColors.surface2,
                shape = CircleShape,
            ),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (enabled) contentColor else MaterialTheme.appColors.textMuted,
            modifier = Modifier.size(Sizing.icon),
        )
    }
}
