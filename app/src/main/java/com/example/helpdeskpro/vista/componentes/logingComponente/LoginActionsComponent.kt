package com.example.helpdeskpro.vista.componentes.logingComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val LoginButtonColor = Color(0xFF8998AD)

@Composable
fun LoginActionsComponent(
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = LoginButtonColor)
        ) {
            Text(
                text = stringResource(if (isLoading) R.string.logging_in else R.string.login),
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onForgotPassword, enabled = !isLoading) {
                Text(stringResource(R.string.forgot_password))
            }
            TextButton(onClick = onCreateAccount, enabled = !isLoading) {
                Text(stringResource(R.string.create_account))
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun LoginActionsComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        LoginActionsComponent(
            onLogin = {},
            onForgotPassword = {},
            onCreateAccount = {}
        )
    }
}
