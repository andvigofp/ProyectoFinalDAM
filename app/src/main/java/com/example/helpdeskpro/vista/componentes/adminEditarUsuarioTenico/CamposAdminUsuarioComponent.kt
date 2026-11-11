package com.example.helpdeskpro.vista.componentes.adminEditarUsuarioTenico

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.vista.componentes.crearCuentaComponente.SelectorCuentaComponent

@Composable
fun CamposAdminUsuarioComponent(
    nombre: String,
    apellidos: String,
    email: String,
    telefono: String,
    password: String,
    rol: String,
    departamento: String,
    onNombreChange: (String) -> Unit,
    onApellidosChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onDepartamentoChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    emailEditable: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CampoAdminUsuario(
            label = stringResource(R.string.name),
            value = nombre,
            placeholder = stringResource(R.string.name_placeholder),
            onValueChange = onNombreChange
        )
        CampoAdminUsuario(
            label = stringResource(R.string.surnames),
            value = apellidos,
            placeholder = stringResource(R.string.surnames_placeholder),
            onValueChange = onApellidosChange
        )
        CampoAdminUsuario(
            label = stringResource(R.string.email),
            value = email,
            placeholder = stringResource(R.string.account_email_placeholder),
            keyboardType = KeyboardType.Email,
            onValueChange = onEmailChange,
            enabled = emailEditable
        )
        CampoAdminUsuario(
            label = stringResource(R.string.phone),
            value = telefono,
            placeholder = stringResource(R.string.phone_placeholder),
            keyboardType = KeyboardType.Phone,
            onValueChange = onTelefonoChange
        )
        CampoAdminUsuario(
            label = stringResource(R.string.password),
            value = password,
            placeholder = stringResource(R.string.password_mask),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChange
        )
        SelectorCuentaComponent(
            label = stringResource(R.string.role),
            value = rol,
            placeholder = stringResource(R.string.role_placeholder),
            options = listOf(
                stringResource(R.string.role_employee),
                stringResource(R.string.role_technician),
                stringResource(R.string.role_administrator)
            ),
            onOptionSelected = onRolChange
        )
        SelectorCuentaComponent(
            label = stringResource(R.string.department),
            value = departamento,
            placeholder = stringResource(R.string.department_placeholder),
            options = listOf(
                stringResource(R.string.department_informatics),
                stringResource(R.string.department_human_resources),
                stringResource(R.string.department_administration),
                stringResource(R.string.department_production)
            ),
            onOptionSelected = onDepartamentoChange
        )
    }
}

@Composable
private fun CampoAdminUsuario(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None,
    enabled: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
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
