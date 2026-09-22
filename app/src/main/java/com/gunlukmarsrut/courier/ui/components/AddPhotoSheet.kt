package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** e) Add photo: two large options — Kamera / Qalereya. */
@Composable
fun AddPhotoSheet(
    onDismissRequest: () -> Unit,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text("Şəkil əlavə et", style = AppType.title, color = colors.textPrimary)
        Spacer(Modifier.height(Spacing.lg))
        PrimaryButton(text = "Kamera", onClick = onCamera, icon = Icons.Filled.CameraAlt)
        Spacer(Modifier.height(Spacing.sm))
        SecondaryButton(text = "Qalereya", onClick = onGallery, icon = Icons.Filled.PhotoLibrary)
    }
}
