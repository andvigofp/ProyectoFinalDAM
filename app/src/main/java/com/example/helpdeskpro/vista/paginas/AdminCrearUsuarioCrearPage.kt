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
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componentes.adminEditarUsuarioTenico.CamposAdminUsuarioComponent
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.SuccessDialog
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

@Composable
fun AdminCrearUsuarioCrearPage(
    usuario: AdminUsuarioDTO?,
    idNuevoUsuario: Long,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onGuardar: (AdminUsuarioDTO, String?, () -> Unit) -> Unit = { _, _, onGuardado -> onGuardado() }
) {
    var nombre by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.nombre.orEmpty()) }
    var apellidos by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.apellidos.orEmpty()) }
    var email by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.email.orEmpty()) }
    var telefono by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.telefono.orEmpty()) }
    var password by rememberSaveable(usuario?.id) { mutableStateOf("") }
    var rol by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.rol.orEmpty()) }
    var departamento by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.departamento.orEmpty()) }
    var activo by rememberSaveable(usuario?.id) { mutableStateOf(usuario?.activo ?: true) }
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }
    var mostrarExito by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        ToolbarComponent(
            titulo = stringResource(if (usuario == null) R.string.add_user else R.string.edit_user),
            onVolver = onVolver
        )
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
        AppButton(
            data = ButtonData(
                nombre = if (usuario == null) R.string.create else R.string.save,
                enabled = nombre.isNotBlank() && apellidos.isNotBlank() &&
                    email.isNotBlank() && rol.isNotBlank() && departamento.isNotBlank() &&
                    (usuario != null || password.isNotBlank())
            ),
            onClick = { mostrarConfirmacion = true },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacion,
        onDismissRequest = { mostrarConfirmacion = false },
        title = stringResource(R.string.confirm_action_title),
        message = stringResource(
            if (usuario == null) R.string.confirm_create_user_message
            else R.string.confirm_save_user_message
        ),
        confirmButtonText = stringResource(if (usuario == null) R.string.create else R.string.save),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = {
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
                ),
                password.takeIf { it.isNotBlank() }
            ) {
                mostrarExito = true
            }
        }
    )

    SuccessDialog(
        showDialog = mostrarExito,
        onDismissRequest = {
            mostrarExito = false
            onVolver()
        },
        message = stringResource(
            if (usuario == null) R.string.admin_user_created_success
            else R.string.admin_user_updated_success
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AdminUsuarioPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AdminCrearUsuarioCrearPage(usuario = null, idNuevoUsuario = 1)
        }
    }
}
