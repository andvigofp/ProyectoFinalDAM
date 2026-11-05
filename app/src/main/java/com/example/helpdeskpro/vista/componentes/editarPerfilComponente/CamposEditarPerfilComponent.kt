package com.example.helpdeskpro.vista.componentes.editarPerfilComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.viemodel.ustate.EditarPerfilUiState

@Composable
fun CamposEditarPerfilComponent(
    uiState: EditarPerfilUiState,
    onNombreChange: (String) -> Unit,
    onApellidosChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val perfil = uiState.perfil ?: return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CampoEditarPerfil(
            label = stringResource(R.string.name),
            value = perfil.nombre,
            placeholder = stringResource(R.string.name_placeholder),
            error = uiState.nombreError,
            onValueChange = onNombreChange
        )
        CampoEditarPerfil(
            label = stringResource(R.string.surnames),
            value = perfil.apellidos,
            placeholder = stringResource(R.string.surnames_placeholder),
            error = uiState.apellidosError,
            onValueChange = onApellidosChange
        )
        CampoEditarPerfil(
            label = stringResource(R.string.email),
            value = perfil.email,
            placeholder = stringResource(R.string.account_email_placeholder),
            keyboardType = KeyboardType.Email,
            error = uiState.emailError,
            onValueChange = onEmailChange
        )
        CampoEditarPerfil(
            label = stringResource(R.string.phone),
            value = perfil.telefono,
            placeholder = stringResource(R.string.phone_placeholder),
            keyboardType = KeyboardType.Phone,
            error = uiState.telefonoError,
            onValueChange = onTelefonoChange
        )
        CampoEditarPerfil(
            label = stringResource(R.string.password),
            value = uiState.contrasena,
            placeholder = stringResource(R.string.password_optional_placeholder),
            keyboardType = KeyboardType.Password,
            error = uiState.contrasenaError,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onContrasenaChange
        )
    }
}

@Composable
private fun CampoEditarPerfil(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
