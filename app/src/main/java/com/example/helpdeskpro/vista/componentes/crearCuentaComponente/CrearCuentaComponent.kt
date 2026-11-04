package com.example.helpdeskpro.vista.componentes.crearCuentaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val CrearCuentaButtonColor = Color(0xFF8998AD)

/**
 * Formulario reutilizable de registro. El estado y la validación se gestionan fuera.
 *
 * @param nombre nombre actual del usuario.
 * @param apellidos apellidos actuales del usuario.
 * @param email correo actual del usuario.
 * @param telefono teléfono actual del usuario.
 * @param password contraseña actual del usuario.
 * @param rol rol seleccionado.
 * @param departamento departamento seleccionado.
 * @param onNombreChange callback para cambiar el nombre.
 * @param onApellidosChange callback para cambiar los apellidos.
 * @param onEmailChange callback para cambiar el correo.
 * @param onTelefonoChange callback para cambiar el teléfono.
 * @param onPasswordChange callback para cambiar la contraseña.
 * @param onRolChange callback para cambiar el rol.
 * @param onDepartamentoChange callback para cambiar el departamento.
 * @param onGuardar callback para enviar el formulario.
 * @param modifier modificador aplicado al formulario.
 * @param roles opciones disponibles de rol.
 * @param departamentos opciones disponibles de departamento.
 * @param nombreError error de validación del nombre.
 * @param apellidosError error de validación de los apellidos.
 * @param emailError error de validación del correo.
 * @param telefonoError error de validación del teléfono.
 * @param passwordError error de validación de la contraseña.
 * @param rolError error de validación del rol.
 * @param departamentoError error de validación del departamento.
 * @param errorMessage error general del formulario.
 * @param isLoading indica si hay una operación en curso.
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
        CampoTextoCuentaComponent(
            label = stringResource(R.string.name),
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = stringResource(R.string.name_placeholder),
            error = nombreError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !isLoading
        )

        CampoTextoCuentaComponent(
            label = stringResource(R.string.surnames),
            value = apellidos,
            onValueChange = onApellidosChange,
            placeholder = stringResource(R.string.surnames_placeholder),
            error = apellidosError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !isLoading
        )

        CampoTextoCuentaComponent(
            label = stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.account_email_placeholder),
            error = emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            enabled = !isLoading
        )

        CampoTextoCuentaComponent(
            label = stringResource(R.string.phone),
            value = telefono,
            onValueChange = onTelefonoChange,
            placeholder = stringResource(R.string.phone_placeholder),
            error = telefonoError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            enabled = !isLoading
        )

        CampoTextoCuentaComponent(
            label = stringResource(R.string.password),
            value = password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.password_placeholder),
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
                    Text(stringResource(if (passwordVisible) R.string.hide else R.string.show))
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

        SelectorCuentaComponent(
            label = stringResource(R.string.role),
            value = rol,
            placeholder = stringResource(R.string.role_placeholder),
            options = roles,
            onOptionSelected = onRolChange,
            error = rolError,
            enabled = !isLoading
        )

        SelectorCuentaComponent(
            label = stringResource(R.string.department),
            value = departamento,
            placeholder = stringResource(R.string.department_placeholder),
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
                text = stringResource(if (isLoading) R.string.saving else R.string.save),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CrearCuentaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CrearCuentaComponent(
            nombre = "Ana",
            apellidos = "Pérez García",
            email = "ana@empresa.com",
            telefono = "600 123 456",
            password = "123456",
            rol = "EMPLEADO",
            departamento = "Ventas",
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
