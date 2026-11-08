package com.example.helpdeskpro.vista.componentes.homeAdminComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton

@Composable
fun AccionAdminComponent(
    texto: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppButton(
        data = ButtonData(nombre = texto),
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    )
}
