package com.example.helpdeskpro.vista.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private val CrearCuentaButtonColor = Color(0xFF8998AD)

/**
 * Formulario reutilizable de registro. El estado y la validación se gestionan fuera.
 */
@Composable
fun CrearCuentaComponent(
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
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier,
    roles: List<String> = listOf("EMPLEADO", "TECNICO", "ADMINISTRADOR"),
    departamentos: List<String> = listOf("Ventas", "Sistemas"),
    nombreError: String? = null,
    apellidosError: String? = null,
    emailError: String? = null,
    telefonoError: String? = null,
    passwordError: String? = null,
    rolError: String? = null,
    departamentoError: String? = null,
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CampoTextoCuenta(
            label = "Nombre",
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = "Ana",
            error = nombreError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !isLoading
        )

        CampoTextoCuenta(
            label = "Apellidos",
            value = apellidos,
            onValueChange = onApellidosChange,
            placeholder = "Pérez García",
            error = apellidosError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !isLoading
        )

        CampoTextoCuenta(
            label = "Email",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "ana@empresa.com",
            error = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            enabled = !isLoading
        )

        CampoTextoCuenta(
            label = "Teléfono",
            value = telefono,
            onValueChange = onTelefonoChange,
            placeholder = "600 123 456",
            error = telefonoError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            enabled = !isLoading
        )

        CampoTextoCuenta(
            label = "Contraseña",
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Introduce tu contraseña",
            error = passwordError,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(
                    onClick = { passwordVisible = !passwordVisible },
                    enabled = !isLoading
                ) {
                    Text(if (passwordVisible) "Ocultar" else "Mostrar")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!isLoading) onGuardar()
                }
            ),
            enabled = !isLoading
        )

        SelectorCuenta(
            label = "Rol",
            value = rol,
            placeholder = "EMPLEADO / TECNICO / ADMINISTRADOR",
            options = roles,
            onOptionSelected = onRolChange,
            error = rolError,
            enabled = !isLoading
        )

        SelectorCuenta(
            label = "Departamento",
            value = departamento,
            placeholder = "Ventas / Sistemas …",
            options = departamentos,
            onOptionSelected = onDepartamentoChange,
            error = departamentoError,
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onGuardar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = CrearCuentaButtonColor)
        ) {
            Text(
                text = if (isLoading) "Guardando…" else "Guardar",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CampoTextoCuenta(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
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
            supportingText = {
                if (error != null) Text(error)
            },
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorCuenta(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                placeholder = { Text(placeholder) },
                singleLine = true,
                isError = error != null,
                supportingText = {
                    if (error != null) Text(error)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                enabled = enabled
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
