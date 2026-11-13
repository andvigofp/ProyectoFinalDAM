package com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.viemodel.ustate.RestablecerContrasenaUiState

@Composable
fun CamposRestablecerContrasenaComponent(
    uiState: RestablecerContrasenaUiState,
    onContrasenaChange: (String) -> Unit,
    onCodigoChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CampoRecuperacion(
            label = stringResource(R.string.email),
            value = uiState.email,
            placeholder = stringResource(R.string.email_placeholder),
            error = uiState.emailError,
            keyboardType = KeyboardType.Email,
            onValueChange = {},
            enabled = false
        )
        CampoRecuperacion(
            label = stringResource(R.string.new_password),
            value = uiState.nuevaContrasena,
            placeholder = stringResource(R.string.password_placeholder),
            error = uiState.contrasenaError,
            keyboardType = KeyboardType.Password,
            onValueChange = onContrasenaChange,
            enabled = !uiState.isLoading && uiState.successMessage == null,
            ocultarTexto = true
        )
        CampoRecuperacion(
            label = stringResource(R.string.recovery_code),
            value = uiState.codigo,
            placeholder = stringResource(R.string.recovery_code_placeholder),
            error = uiState.codigoError,
            keyboardType = KeyboardType.Number,
            onValueChange = onCodigoChange,
            enabled = !uiState.isLoading && uiState.successMessage == null
        )
    }
}

@Composable
private fun CampoRecuperacion(
    label: String,
    value: String,
    placeholder: String,
    error: String?,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    ocultarTexto: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            visualTransformation = if (ocultarTexto) {
                PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            enabled = enabled
        )
    }
}
