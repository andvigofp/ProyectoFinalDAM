package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun EmailRecuperacionComponent(
    email: String,
    onEmailChange: (String) -> Unit,
    onEnviar: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.email),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            singleLine = true,
            isError = error != null,
            supportingText = { if (error != null) Text(error) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { if (enabled) onEnviar() }),
            enabled = enabled
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun EmailRecuperacionComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        EmailRecuperacionComponent(
            email = "",
            onEmailChange = {},
            onEnviar = {}
        )
    }
}
