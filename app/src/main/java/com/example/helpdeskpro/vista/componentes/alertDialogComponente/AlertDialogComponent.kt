package com.example.helpdeskpro.vista.componentes.alertDialogComponente

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ButtonData
import com.example.helpdeskpro.modelo.ButtonType
import com.example.helpdeskpro.vista.componentes.buttonsComponente.AppButton

/**
 * Componente de AlertDialog reutilizable para toda la aplicación
 * @author Andrés
 * @param showDialog Booleano que controla si mostrar el diálogo
 * @param onDismissRequest Función llamada cuando se descarta el diálogo
 * @param title Título del diálogo
 * @param message Mensaje del diálogo
 * @param confirmButtonText Texto del botón de confirmación (por defecto "OK")
 * @param dismissButtonText Texto del botón de cancelación (opcional, si es null no se muestra)
 * @param onConfirmClick Función llamada al confirmar
 * @param onDismissClick Función llamada al cancelar (si dismissButtonText no es null)
 * @param isDestructive Indica si es un diálogo de acción destructiva (rojo)
 * @param shape Forma del diálogo
 */
@Composable
fun AlertDialogComponent(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmButtonText: String? = null,
    dismissButtonText: String? = null,
    onConfirmClick: () -> Unit = {},
    onDismissClick: () -> Unit = {},
    isDestructive: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    if (showDialog) {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = title,
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
            },
            text = {
                Text(
                    text = message,
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            },
            confirmButton = {
                confirmButtonText?.let { label ->
                    AppButton(
                        data = ButtonData(
                            nombre = R.string.dialog_confirm,
                            type = if (isDestructive) ButtonType.DANGER else ButtonType.PRIMARY
                        ),
                        onClick = {
                            onConfirmClick()
                            onDismissRequest()
                        },
                        text = label
                    )
                }
            },
            dismissButton = if (dismissButtonText != null) {
                {
                    TextButton(
                        onClick = {
                            onDismissClick()
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = dismissButtonText,
                            style = typography.labelLarge,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            } else {
                null
            },
            containerColor = colors.surface,
            shape = shape,
            tonalElevation = 8.dp
        )
    }
}

/**
 * Versión simplificada para diálogos de información
 */
@Composable
fun InfoDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,

    ) {
    AlertDialogComponent(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        title = title,
        message = message,

        dismissButtonText = null
    )
}

/**
 * Diálogo de confirmación (Sí/No)
 */
@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmButtonText: String = "Confirmar",
    dismissButtonText: String = "Cancelar",
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit = {},
    isDestructive: Boolean = false
) {
    AlertDialogComponent(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText,
        dismissButtonText = dismissButtonText,
        onConfirmClick = onConfirmClick,
        onDismissClick = onDismissClick,
        isDestructive = isDestructive
    )
}

/**
 * Diálogo de éxito
 */
@Composable
fun SuccessDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String = "¡Éxito!",
    message: String,
    confirmButtonText: String = "Continuar"
) {
    AlertDialogComponent(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText
    )
}

/**
 * Diálogo de error
 */
@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    title: String = "Error",
    message: String,
    confirmButtonText: String = "Aceptar"
) {
    AlertDialogComponent(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        title = title,
        message = message,
        confirmButtonText = confirmButtonText
    )
}

@Preview
@Composable
fun AlertDialogPreview() {
    AlertDialogComponent(
        showDialog = true,
        onDismissRequest = {},
        title = "Título del Diálogo",
        message = "Este es un mensaje de ejemplo para mostrar cómo se ve el AlertDialogComponent en la aplicación.",
        onConfirmClick = { /* Acción al confirmar */ },
        onDismissClick = { /* Acción al cancelar */ },
        isDestructive = false
    )
}