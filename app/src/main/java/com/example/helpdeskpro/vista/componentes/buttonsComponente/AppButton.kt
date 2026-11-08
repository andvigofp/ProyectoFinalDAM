package com.example.helpdeskpro.vista.componentes.buttonsComponente

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.modelo.ButtonType

@Composable
fun AppButton(
    data: ButtonData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null
) {
    val buttonText = text ?: stringResource(id = data.nombre)

    when (data.type) {
        ButtonType.PRIMARY ->
            PrimaryButton(
                text = buttonText,
                onClick = onClick,
                enabled = data.enabled,
                modifier = modifier
            )

        ButtonType.SECONDARY ->
            SecondaryButton(
                text = buttonText,
                onClick = onClick,
                enabled = data.enabled,
                modifier = modifier
            )

        ButtonType.DANGER ->
            DangerButton(
                text = buttonText,
                onClick = onClick,
                enabled = data.enabled,
                modifier = modifier
            )

        ButtonType.OUTLINE ->
            OutlineButton(
                text = buttonText,
                onClick = onClick,
                enabled = data.enabled,
                modifier = modifier
            )
    }
}