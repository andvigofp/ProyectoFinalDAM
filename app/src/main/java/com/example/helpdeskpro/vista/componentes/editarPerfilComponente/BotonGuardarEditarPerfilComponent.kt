package com.example.helpdeskpro.vista.componentes.editarPerfilComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton

@Composable
fun BotonGuardarEditarPerfilComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean = false
) {
    AppButton(
        data = ButtonData(
            nombre = if (isLoading) R.string.saving else R.string.save,
            enabled = enabled && !isLoading
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    )
}
