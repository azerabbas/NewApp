package com.gunlukmarsrut.courier.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Full design-system color token set. Material3's ColorScheme only covers
 * primary/error/etc, so the extra semantic tokens (success, warning,
 * transfer, whatsapp, surface-2, text-muted) live here and are exposed via
 * [LocalAppColors].
 */
data class AppColorTokens(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val danger: Color,
    val onDanger: Color,
    val dangerContainer: Color,
    val onDangerContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val transfer: Color,
    val onTransfer: Color,
    val transferContainer: Color,
    val onTransferContainer: Color,

    val whatsapp: Color,
    val onWhatsapp: Color,

    val background: Color,
    val surface: Color,
    val surface2: Color,
    val border: Color,
    val scrim: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textOnPrimary: Color,

    val finished: Color,
    val onFinished: Color,
    val finishedContainer: Color,
    val onFinishedContainer: Color,
)

val LightAppColors = AppColorTokens(
    primary = Color(0xFF1E3A8A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDDE6FB),
    onPrimaryContainer = Color(0xFF11265C),

    success = Color(0xFF15803D),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFDCF5E4),
    onSuccessContainer = Color(0xFF0C4B25),

    danger = Color(0xFFD92D20),
    onDanger = Color(0xFFFFFFFF),
    dangerContainer = Color(0xFFFDE4E1),
    onDangerContainer = Color(0xFF7A170F),

    warning = Color(0xFFB65C00),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFDEDD1),
    onWarningContainer = Color(0xFF6B3900),

    transfer = Color(0xFF6D28D9),
    onTransfer = Color(0xFFFFFFFF),
    transferContainer = Color(0xFFEEE4FC),
    onTransferContainer = Color(0xFF3D1878),

    whatsapp = Color(0xFF25D366),
    onWhatsapp = Color(0xFF07200F),

    background = Color(0xFFF5F7FA),
    surface = Color(0xFFFFFFFF),
    surface2 = Color(0xFFEEF1F6),
    border = Color(0xFFE2E6ED),
    scrim = Color(0x66101828),

    textPrimary = Color(0xFF101828),
    textSecondary = Color(0xFF475467),
    textMuted = Color(0xFF6D7480),
    textOnPrimary = Color(0xFFFFFFFF),

    finished = Color(0xFF667085),
    onFinished = Color(0xFFFFFFFF),
    finishedContainer = Color(0xFFE9EBEF),
    onFinishedContainer = Color(0xFF475467),
)

val DarkAppColors = AppColorTokens(
    primary = Color(0xFF7DA0FF),
    onPrimary = Color(0xFF0B1A3E),
    primaryContainer = Color(0xFF1B2C55),
    onPrimaryContainer = Color(0xFFDCE6FF),

    success = Color(0xFF3DD68C),
    onSuccess = Color(0xFF07301A),
    successContainer = Color(0xFF12402A),
    onSuccessContainer = Color(0xFFBBF2D3),

    danger = Color(0xFFFF7A6E),
    onDanger = Color(0xFF3A0904),
    dangerContainer = Color(0xFF4A1B15),
    onDangerContainer = Color(0xFFFFD9D3),

    warning = Color(0xFFF2A93B),
    onWarning = Color(0xFF3B2400),
    warningContainer = Color(0xFF44300E),
    onWarningContainer = Color(0xFFFBE0B0),

    transfer = Color(0xFFB794F6),
    onTransfer = Color(0xFF2A1550),
    transferContainer = Color(0xFF352262),
    onTransferContainer = Color(0xFFE9DCFD),

    whatsapp = Color(0xFF25D366),
    onWhatsapp = Color(0xFF07200F),

    background = Color(0xFF0F1720),
    surface = Color(0xFF18212C),
    surface2 = Color(0xFF212B37),
    border = Color(0xFF2E3947),
    scrim = Color(0x99000000),

    textPrimary = Color(0xFFF0F3F7),
    textSecondary = Color(0xFFAEB8C4),
    textMuted = Color(0xFF7B8794),
    textOnPrimary = Color(0xFF0B1A3E),

    finished = Color(0xFF8B96A3),
    onFinished = Color(0xFF0F1720),
    finishedContainer = Color(0xFF29323D),
    onFinishedContainer = Color(0xFFC2CAD3),
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }
