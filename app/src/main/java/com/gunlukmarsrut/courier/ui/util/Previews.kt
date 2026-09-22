package com.gunlukmarsrut.courier.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/** Renders every annotated composable twice — light and dark — at phone width. */
@Preview(name = "Açıq (Light)", group = "theme", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Qaranlıq (Dark)", group = "theme", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class LightDarkPreview
