package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componentes.adminEditarUsuarioTenico.CamposAdminUsuarioComponent
import com.example.helpdeskpro.vista.componentes.perfilComponente.BotonVolverPerfilComponent

@Composable
fun AdminCrearUsuarioCrearPage(
    usuario: AdminUsuarioDTO?,
    idNuevoUsuario: Long,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onGuardar: (AdminUsuarioDTO) -> Unit = {}
) {
    var nombre by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.nombre.orEmpty()) }
    var apellidos by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.apellidos.orEmpty()) }
    var email by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.email.orEmpty()) }
    var telefono by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.telefono.orEmpty()) }
    var password by rememberSaveable(usuario?.id) { mutableStateOf("") }
    var rol by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.rol.orEmpty()) }
    var departamento by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.departamento.orEmpty()) }
    var activo by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.activo ?: true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonVolverPerfilComponent(onClick = onVolver)
            Text(
                text = stringResource(if (usuario == null) R.string.add_user else R.string.edit_user),
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        CamposAdminUsuarioComponent(
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            telefono = telefono,
            password = password,
            rol = rol,
            departamento = departamento,
            onNombreChange = { nombre = it },
            onApellidosChange = { apellidos = it },
            onEmailChange = { email = it },
            onTelefonoChange = { telefono = it },
            onPasswordChange = { password = it },
            onRolChange = { rol = it },
            onDepartamentoChange = { departamento = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.active_label),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Switch(checked = activo, onCheckedChange = { activo = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onGuardar(
                    AdminUsuarioDTO(
                        id = usuario?.id ?: idNuevoUsuario,
                        nombre = nombre.trim(),
                        apellidos = apellidos.trim(),
                        email = email.trim(),
                        telefono = telefono.trim(),
                        rol = rol,
                        departamento = departamento,
                        activo = activo
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nombre.isNotBlank() && apellidos.isNotBlank() &&
                email.isNotBlank() && rol.isNotBlank() && departamento.isNotBlank() &&
                (usuario != null || password.isNotBlank()),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8092B0)),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(if (usuario == null) R.string.create else R.string.save),
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminUsuarioPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        Surface(color = Color.White) {
            AdminCrearUsuarioCrearPage(usuario = null, idNuevoUsuario = 1)
        }
    }
}
