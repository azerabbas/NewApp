package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.data.Stop
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.CardShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

data class AddressFormResult(val fullName: String, val phone: String, val note: String?)

/**
 * Covers both (a) Add address — opened when a location is shared into the app — and
 * (b) Edit customer, which reuses the exact same layout pre-filled.
 */
@Composable
fun AddressFormSheet(
    onDismissRequest: () -> Unit,
    onSubmit: (AddressFormResult) -> Unit,
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    activeDayTitle: String? = "22.09.26",
    onCreateDay: (() -> Unit)? = null,
    initialName: String = "",
    initialPhone: String = "",
    initialNote: String = "",
    stops: List<Stop> = emptyList(),
) {
    val colors = MaterialTheme.appColors
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var note by remember { mutableStateOf(initialNote) }

    AppBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        Text(
            text = if (isEdit) "Redaktə et" else "Yeni ünvan",
            style = AppType.display,
            color = colors.textPrimary,
        )
        Spacer(Modifier.height(Spacing.md))

        if (!isEdit) {
            RouteMapPreview(
                stops = stops,
                clip = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            )
            Spacer(Modifier.height(Spacing.md))
        }

        if (!isEdit && activeDayTitle == null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.warningContainer, RoundedCornerShape(12.dp))
                    .padding(Spacing.md),
            ) {
                Text("Aktiv gün yoxdur", style = AppType.body, color = colors.onWarningContainer)
                Spacer(Modifier.height(Spacing.sm))
                if (onCreateDay != null) {
                    PrimaryButton(text = "Gün yarat", onClick = onCreateDay)
                }
            }
            return@AppBottomSheet
        }

        if (!isEdit && activeDayTitle != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface2, RoundedCornerShape(12.dp))
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            ) {
                Text("Aktiv gün: $activeDayTitle", style = AppType.label, color = colors.textSecondary)
            }
            Spacer(Modifier.height(Spacing.md))
        }

        AppTextField(value = name, onValueChange = { name = it }, label = "Ad Soyad", placeholder = "Ad Soyad")
        Spacer(Modifier.height(Spacing.sm))
        PhoneInputField(value = phone, onValueChange = { phone = it })
        Spacer(Modifier.height(Spacing.sm))
        NoteField(value = note, onValueChange = { note = it })
        Spacer(Modifier.height(Spacing.lg))

        val canSubmit = name.isNotBlank() && phone.length == 10
        PrimaryButton(
            text = if (isEdit) "Yadda saxla" else "Əlavə et",
            onClick = { onSubmit(AddressFormResult(name.trim(), phone, note.trim().ifBlank { null })) },
            enabled = canSubmit,
        )
    }
}
