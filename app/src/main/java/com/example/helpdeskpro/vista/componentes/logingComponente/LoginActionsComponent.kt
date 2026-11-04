package com.example.helpdeskpro.vista.componentes.logingComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun LoginActionsComponent(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun LoginActionsComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        LoginActionsComponent(
            onForgotPassword = {},
            onCreateAccount = {}
        )
    }
}
