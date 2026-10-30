package com.example.helpdeskpro.vista.pagina

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.CrearCuentaComponent

/**
 * Pantalla de registro sin estado propio: recibe los datos y delega los eventos.
 */
@Composable
fun CrearCuentaPage(
    modifier: Modifier = Modifier,
    nombre: String = "",
    apellidos: String = "",
    email: String = "",
    telefono: String = "",
    password: String = "",
    rol: String = "",
    departamento: String = "",
    onNombreChange: (String) -> Unit = {},
    onApellidosChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onTelefonoChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRolChange: (String) -> Unit = {},
    onDepartamentoChange: (String) -> Unit = {},
    onGuardar: () -> Unit = {},
    onVolverLogin: () -> Unit = {},
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVolverLogin,
                enabled = !isLoading
            ) {
                Text(
                    text = "‹",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        CrearCuentaComponent(
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            telefono = telefono,
            password = password,
            rol = rol,
            departamento = departamento,
            onNombreChange = onNombreChange,
            onApellidosChange = onApellidosChange,
            onEmailChange = onEmailChange,
            onTelefonoChange = onTelefonoChange,
            onPasswordChange = onPasswordChange,
            onRolChange = onRolChange,
            onDepartamentoChange = onDepartamentoChange,
            onGuardar = onGuardar,
            modifier = Modifier.fillMaxWidth(),
            nombreError = nombreError,
            apellidosError = apellidosError,
            emailError = emailError,
            telefonoError = telefonoError,
            passwordError = passwordError,
            rolError = rolError,
            departamentoError = departamentoError,
            errorMessage = errorMessage,
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CrearCuentaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CrearCuentaPage()
    }
}
