package com.gunlukmarsrut.courier.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.InputShape
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors

/** Label above, 12dp radius, danger-outlined error state with a message underneath. */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorText: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val isError = errorText != null
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppType.label,
            color = MaterialTheme.appColors.textSecondary,
        )
        Spacer(Modifier.height(Spacing.xxs))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = placeholder?.let { { Text(it, style = AppType.body, color = MaterialTheme.appColors.textMuted) } },
            textStyle = AppType.body,
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailing,
            shape = InputShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) MaterialTheme.appColors.danger else MaterialTheme.appColors.primary,
                unfocusedBorderColor = if (isError) MaterialTheme.appColors.danger else MaterialTheme.appColors.border,
                disabledBorderColor = MaterialTheme.appColors.border,
                focusedContainerColor = MaterialTheme.appColors.surface,
                unfocusedContainerColor = MaterialTheme.appColors.surface,
                disabledContainerColor = MaterialTheme.appColors.surface2,
                cursorColor = MaterialTheme.appColors.primary,
                focusedTextColor = MaterialTheme.appColors.textPrimary,
                unfocusedTextColor = MaterialTheme.appColors.textPrimary,
            ),
        )
        if (isError) {
            Text(
                text = errorText,
                style = AppType.caption,
                color = MaterialTheme.appColors.danger,
                modifier = Modifier.padding(top = Spacing.xxs, start = Spacing.xxs),
            )
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: String? = null,
) {
    var visible by remember { mutableStateOf(false) }
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        errorText = errorText,
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailing = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Şifrəni gizlət" else "Şifrəni göstər",
                    tint = MaterialTheme.appColors.textMuted,
                )
            }
        },
    )
}

/** Numeric keypad, auto-spaced "050 123 45 67", capped to 10 digits. "+994" is never shown. */
@Composable
fun PhoneInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Nömrə",
    errorText: String? = null,
) {
    AppTextField(
        value = value,
        onValueChange = { new -> onValueChange(new.filter { it.isDigit() }.take(10)) },
        label = label,
        modifier = modifier,
        placeholder = "050 123 45 67",
        errorText = errorText,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation,
    )
}

@Composable
fun NoteField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Qeyd",
    placeholder: String = "Məs. həyətdə mühafizəçiyə ver",
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        singleLine = false,
        minLines = 3,
    )
}

object PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: androidx.compose.ui.text.AnnotatedString): androidx.compose.ui.text.input.TransformedText {
        val digits = text.text
        val out = StringBuilder()
        for (i in digits.indices) {
            if (i == 3 || i == 6 || i == 8) out.append(' ')
            out.append(digits[i])
        }
        val offsetMapping = object : androidx.compose.ui.text.input.OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var spaces = 0
                if (offset > 3) spaces++
                if (offset > 6) spaces++
                if (offset > 8) spaces++
                return offset + spaces
            }

            override fun transformedToOriginal(offset: Int): Int {
                var spaces = 0
                if (offset > 3) spaces++
                if (offset > 7) spaces++
                if (offset > 10) spaces++
                return (offset - spaces).coerceIn(0, digits.length)
            }
        }
        return androidx.compose.ui.text.input.TransformedText(androidx.compose.ui.text.AnnotatedString(out.toString()), offsetMapping)
    }
}
