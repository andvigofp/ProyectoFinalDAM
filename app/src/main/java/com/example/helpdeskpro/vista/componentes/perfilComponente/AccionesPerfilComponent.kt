package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.modelo.ButtonType
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componentes.alertDialogComponente.ConfirmationDialog
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton

@Composable
fun AccionesPerfilComponent(
    onEditarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarConfirmacionCerrarSesion by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppButton(
            data = ButtonData(nombre = R.string.edit_profile),
            onClick = onEditarPerfil,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        )
        AppButton(
            data = ButtonData(nombre = R.string.logout, type = ButtonType.DANGER),
            onClick = { mostrarConfirmacionCerrarSesion = true },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        )
    }

    ConfirmationDialog(
        showDialog = mostrarConfirmacionCerrarSesion,
        onDismissRequest = { mostrarConfirmacionCerrarSesion = false },
        title = stringResource(R.string.confirm_logout_title),
        message = stringResource(R.string.confirm_logout_message),
        confirmButtonText = stringResource(R.string.logout),
        dismissButtonText = stringResource(R.string.dialog_cancel),
        onConfirmClick = onCerrarSesion,
        isDestructive = true
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun AccionesPerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        AccionesPerfilComponent(onEditarPerfil = {}, onCerrarSesion = {})
    }
}
