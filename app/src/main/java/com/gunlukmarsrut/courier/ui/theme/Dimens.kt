package com.gunlukmarsrut.courier.ui.theme

import androidx.compose.ui.unit.dp

/** 8pt grid (4pt for fine details) + the radii/sizes fixed by the design system. */
object Spacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 40.dp

    /** Screen side margin. */
    val screenMargin = 16.dp

    /** Gap between cards. */
    val cardGap = 12.dp

    /** Card inner padding. */
    val cardPadding = 16.dp
}

object Radius {
    val card = 16.dp
    val button = 12.dp
    val input = 12.dp
    val bottomSheet = 24.dp
    val pill = 999.dp
}

object Sizing {
    /** Minimum touch target. */
    val touchTarget = 48.dp

    /** Primary button height. */
    val primaryButton = 56.dp

    /** Secondary/icon button default height. */
    val standardButton = 48.dp

    val iconButton = 48.dp
    val icon = 24.dp

    /** Stop order-number circle. */
    val stopNumberCircle = 36.dp

    val avatarSmall = 32.dp
    val avatarMedium = 40.dp

    val mapPreviewHeight = 320.dp
    val mapPinSmall = 28.dp
    val mapPinCurrent = 40.dp

    val photoThumb = 56.dp
}
