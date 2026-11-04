package com.example.helpdeskpro.vista.componentes.crearCuentaComponente

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState

@Composable
fun CamposCrearCuentaComponent(
    uiState: CrearCuentaUiState,
    onNombreChange: (String) -> Unit,
    onApellidosChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onDepartamentoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.name),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = onNombreChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.name_placeholder)) },
                singleLine = true,
                isError = uiState.nombreError != null,
                supportingText = { uiState.nombreError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !uiState.isLoading
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.surnames),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = uiState.apellidos,
                onValueChange = onApellidosChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.surnames_placeholder)) },
                singleLine = true,
                isError = uiState.apellidosError != null,
                supportingText = { uiState.apellidosError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !uiState.isLoading
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.email),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.account_email_placeholder)) },
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                enabled = !uiState.isLoading
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.phone),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = uiState.telefono,
                onValueChange = onTelefonoChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.phone_placeholder)) },
                singleLine = true,
                isError = uiState.telefonoError != null,
                supportingText = { uiState.telefonoError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                enabled = !uiState.isLoading
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                singleLine = true,
                isError = uiState.passwordError != null,
                supportingText = { uiState.passwordError?.let { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (!uiState.isLoading) onGuardar() }
                ),
                enabled = !uiState.isLoading
            )
        }
        SelectorCuentaComponent(
            label = stringResource(R.string.role),
            value = uiState.rol,
            placeholder = stringResource(R.string.role_placeholder),
            options = listOf("EMPLEADO", "TECNICO", "ADMINISTRADOR"),
            onOptionSelected = onRolChange,
            error = uiState.rolError,
            enabled = !uiState.isLoading
        )
        SelectorCuentaComponent(
            label = stringResource(R.string.department),
            value = uiState.departamento,
            placeholder = stringResource(R.string.department_placeholder),
            options = listOf("Ventas", "Sistemas"),
            onOptionSelected = onDepartamentoChange,
            error = uiState.departamentoError,
            enabled = !uiState.isLoading
        )
        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CamposCrearCuentaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CamposCrearCuentaComponent(
            uiState = CrearCuentaUiState(),
            onNombreChange = {},
            onApellidosChange = {},
            onEmailChange = {},
            onTelefonoChange = {},
            onPasswordChange = {},
            onRolChange = {},
            onDepartamentoChange = {},
            onGuardar = {}
        )
    }
}
