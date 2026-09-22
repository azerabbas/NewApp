package com.gunlukmarsrut.courier.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gunlukmarsrut.courier.ui.components.AppTextField
import com.gunlukmarsrut.courier.ui.components.PasswordField
import com.gunlukmarsrut.courier.ui.components.PrimaryButton
import com.gunlukmarsrut.courier.ui.theme.AppType
import com.gunlukmarsrut.courier.ui.theme.GunlukMarsrutTheme
import com.gunlukmarsrut.courier.ui.theme.Spacing
import com.gunlukmarsrut.courier.ui.theme.appColors
import com.gunlukmarsrut.courier.ui.util.LightDarkPreview

/** Stateful entry point: owns the form and the fake "kuryer" / "12345" check. */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    LoginScreenContent(
        username = username,
        password = password,
        showError = showError,
        loading = loading,
        onUsernameChange = { username = it; showError = false },
        onPasswordChange = { password = it; showError = false },
        onSubmit = {
            loading = true
            val ok = username.trim().equals("kuryer", ignoreCase = true) && password == "12345"
            loading = false
            if (ok) onLoginSuccess() else showError = true
        },
        modifier = modifier,
    )
}

@Composable
private fun LoginScreenContent(
    username: String,
    password: String,
    showError: Boolean,
    loading: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.appColors
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.screenMargin)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(Spacing.xxl * 2))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(colors.primary, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = colors.onPrimary, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(Spacing.md))
            Text(
                "Günlük Marşrut",
                style = AppType.display,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(Spacing.xxl))

            AppTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "İstifadəçi adı",
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.sm))
            PasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Şifrə",
                modifier = Modifier.fillMaxWidth(),
            )

            if (showError) {
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    "İstifadəçi adı və ya şifrə yanlışdır",
                    style = AppType.caption,
                    color = colors.danger,
                )
            }

            Spacer(Modifier.height(Spacing.lg))

            PrimaryButton(
                text = "Daxil ol",
                loading = loading,
                enabled = username.isNotBlank() && password.isNotBlank(),
                onClick = onSubmit,
            )

            Spacer(Modifier.height(Spacing.xxl))
        }
    }
}

@LightDarkPreview
@Composable
private fun LoginScreenPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        LoginScreenContent(
            username = "kuryer",
            password = "12345",
            showError = false,
            loading = false,
            onUsernameChange = {},
            onPasswordChange = {},
            onSubmit = {},
        )
    }
}

@LightDarkPreview
@Composable
private fun LoginScreenErrorPreview() {
    GunlukMarsrutTheme(darkTheme = isSystemInDarkTheme()) {
        LoginScreenContent(
            username = "kuryer",
            password = "yanlis",
            showError = true,
            loading = false,
            onUsernameChange = {},
            onPasswordChange = {},
            onSubmit = {},
        )
    }
}
